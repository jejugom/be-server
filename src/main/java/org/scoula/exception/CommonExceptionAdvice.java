package org.scoula.exception;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
@Order(1)
public class CommonExceptionAdvice {
	// @ExceptionHandler(Exception.class)
	// public String except(Exception ex, Model model){
	//
	// 	log.error("Exception ...." + ex.getMessage());
	// 	model.addAttribute("exception",ex);
	// 	log.error(model);
	// 	return "error_page";
	// }
	@ExceptionHandler(NoHandlerFoundException.class)
	public String handle404(NoHandlerFoundException ex) {
		return "/resources/index.html";
		//절대 경로로 지정하는 이유
		// backend 만 사용 ->
		// -> servlet Config에 suffix/ prefix 처리 하는 부분이 사라져서.

	}
}
