package org.scoula.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.scoula.security.util.JwtProcessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private static final String AUTHORIZATION_HEADER = "Authorization";
	public static String BEARER_PREFIX = "Bearer "; //끝에 공백

	private final JwtProcessor jwtProcessor;
	private final UserDetailsService userDetailsService;
	private Authentication getAuthentication(String token){
		//jwt에서 email 가져오기
		String email = jwtProcessor.getUsername(token);
		//db 에서 email 가져오기
		UserDetails principle = userDetailsService.loadUserByUsername(email);
		return new UsernamePasswordAuthenticationToken(principle,null,principle.getAuthorities());
	}
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	throws ServletException, IOException{
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);//
		//Beaerer Token 이 null 이면 로그인 X 인 상테

		if(bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)){
			String token = bearerToken.substring(BEARER_PREFIX.length());

			//토큰에서 사용자 정보 추출 및 Authentication 객체 구성 후 SecurityContext에 저장
			Authentication authentication = getAuthentication(token);

			//Authentication 의 SecurityContext를 만들어야 함.
			//SucurityContextHolder의 static method로 authentication을 설정

			SecurityContextHolder.getContext().setAuthentication(authentication);
			// -> 필터의 목적
		}
		super.doFilter(request,response,filterChain);

	}
}