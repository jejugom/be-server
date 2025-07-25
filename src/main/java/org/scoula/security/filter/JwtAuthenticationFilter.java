package org.scoula.security.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.scoula.security.util.JwtProcessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Component
// @Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String BEARER_PREFIX = "Bearer "; // 끝에 공백 포함

	private final JwtProcessor jwtProcessor;

	private Authentication getAuthentication(String token) {
		// JWT에서 email 추출
		String email = jwtProcessor.getUsername(token);

		// 권한 정보 없이 인증 객체 생성 (필요시 ROLE_USER 등 추가 가능)
		return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		List<String> whitelist = List.of(
			"/auth/kakao",
			"/api/user/join",
			"/favicon.ico",
			"/oauth/authorize",
			"/auth/kakao/callback",
			"/auth/refresh",
			"/api/home"
		);

		String uri = request.getRequestURI();
		log.info("JwtAuthenticationFilter: doFilterInternal called for URI: {}", uri);

		if (whitelist.contains(uri)) {
			filterChain.doFilter(request, response);
			return;
		}

		log.info("JwtAuthenticationFilter: doFilterInternal called for URI: {}", request.getRequestURI());

		// 1. 헤더에서 Bearer 토큰을 가져오기
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

		// 2. 토큰이 존재하고, 유효한 경우에만 인증(Authentication) 객체를 생성하여 SecurityContext에 저장
		if (bearerToken == null) {
			log.warn("JwtAuthenticationFilter: Authorization header is missing."); // 헤더 누락 로그
			log.warn("Authorization header missing.");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write("{\"error\": \"Authorization Header가 누락되었습니다.\"}");
			return;
		} else if (!bearerToken.startsWith(BEARER_PREFIX)) {
			log.warn("JwtAuthenticationFilter: Authorization header does not start with Bearer. Header: {}",
				bearerToken); // Bearer 접두사 누락 로그
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write("{\"error\": \"Authorization-Header는 Bearer 로 시작해야 합니다.\"}");
			return;
		} else {
			String token = bearerToken.substring(BEARER_PREFIX.length());
			log.info("JwtAuthenticationFilter: Extracted token: {}", token); // 토큰 추출 로그

			if (jwtProcessor.validateToken(token)) {
				Authentication authentication = getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
				log.info("JwtAuthenticationFilter: Authentication successful for user: {}",
					authentication.getName()); // 인증 성공 로그
			} else {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.setContentType("application/json;charset=UTF-8");
				response.getWriter().write("{\"error\": \"유효하지 않은 토큰입니다.\"}");
				return;
			}
		}

		// 동일한 2번 코드 (디버깅 설명 제거 버전)
		// if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
		// 	String token = bearerToken.substring(BEARER_PREFIX.length());
		//
		// 	if (jwtProcessor.validateToken(token)) {
		// 		Authentication authentication = getAuthentication(token);
		// 		SecurityContextHolder.getContext().setAuthentication(authentication);
		// 	}
		// }

		// 3. 다음 필터로 요청을 전달합니다. 토큰이 없거나 유효하지 않아도 일단 통과시킵니다.
		//    최종 접근 허용 여부는 SecurityConfig에서 결정합니다.
		filterChain.doFilter(request, response);

	}
}
