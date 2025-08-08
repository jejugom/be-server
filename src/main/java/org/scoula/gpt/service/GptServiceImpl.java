package org.scoula.gpt.service;

import java.time.Duration;
import java.util.List;

import org.scoula.gpt.dto.ChatRequestDto;
import org.scoula.gpt.dto.ChatResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

/**
 * GptService 인터페이스의 구현체입니다.
 * 실제 OpenAI API와 통신하는 로직을 담당합니다.
 */
@Service
public class GptServiceImpl implements GptService {

	private final OpenAiService openAiService;

	/**
	 * 생성자 주입을 통해 OpenAiService를 초기화합니다.
	 * @param apiKey application.properties에 설정된 OpenAI API 키
	 */
	public GptServiceImpl(@Value("${openai.api.key}") String apiKey) {
		// API 요청 시 타임아웃을 60초로 설정하여 무한 대기를 방지합니다.
		this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(60));
	}

	@Override
	public ChatResponseDto getChatResponse(ChatRequestDto chatRequestDto) {
		try {
			// 1. GPT에게 보낼 메시지를 생성합니다.
			//    - role: "user"는 사용자가 보낸 메시지임을 의미합니다.
			final List<ChatMessage> messages = List.of(
				new ChatMessage("user", chatRequestDto.getQuestion())
			);

			// 2. OpenAI API에 보낼 요청 객체를 생성합니다.
			ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
				.model("gpt-4o-mini") // 사용할 AI 모델을 지정합니다.
				.messages(messages)
				.maxTokens(1024) // 응답의 최대 길이를 설정합니다.
				.temperature(0.7) // 답변의 창의성, 0에 가까울수록 결정론적
				.build();

			// 3. API를 호출하고 응답을 받습니다.
			//    getChoices().get(0)은 여러 답변 후보 중 첫 번째 것을 선택함을 의미합니다.
			String content = openAiService.createChatCompletion(completionRequest)
				.getChoices().get(0).getMessage().getContent();

			// 4. 받은 답변을 DTO에 담아 반환합니다.
			return new ChatResponseDto(content);

		} catch (Exception e) {
			// API 호출 중 예외 발생 시 처리
			e.printStackTrace();
			return new ChatResponseDto("죄송합니다. AI 서비스에 문제가 발생했습니다.");
		}
	}
}
