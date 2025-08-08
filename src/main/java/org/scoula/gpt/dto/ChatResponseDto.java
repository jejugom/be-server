package org.scoula.gpt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 서버가 AI의 답변을 담아 클라이언트로 보내는 응답 DTO 입니다.
 * AI의 답변(answer)을 필드로 가집니다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponseDto {
	private String answer;
}
