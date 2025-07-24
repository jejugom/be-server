package org.scoula.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.log4j.Log4j2;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
	//카카오 등 외부 요청 실패
	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<ErrorResponse> handleHttpClientError(HttpClientErrorException error) {
		log.error("Http client Error: {}", error.getMessage());
		if (error.getStatusCode() == HttpStatus.BAD_REQUEST && error.getResponseBodyAsString().contains("KOE320")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new ErrorResponse("유효하지 않은 코드입니다.", error.getResponseBodyAsString()));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ErrorResponse("외부 서버 통신 장애", error.getResponseBodyAsString()));
	}

	//잘못된 파라미터 타입
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException error) {
		log.error("파라미터 타입 불일치: {}", error.getMessage());
		return ResponseEntity.badRequest()
			.body(new ErrorResponse("잘못된 요청 파라미터", error.getMessage()));
	}

	//NPE, 일반 예외
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException error) {
		log.error("NullPointerException: {}", error.getMessage(), error);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ErrorResponse("서버 내부 오류", "필수 정보가 누락됐거나 잘못되었습니다."));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException error) {
		return ResponseEntity.badRequest()
			.body(new ErrorResponse("잘못된 요청", error.getMessage()));
	}

	//나머지 예외 통합 처리
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneral(Exception error) {
		log.error("X 알 수 없는 예외 발생: {}", error.getMessage(), error);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ErrorResponse("서버 내부 오류", error.getMessage()));
	}
}

