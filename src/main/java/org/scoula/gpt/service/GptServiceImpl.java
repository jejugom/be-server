// GptServiceImpl.java (Implementation with JSON Mode)
// GPT 응답을 'JSON 모드'로 강제하여 안정적으로 파싱하는 로직을 구현합니다.
package org.scoula.gpt.service;

import java.io.IOException;

import org.scoula.gpt.dto.ChatRequestDto;
import org.scoula.gpt.dto.DataResponseDto;
import org.scoula.gpt.dto.ErrorResponseDto;
import org.scoula.gpt.dto.GptParsedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.ResponseFormatJsonObject;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

@Service
public class GptServiceImpl implements GptService {

	private static final Logger log = LoggerFactory.getLogger(GptServiceImpl.class);

	private final OpenAIClient openAiClient;
	private final String systemPrompt;
	private final ObjectMapper objectMapper;

	public GptServiceImpl(@Value("${openai.api.key}") String apiKey,
		@Value("${gpt.system.prompt}") String systemPrompt) {
		this.openAiClient = OpenAIOkHttpClient.builder()
			.apiKey(apiKey)
			.build();
		this.systemPrompt = systemPrompt;
		this.objectMapper = new ObjectMapper();
		log.info("GptServiceImpl 초기화 완료: OpenAIClient 및 ObjectMapper 생성");
	}

	/**
	 * 사용자의 질문을 받아 GPT 응답을 요청하고, 받은 JSON을 파싱하여 구조화된 객체로 반환합니다.
	 * @param chatRequestDto 사용자의 질문이 담긴 DTO
	 * @return 파싱된 GPT 응답 객체 (성공 데이터 또는 오류 정보 포함)
	 */
	@Override
	public GptParsedResponse getGptResponseAndParse(ChatRequestDto chatRequestDto) {
		log.info("GPT 요청 시작 (JSON 모드): {}", chatRequestDto.getQuestion());

		try {
			// ChatCompletionCreateParams: GPT API 요청 파라미터를 설정합니다.
			ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
				.model(ChatModel.GPT_4O_MINI_2024_07_18) // JSON 모드를 안정적으로 지원하는 최신 모델 사용을 권장합니다.
				.addSystemMessage(this.systemPrompt)
				.addUserMessage(buildUserMessage(chatRequestDto.getQuestion()))

				// --- 핵심 수정 사항: JSON 모드 활성화 ---
				// 이 설정을 통해 GPT는 반드시 문법에 맞는 JSON 객체만 응답합니다.
				.responseFormat(
					ChatCompletionCreateParams.ResponseFormat.ofJsonObject(
						ResponseFormatJsonObject.builder().build()
					)
				)
				.maxCompletionTokens(1024)
				.build();

			ChatCompletion completion = openAiClient.chat().completions().create(params);
			log.info("GPT API 호출 성공 (JSON 모드)");

			if (completion.choices().isEmpty()) {
				log.warn("응답 choices가 비어있음");
				return GptParsedResponse.ofError("GPT 응답이 비어있습니다.");
			}

			// JSON 모드를 사용하면 응답은 항상 JSON 형식이므로, 별도 정리 없이 바로 파싱합니다.
			String rawContent = completion.choices().get(0).message().content().orElse("");
			log.info("GPT 원본 JSON 응답: {}", rawContent);

			return parseGptResponse(rawContent);

		} catch (Exception e) {
			log.error("GPT 서비스 처리 중 에러 발생: {}", e.getMessage(), e);
			return GptParsedResponse.ofError("GPT 서비스 처리 중 오류가 발생했습니다: " + e.getMessage());
		}
	}

	/**
	 * GPT에게 보낼 사용자 메시지를 JSON 형식으로 만듭니다.
	 * @param query 사용자의 원본 질문
	 * @return JSON 형식의 문자열
	 */
	private String buildUserMessage(String query) {
		// JSON 모드를 사용하므로, "json으로 답해줘" 같은 부가 설명은 제거해도 됩니다.
		return String.format("{\"task\": \"데이터 생성\", \"query\": \"%s\"}", query);
	}

	/**
	 * GPT의 응답 문자열을 파싱하여 GptParsedResponse 객체로 변환합니다.
	 * @param jsonContent GPT의 원본 JSON 응답 문자열
	 * @return 파싱된 응답 객체
	 */
	private GptParsedResponse parseGptResponse(String jsonContent) {
		try {
			// 먼저 오류 형식인지 확인
			ErrorResponseDto errorDto = tryParse(jsonContent, ErrorResponseDto.class);
			if (errorDto != null && errorDto.isError()) {
				log.warn("GPT가 오류 응답을 반환: {}", errorDto.getMessage());
				return GptParsedResponse.ofError(errorDto.getMessage());
			}

			// 성공 형식으로 파싱
			DataResponseDto dataDto = tryParse(jsonContent, DataResponseDto.class);
			if (dataDto != null) {
				log.info("GPT 응답 파싱 성공");
				return GptParsedResponse.ofSuccess(dataDto);
			}

			log.error("GPT 응답을 JSON으로 파싱하는 데 실패했습니다. 내용: {}", jsonContent);
			return GptParsedResponse.ofError("응답을 처리할 수 없는 형식입니다.");

		} catch (Exception e) {
			log.error("JSON 파싱 중 예외 발생: {}", e.getMessage(), e);
			return GptParsedResponse.ofError("JSON 파싱 중 오류가 발생했습니다.");
		}
	}

	/**
	 * JSON 문자열을 주어진 클래스 타입으로 파싱 시도. 실패 시 null 반환.
	 */
	private <T> T tryParse(String json, Class<T> clazz) {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (IOException e) {
			// 파싱 실패는 오류가 아닌, 다른 DTO 타입일 가능성을 의미하므로 로그 레벨을 debug로 조정할 수 있습니다.
			log.debug("{} 파싱 실패: {}", clazz.getSimpleName(), e.getMessage());
			return null;
		}
	}
}
