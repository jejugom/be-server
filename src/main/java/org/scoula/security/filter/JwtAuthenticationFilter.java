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

		log.debug("JwtAuthenticationFilter running for URI: {}", request.getRequestURI());

		// 1. 요청 헤더에서 토큰을 추출합니다.
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

		// 2. 토큰이 존재하고, 'Bearer '로 시작하며, 유효한 경우에만 인증 객체를 생성하여 SecurityContext에 저장합니다.
		if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
			String token = bearerToken.substring(BEARER_PREFIX.length());

			// 2-1. 토큰 유효성 검증
			if (jwtProcessor.validateToken(token)) {
				// 2-2. 유효한 토큰이면 인증 정보를 생성하여 SecurityContextHolder에 저장
				Authentication authentication = getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
				log.debug("Authentication successful for user: {}", authentication.getName());
			} else {
				log.warn("Invalid JWT token received.");
			}
		} else {
			log.debug("Authorization header is missing or does not start with Bearer.");
		}

		// 3. 토큰 유무나 유효성과 관계없이 항상 다음 필터로 요청을 전달합니다.
		//    접근 허용 여부는 SecurityConfig의 설정에 따라 Spring Security가 최종적으로 결정합니다.
		filterChain.doFilter(request, response);
	}
}
