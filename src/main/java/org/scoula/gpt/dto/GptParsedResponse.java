package org.scoula.gpt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // null 필드는 JSON 변환 시 제외
public class GptParsedResponse {
	private boolean success;
	private DataResponseDto data;
	private String errorMessage;

	// 성공 응답을 위한 정적 팩토리 메소드
	public static GptParsedResponse ofSuccess(DataResponseDto data) {
		GptParsedResponse response = new GptParsedResponse();
		response.setSuccess(true);
		response.setData(data);
		return response;
	}

	// 오류 응답을 위한 정적 팩토리 메소드
	public static GptParsedResponse ofError(String errorMessage) {
		GptParsedResponse response = new GptParsedResponse();
		response.setSuccess(false);
		response.setErrorMessage(errorMessage);
		return response;
	}
}