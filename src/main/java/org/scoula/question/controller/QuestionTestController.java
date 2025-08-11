package org.scoula.question.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import org.scoula.gpt.dto.ChatRequestDto;
import org.scoula.gpt.dto.ChatResponseDto;
import org.scoula.gpt.service.GptService;
import org.scoula.question.dto.QuestionResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question")
@Api(tags = "질문 API", description = "텍스트와 음성 질문 처리 API")
@Log4j2
public class QuestionTestController {
	private final GptService gptService;

	// application-dev.properties
	@Value("${naver.clova.api.key.secret}")
	private String clovaSpeechApiKey;

	@ApiOperation(value = "텍스트 질문 처리", notes = "텍스트 질문만 받아서 처리합니다.")
	@PostMapping("/text")
	public ResponseEntity<QuestionResponseDto> handleTextQuestion(
		@RequestBody Map<String, String> request,
		Authentication authentication) {
		// 인증된 사용자 정보 로깅
		String userEmail = authentication != null ? authentication.getName() : "비로그인";
		log.info("텍스트 질문 받음 - 사용자: {}, 질문: '{}'", userEmail, request.get("text"));

		String text = request.get("text");
		if (text == null || text.trim().isEmpty()) {
			QuestionResponseDto errorResponse = new QuestionResponseDto("ERROR", "텍스트 질문이 필요합니다.");
			return ResponseEntity.badRequest().body(errorResponse);
		}

		ChatResponseDto gptResponseDto = gptService.getChatResponse(new ChatRequestDto(text.trim()));

		log.info("gpt 응답 = " + gptResponseDto.getAnswer());

		QuestionResponseDto response = new QuestionResponseDto("SUCCESS", "텍스트 질문이 성공적으로 처리되었습니다.", text.trim(),
			gptResponseDto.getAnswer());
		return ResponseEntity.ok(response);
	}

	@ApiOperation(value = "음성 질문 처리", notes = "음성 파일만 받아서 처리하고 GPT를 통해 답변을 반환합니다.")
	@PostMapping("/voice")
	public ResponseEntity<QuestionResponseDto> handleVoiceQuestion(
		@RequestParam(value = "audio") MultipartFile audioFile,
		Authentication authentication) {

		// 인증된 사용자 정보 로깅
		String userEmail = authentication != null ? authentication.getName() : "비로그인";
		log.info("음성 질문 요청 받음 - 사용자: {}, 파일: {}", userEmail, audioFile.getOriginalFilename());

		if (audioFile == null || audioFile.isEmpty()) {
			QuestionResponseDto errorResponse = new QuestionResponseDto("ERROR", "음성 파일이 필요합니다.");
			return ResponseEntity.badRequest().body(errorResponse);
		}

		try {
			// 1. 음성 파일을 텍스트로 변환 (FFmpeg + CLOVA API)
			String speechToText = convertSpeechToText(audioFile);
			log.info("음성-> 텍스트 변환 결과: '{}'", speechToText);

			// 서비스 로직
			ChatResponseDto gptResponseDto = gptService.getChatResponse(new ChatRequestDto(speechToText));

			log.info("gpt 응답 = " + gptResponseDto.getAnswer());

			QuestionResponseDto response = new QuestionResponseDto("SUCCESS", "음성 질문이 성공적으로 처리되었습니다.", speechToText,
				gptResponseDto.getAnswer());
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			log.error("음성 질문 처리 실패: {}", e.getMessage(), e);
			QuestionResponseDto errorResponse = new QuestionResponseDto("ERROR",
				"음성 질문 처리 중 오류가 발생했습니다: " + e.getMessage());
			return ResponseEntity.internalServerError().body(errorResponse);
		}
	}

