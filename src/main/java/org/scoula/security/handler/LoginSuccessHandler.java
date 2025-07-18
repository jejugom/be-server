package org.scoula.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.scoula.user.domain.CustomUser;
import org.scoula.user.dto.AuthResultDTO;
import org.scoula.user.dto.UserInfoDTO;
import org.scoula.security.util.JsonResponse;
import org.scoula.security.util.JwtProcessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

	private final JwtProcessor jwtProcessor;

	private AuthResultDTO makeAuthResult(CustomUser user){
		String email = user.getUsername();
		//토큰 생성
		String token = jwtProcessor.generateToken(email);
		//토큰 + 사용자 기본 정보 ( 사용자명, ... ) 를 묶어서 AuthResultDTO 구성
		return new AuthResultDTO(token, UserInfoDTO.of(user.getUser()));
	}



	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		//인증 결과 Principal
		CustomUser user = (CustomUser) authentication.getPrincipal();

		//인증 성공 결과를 JSON으로 직접 응답
		AuthResultDTO result = makeAuthResult(user);
		JsonResponse.send(response,result);
	}
}