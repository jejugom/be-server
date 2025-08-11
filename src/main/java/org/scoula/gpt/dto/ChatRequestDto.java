package org.scoula.gpt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 클라이언트에서 서버로 보내는 요청을 담는 DTO 입니다.
 * 사용자의 질문(question)을 필드로 가집니다.
 */
@Data
@AllArgsConstructor
public class ChatRequestDto {
	private String question;
}
