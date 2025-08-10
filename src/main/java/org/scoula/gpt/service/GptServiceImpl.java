package org.scoula.gpt.service;

import org.scoula.gpt.dto.ChatRequestDto;
import org.scoula.gpt.dto.ChatResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

/**
 * GptService 인터페이스의 최종 구현체입니다.
 * Config 클래스 없이, 서비스 내에서 직접 OpenAIClient를 생성합니다.
 */
@Service
public class GptServiceImpl implements GptService {

	private static final Logger log = LoggerFactory.getLogger(GptServiceImpl.class);

	// 서비스 내에서 직접 생성하고 관리하는 OpenAIClient
	private final OpenAIClient openAiClient;

	// application-dev.properties에서 주입받는 시스템 프롬프트
	private final String systemPrompt;

	/**
	 * 생성자에서 직접 API 키와 프롬프트를 주입받아 OpenAIClient를 초기화합니다.
	 * @param apiKey application-dev.properties에 정의된 openai.api.key 값
	 * @param systemPrompt application-dev.properties에 정의된 gpt.system.prompt 값
	 */
	public GptServiceImpl(@Value("${openai.api.key}") String apiKey,
		@Value("${gpt.system.prompt}") String systemPrompt) {
		// 1. 주입받은 API 키로 OpenAIClient를 직접 생성합니다.
		this.openAiClient = OpenAIOkHttpClient.builder()
			.apiKey(apiKey)
			.build();
		this.systemPrompt = systemPrompt;
		log.info("GptServiceImpl 초기화 완료: OpenAIClient 생성");
	}

	/**
	 * 사용자의 질문 DTO를 받아 GPT 모델의 답변 DTO를 반환합니다.
	 * @param chatRequestDto 사용자의 질문이 담긴 DTO
	 * @return GPT 모델의 답변이 담긴 DTO
	 */
	@Override
	public ChatResponseDto getChatResponse(ChatRequestDto chatRequestDto) {
		log.info("GPT 요청 시작: {}", chatRequestDto.getQuestion());

		try {
			ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
				.model(ChatModel.GPT_4O_MINI_2024_07_18)
				.addSystemMessage(this.systemPrompt)
				.addUserMessage(chatRequestDto.getQuestion() + "json")
				.maxCompletionTokens(1024)
				.temperature(0.3)
				.build();

			ChatCompletion completion = openAiClient.chat().completions().create(params);
			log.info("GPT API 호출 성공: {}", completion);

			if (completion.choices().isEmpty()) {
				log.warn("응답 choices가 비어있음");
				return new ChatResponseDto("GPT 응답 없음");
			}

			String content = completion.choices().get(0).message().content().orElse("");
			log.info("GPT 응답 내용: {}", content);

			return new ChatResponseDto(content);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("GPT 서비스 처리 중 에러 발생: {}", e.getMessage(), e);
			throw e;
		}
	}

}
