package org.scoula.security.config;

import java.util.Arrays;
import java.util.Collections;

import org.scoula.security.filter.JwtAuthenticationFilter;
import org.scoula.security.handler.CustomAccessDeniedHandler;
import org.scoula.security.handler.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * 애플리케이션의 보안 설정을 담당하는 클래스. (Spring Framework 환경)
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

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			// 1. CORS 설정을 HttpSecurity에 직접 적용합니다. (가장 중요)
			.cors().configurationSource(corsConfigurationSource())
			.and()
			.csrf().disable()
			.httpBasic().disable()
			.formLogin().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		http
			.exceptionHandling()
			.authenticationEntryPoint(authenticationEntryPoint)
			.accessDeniedHandler(accessDeniedHandler);

		http
			.authorizeRequests()
			// --- 1. 인증 없이 항상 접근 가능한 경로 ---
			.antMatchers(
				// 기본 경로
				"/",
				"/favicon.ico",

				// Swagger UI 관련 경로
				"/swagger-ui.html",
				"/swagger-resources/**",
				"/v2/api-docs",
				"/webjars/**",

				// 인증 관련 전체 경로 (카카오 로그인, 토큰 재발급 등)
				"/auth/**",

				// 회원가입 경로
				"/api/user/join",

				// 비로그인 사용자도 볼 수 있는 컨텐츠 API
				"/api/home",
				"/api/news/**"
			).permitAll()

			// --- 2. CORS Pre-flight 요청은 항상 허용 ---
			.antMatchers(HttpMethod.OPTIONS).permitAll()

			// --- 3. 위에서 정의한 경로 외 모든 요청은 반드시 인증 필요 ---
			.anyRequest().authenticated();
	}

	/**
	 * CORS 설정을 정의하는 Bean.
	 * HttpSecurity의 .cors() 설정에 의해 사용됩니다.
	 */
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowCredentials(true);

		// Arrays.asList()를 사용하여 여러 개의 출처를 등록
		configuration.setAllowedOriginPatterns(Arrays.asList(
			"http://localhost:5173",
			"https://nohoodorak-fe.vercel.app"
		));

		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Collections.singletonList("*"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
