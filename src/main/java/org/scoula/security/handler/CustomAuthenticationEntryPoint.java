package org.scoula.security.handler;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException {

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
		response.setContentType("application/json;charset=UTF-8");

		Map<String, String> errorResponse = Map.of(
			"error", "인증이 필요합니다.",
			"message", authException.getMessage()
		);

		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
}
