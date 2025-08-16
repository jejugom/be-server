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
			// ... 기존 antMatchers 설정은 동일 ...
			.antMatchers(
				"/assets/**", "/swagger-ui.html", "/webjars/**",
				"/swagger-resources/**", "/v2/api-docs"
			).permitAll()
			.antMatchers(HttpMethod.GET,
				"/", "/favicon.ico", "/api/home", "/auth/kakao",
				"/auth/kakao/callback", "/api/retirement", "/api/news"
			).permitAll()
			.antMatchers(HttpMethod.POST,
				"/api/user/join", "/auth/kakao", "/auth/refresh", "/api/news/crawl"
			).permitAll()
			.antMatchers(HttpMethod.OPTIONS).permitAll()
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
