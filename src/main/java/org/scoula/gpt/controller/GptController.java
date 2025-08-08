package org.scoula.gpt.controller;

import org.scoula.gpt.dto.ChatRequestDto;
import org.scoula.gpt.dto.ChatResponseDto;
import org.scoula.gpt.service.GptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * GPT 관련 API 요청을 처리하는 컨트롤러입니다.
 */
@RestController
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 만들어줍니다.
@RequestMapping("/api/gpt") // 이 컨트롤러의 모든 API는 /api/gpt 경로 하위에 위치합니다.
public class GptController {

	private final GptService gptService;

	/**
	 * 클라이언트로부터 채팅 요청을 받아 AI의 답변을 반환하는 API 엔드포인트입니다.
	 * @param chatRequestDto 사용자의 질문이 담긴 요청 본문 (JSON 형태)
	 * @return AI의 답변이 담긴 ResponseEntity
	 */
	@PostMapping("/chat")
	public ResponseEntity<ChatResponseDto> chat(@RequestBody ChatRequestDto chatRequestDto) {
		// 서비스 레이어에 요청을 전달하고 응답을 받습니다.
		ChatResponseDto chatResponseDto = gptService.getChatResponse(chatRequestDto);
		// HTTP 200 OK 상태 코드와 함께 응답 본문을 반환합니다.
		return ResponseEntity.ok(chatResponseDto);
	}
}
