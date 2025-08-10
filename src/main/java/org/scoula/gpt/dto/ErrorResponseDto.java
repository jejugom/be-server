package org.scoula.gpt.dto;

import lombok.Data;

@Data
public class ErrorResponseDto {
	private boolean error;
	private String message;
}