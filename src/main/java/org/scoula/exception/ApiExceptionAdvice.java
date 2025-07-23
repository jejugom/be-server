package org.scoula.exception;

import java.util.NoSuchElementException;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(2)
public class ApiExceptionAdvice {

	// 404 Error
	@ExceptionHandler(NoSuchElementException.class)
	protected ResponseEntity<String> handleIllegalArgumentException(NoSuchElementException error) {
		return ResponseEntity
			.status(HttpStatus.NOT_FOUND)
			.header("Content-Type", "text/plain;charset=UTF-8")
			.body("해당 ID의 요소가 없습니다.");
	}

	// 500 Error
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<String> handleException(Exception error) {
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.header("Content-Type", "text/plain;charset=UTF-8")
			.body(error.getMessage());
	}

}
