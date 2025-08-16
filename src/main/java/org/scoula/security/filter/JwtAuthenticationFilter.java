package org.scoula.security.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.scoula.security.util.JwtProcessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProcessor jwtProcessor;

	private Authentication getAuthentication(String token) {
		String email = jwtProcessor.getUsername(token);
		return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		log.debug("JwtAuthenticationFilter running for URI: {}", request.getRequestURI());

		String token = resolveTokenFromCookie(request);

		// ✅ try-catch 블록으로 토큰 검증 로직을 감쌉니다.
		try {
			if (token != null && jwtProcessor.validateToken(token)) {
				Authentication authentication = getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
				log.debug("Authentication successful for user: {}", authentication.getName());
			}
		} catch (JwtException e) {
			// ✅ 토큰이 만료되었거나, 서명이 유효하지 않거나, 형식이 잘못된 경우 등
			// 모든 JWT 관련 예외는 여기서 처리됩니다.
			// 사용자를 인증하지 않고 조용히 넘어가는 것이 목적이므로, 로그만 남깁니다.
			log.warn("Invalid JWT Token: {}", e.getMessage());
		}

		// 다음 필터로 요청과 응답을 전달합니다.
		filterChain.doFilter(request, response);
	}

	private String resolveTokenFromCookie(HttpServletRequest request) {
		final Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}
		return Arrays.stream(cookies)
			.filter(cookie -> "accessToken".equals(cookie.getName()))
			.findFirst()
			.map(Cookie::getValue)
			.orElse(null);
	}
}
