package org.scoula.security.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.scoula.security.handler.CustomAuthenticationEntryPoint;
import org.scoula.security.util.JwtProcessor;
import org.springframework.security.authentication.InsufficientAuthenticationException;
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
	// ✅ 1. 중앙 예외 처리를 담당할 AuthenticationEntryPoint를 주입받습니다.
	private final CustomAuthenticationEntryPoint authenticationEntryPoint;

	private Authentication getAuthentication(String token) {
		String email = jwtProcessor.getUsername(token);
		return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		String token = resolveTokenFromCookie(request);

		// ✅ 2. try-catch 구문을 사용하여 토큰 검증 과정에서 발생하는 예외를 처리합니다.
		try {
			if (token != null && jwtProcessor.validateToken(token)) {
				Authentication authentication = getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
				log.debug("Authentication successful for user: {}", authentication.getName());
			}
			// 토큰이 유효하거나, 아예 없는 경우에는 다음 필터로 정상적으로 진행합니다.
			filterChain.doFilter(request, response);

		} catch (JwtException e) {
			// ✅ 3. 토큰이 만료되었거나 유효하지 않을 때 발생하는 모든 JwtException을 여기서 잡습니다.
			log.warn("Invalid JWT Token detected: {}", e.getMessage());
			// 예외를 숨기지 않고, 즉시 중앙 예외 처리 전문가(AuthenticationEntryPoint)에게 처리를 위임합니다.
			// 이 `commence` 메서드가 클라이언트에게 정확한 401 Unauthorized 응답을 보내줄 것입니다.
			authenticationEntryPoint.commence(request, response,
				new InsufficientAuthenticationException("Invalid JWT Token", e));
		}
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
