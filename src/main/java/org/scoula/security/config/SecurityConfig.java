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
	public void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(encodingFilter(), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		http
			.httpBasic().disable()
			.csrf().disable()
			.formLogin().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http
			.exceptionHandling()
			.authenticationEntryPoint(authenticationEntryPoint)
			.accessDeniedHandler(accessDeniedHandler);

		http
			.authorizeRequests()
			// 💡 GET 요청 중 인증 없이 허용할 경로들
			.antMatchers(HttpMethod.GET,
				"/",
				"/favicon.ico",
				"/api/home",
				"/auth/kakao",
				"/auth/kakao/callback",
				"/api/retirement",
				"/api/sms/test" // SMS API 경로  임시 추가 - 로그인 구현 후 제거
			).permitAll()
			.antMatchers(HttpMethod.POST,
				"/api/user/join",
				"/auth/kakao",
				"/auth/refresh",
				"/api/sms/send" // SMS API 경로  임시 추가 - 로그인 구현 후 제거
			).permitAll()
			.antMatchers(HttpMethod.OPTIONS).permitAll()
			.anyRequest().authenticated();
	}


	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/assets/**",
			"/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs"
		);
	}

	public CharacterEncodingFilter encodingFilter() {
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("UTF-8");
		encodingFilter.setForceEncoding(true);
		return encodingFilter;
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOriginPattern("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
}
