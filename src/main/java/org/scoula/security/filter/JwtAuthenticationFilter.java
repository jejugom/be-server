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

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * HTTP 요청에서 쿠키를 통해 JWT를 감지하고, 유효한 경우 사용자를 인증하여
 * Spring Security의 SecurityContext에 인증 정보를 설정하는 필터입니다.
 * 모든 요청에 대해 한 번씩 실행됩니다.
 */
@Log4j2
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	/** JWT 토큰의 유효성 검사 및 정보 추출을 담당하는 컴포넌트 */
	private final JwtProcessor jwtProcessor;

	/**
	 * 유효한 JWT 토큰으로부터 Authentication 객체를 생성합니다.
	 * @param token 유효성이 검증된 JWT 문자열
	 * @return 생성된 Authentication 객체
	 */
	private Authentication getAuthentication(String token) {
		// JWT에서 사용자 식별자(email)를 추출합니다.
		String email = jwtProcessor.getUsername(token);

		// UsernamePasswordAuthenticationToken을 사용하여 인증 객체를 생성합니다.
		// 여기서는 별도의 권한(Role)을 설정하지 않으므로 비어있는 리스트를 전달합니다.
		return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
	}

	/**
	 * 모든 요청을 가로채 JWT 인증을 처리하는 핵심 메소드입니다.
	 * @param request  들어오는 HTTP 요청
	 * @param response 나가는 HTTP 응답
	 * @param filterChain 다음 필터로 요청을 전달하기 위한 필터 체인
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		log.debug("JwtAuthenticationFilter running for URI: {}", request.getRequestURI());

		// 1. 요청의 쿠키에서 'accessToken'을 추출합니다.
		String token = resolveTokenFromCookie(request);

		// 2. 토큰이 존재하고 유효한지 확인합니다.
		if (token != null && jwtProcessor.validateToken(token)) {
			// 2-1. 토큰이 유효하면, Authentication 객체를 생성하여 SecurityContextHolder에 저장합니다.
			// 이로써 해당 요청은 인증된 것으로 간주됩니다.
			Authentication authentication = getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			log.debug("Authentication successful for user: {}", authentication.getName());
		} else {
			log.debug("No valid JWT token found in cookie.");
		}

		// 3. 다음 필터로 요청과 응답을 전달합니다.
		// 토큰이 없거나 유효하지 않더라도 필터 체인은 계속 진행되어야 합니다.
		// 최종적인 접근 허용 여부는 SecurityConfig의 authorizeRequests 설정에 따라 결정됩니다.
		filterChain.doFilter(request, response);
	}

	/**
	 * HttpServletRequest의 쿠키에서 'accessToken'을 추출하는 새로운 메소드입니다.
	 * @param request HttpServletRequest 객체
	 * @return 추출된 accessToken 문자열 또는 null
	 */
	private String resolveTokenFromCookie(HttpServletRequest request) {
		// 요청에 쿠키가 없으면 null을 반환합니다.
		final Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}

		// 쿠키 배열을 스트림으로 변환하여 'accessToken'이라는 이름의 쿠키를 찾습니다.
		return Arrays.stream(cookies)
			.filter(cookie -> "accessToken".equals(cookie.getName()))
			.findFirst() // 첫 번째로 일치하는 쿠키를 찾습니다.
			.map(Cookie::getValue) // 쿠키가 존재하면 그 값을 가져옵니다.
			.orElse(null); // 존재하지 않으면 null을 반환합니다.
	}
}
