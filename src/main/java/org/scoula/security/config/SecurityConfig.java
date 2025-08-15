package org.scoula.security.config;

import org.scoula.security.filter.JwtAuthenticationFilter;
import org.scoula.security.handler.CustomAccessDeniedHandler;
import org.scoula.security.handler.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.CorsFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * 애플리케이션의 보안 설정을 담당하는 클래스. JWT 기반의 인증/인가, CORS, 예외 처리 등을 설정합니다.
 */
@Configuration
@EnableWebSecurity
@Log4j2
@ComponentScan(basePackages = {"org.scoula.security", "org.scoula.user.service"})
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final CustomAccessDeniedHandler accessDeniedHandler;
	private final CustomAuthenticationEntryPoint authenticationEntryPoint;

	/**
	 * HTTP 요청에 대한 보안 설정을 구성합니다.
	 *
	 * @param http HttpSecurity 객체
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		// 필터 순서 설정: 인코딩 필터 -> JWT 인증 필터 -> Spring Security 기본 필터
		http.addFilterBefore(encodingFilter(), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		// stateless REST API 설정을 위해 기본 보안 기능 비활성화
		http
			.httpBasic().disable() // HTTP Basic 인증 비활성화
			.csrf().disable()      // CSRF 보호 비활성화
			.formLogin().disable() // 폼 로그인 비활성화
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 세션을 사용하지 않음

		// 인증/인가 과정에서 발생하는 예외 처리를 위한 핸들러 설정
		http
			.exceptionHandling()
			.authenticationEntryPoint(authenticationEntryPoint) // 인증 실패 시 처리
			.accessDeniedHandler(accessDeniedHandler);         // 인가 실패 시 처리

		// 각 엔드포인트에 대한 접근 권한 설정
		http
			.authorizeRequests()
			// Swagger UI 및 정적 리소스에 대한 접근 허용
			.antMatchers(
				"/assets/**",
				"/swagger-ui.html",
				"/webjars/**",
				"/swagger-resources/**",
				"/v2/api-docs"
			).permitAll()
			// GET 요청 중 인증 없이 허용할 경로들
			.antMatchers(HttpMethod.GET,
				"/",
				"/favicon.ico", // favicon.ico 요청도 permitAll로 처리
				"/api/home",
				"/auth/kakao",
				"/auth/kakao/callback",
				"/api/retirement",
				"/api/news"
			).permitAll()
			// 💡 POST 요청 중 인증 없이 허용할 경로들
			.antMatchers(HttpMethod.POST,
				"/api/user/join",
				"/auth/kakao",
				"/auth/refresh", "/api/news/crawl"
			).permitAll()
			// OPTIONS 요청은 모두 허용 (CORS Preflight 요청 처리)
			.antMatchers(HttpMethod.OPTIONS).permitAll()
			// 나머지 모든 요청은 인증 필요
			.anyRequest().authenticated();
	}

	/**
	 * 정적 리소스 등 보안 필터를 거치지 않을 경로를 설정합니다.
	 *
	 * @param web WebSecurity 객체
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		// 💡 web.ignoring() 사용 대신 HttpSecurity의 permitAll()을 사용하는 것이 권장되므로
		// 이 메소드의 내용은 비워두거나 메소드 자체를 삭제합니다. (수정된 부분)
	}

	/**
	 * 요청/응답의 문자 인코딩을 UTF-8로 설정하는 필터를 생성합니다.
	 *
	 * @return CharacterEncodingFilter 객체
	 */
	public CharacterEncodingFilter encodingFilter() {
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("UTF-8");
		encodingFilter.setForceEncoding(true);
		return encodingFilter;
	}

	/**
	 * CORS(Cross-Origin Resource Sharing) 설정을 위한 필터를 Bean으로 등록합니다.
	 *
	 * @return CorsFilter 객체
	 */
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);       // 자격 증명(쿠키 등) 허용
		config.addAllowedOriginPattern("*");    // 모든 출처 허용
		config.addAllowedHeader("*");           // 모든 헤더 허용
		config.addAllowedMethod("*");           // 모든 HTTP 메소드 허용
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
}