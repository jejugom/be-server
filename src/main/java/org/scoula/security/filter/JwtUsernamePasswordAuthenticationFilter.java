package org.scoula.security.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.scoula.user.dto.LoginDTO;
import org.scoula.security.handler.LoginFaillureHandler;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class JwtUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	//스프링 생성자 주입을 통해 전달
	public JwtUsernamePasswordAuthenticationFilter(
		AuthenticationManager authenticationManager,
		LoginFaillureHandler loginFaillureHandler
	) {
		super(authenticationManager);
		setFilterProcessesUrl("/api/auth/login"); //POST 로그인 요청 url
		setAuthenticationFailureHandler(loginFaillureHandler);//로그인 실패 핸들러 등록
	}

	//로그인 요청 URL인 경우 로그인 작업 처리
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
	throws AuthenticationException {
		//요청 BOdy의 JSON에서 username,password -> LoginDTO
		LoginDTO login = LoginDTO.of(request);

		//인증 토큰 (UsernamePasswordAuthentication)구성
		UsernamePasswordAuthenticationToken authenticationToken =
			new UsernamePasswordAuthenticationToken(login.getEmail(),login.getPassword());


		//AuthenticationManager에게 인증 요청
		return getAuthenticationManager().authenticate(authenticationToken);
	}
}
