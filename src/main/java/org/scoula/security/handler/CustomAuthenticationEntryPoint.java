package org.scoula.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.scoula.security.util.JsonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component//이 class도 Compnent -> 빈등록됨 -> Security에 생성자 주입 가능
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {
		log.error("======인증 에러======");
		JsonResponse.sendError(response, HttpStatus.UNAUTHORIZED,authException.getMessage());
	}
	/**
	 * 클라이언트는 위 메세지를 받으면 loginPage로 이동해야 함.
	 * login 성공을 하면,ㅡ 원래 가고자 하는 페이지로 이동하는 작업을
	 * 클라이언트에서 해줘야 함.
	 */

}