	/**
	 * 음성 파일을 텍스트로 변환 (FFmpeg + CLOVA Speech API)
	 */
	private String convertSpeechToText(MultipartFile webmAudioFile) throws IOException, InterruptedException {
		File inputWebmFile = null;
		File outputWavFile = null;
		String tempDir = System.getProperty("java.io.tmpdir");

		try {
			// 1. 전송받은 WebM 파일을 서버에 임시 저장
			String originalFilename = "input_" + System.currentTimeMillis() + ".webm";
			inputWebmFile = new File(tempDir, originalFilename);
			webmAudioFile.transferTo(inputWebmFile);

			// 2. FFmpeg으로 WebM -> WAV 변환
			// (로컬에 brew install ffmpeg으로 설치되어 있어야 함)
			FFmpeg ffmpeg = new FFmpeg();
			String wavFilename = originalFilename.replace(".webm", ".wav");
			outputWavFile = new File(tempDir, wavFilename);

			FFmpegBuilder builder = new FFmpegBuilder()
				.setInput(inputWebmFile.getAbsolutePath())
				.overrideOutputFiles(true)
				.addOutput(outputWavFile.getAbsolutePath())
				.setAudioCodec("pcm_s16le")
				.setAudioSampleRate(16_000)
				.setAudioChannels(1)
				.done();
			ffmpeg.run(builder);
			log.info("FFmpeg 변환 성공: {} -> {}", inputWebmFile.getName(), outputWavFile.getName());

			// 3. 변환된 WAV 파일을 CLOVA Speech API로 전송
			return callClovaApi(outputWavFile);

		} finally {
			// 4. 작업 후 임시 파일들 삭제
			if (inputWebmFile != null && inputWebmFile.exists()) {
				inputWebmFile.delete();
			}
			if (outputWavFile != null && outputWavFile.exists()) {
				outputWavFile.delete();
			}
		}
	}

	/**
	 * CLOVA Speech API 호출
	 */
	private String callClovaApi(File wavFile) throws IOException {
		log.info("ClovaAPI 호출 시작 : {}", wavFile.getName());

		String apiUrl = "https://clovaspeech-gw.ncloud.com/recog/v1/stt?lang=Kor";
		RestTemplate restTemplate = new RestTemplate();

		// HTTP 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-CLOVASPEECH-API-KEY", clovaSpeechApiKey);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

		// 3. HTTP 바디 설정 (WAV 파일을 byte 배열로)
		byte[] fileContent = Files.readAllBytes(wavFile.toPath());

		// 4. 헤더와 바디를 합쳐 요청 객체 생성
		HttpEntity<byte[]> requestEntity = new HttpEntity<>(fileContent, headers);

		// 5. API 호출
		log.info("CLOVA Speech API 호출 시작...");
		ResponseEntity<Map> response = restTemplate.exchange(
			apiUrl,
			HttpMethod.POST,
			requestEntity,
			Map.class
		);
		log.info("CLOVA Speech API 응답 수신. 상태 코드: {}", response.getStatusCode());

		// 6. 응답 결과에서 텍스트 추출
		if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
			// === 결과 TEXT! ===
			return (String)response.getBody().get("text");
		} else {
			throw new IOException("CLOVA API 호출 실패: " + response.getStatusCode());
		}
	}

	// --- 기존 GPT 관련 메서드 ---
	private String buildGptPrompt(String userQuestion) {
		return String.format(
			"당신은 금융 전문가입니다. 사용자의 질문에 대해 쉽고 친근하게 설명해주세요.\n\n" + "질문: %s\n\n" +
				"답변 가이드라인:\n" +
				"1. 어려운 금융 용어는 쉬운 말로 풀어서 설명해주세요\n" +
				"2. 구체적인 예시를 들어주세요\n" +
				"3. 친근한 톤으로 답변해주세요\n" +
				"4. 답변은 3-5문장 정도로 간결하게 해주세요",
			userQuestion
		);
	}

	private String callGptApi(String prompt) {
		log.debug("GPT API 호출 시뮬레이션 - 프롬프트 길이: {} 글자", prompt.length());
		return "금융 질문에 대한 AI 답변입니다. (임시 구현 - 실제로는 GPT API 응답)";
	}
}
