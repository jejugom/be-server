package org.scoula.config;

import javax.servlet.Filter;
import javax.servlet.ServletRegistration;

import org.scoula.security.config.SecurityConfig;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected void customizeRegistration(ServletRegistration.Dynamic registration) {
		registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");
		// 중복된 라인 제거
	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] {RootConfig.class, SecurityConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] {ServletConfig.class, SwaggerConfig.class};
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] {
			"/",
			"/swagger-ui.html",
			"/swagger-resources/**",
			"/v2/api-docs",
			"/webjars/**"
		};
	}

	@Override
	protected Filter[] getServletFilters() {
		// 1. 기존 문자 인코딩 필터
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("UTF-8");
		encodingFilter.setForceEncoding(true);

		// 2. ⭐ CORS 설정을 위한 필터 추가 ⭐
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();

		// 자격 증명(쿠키 등)을 허용
		config.setAllowCredentials(true);
		// 프론트엔드 서버 주소 허용
		config.addAllowedOrigin("http://localhost:5173");
		// 모든 헤더 허용
		config.addAllowedHeader("*");
		// 모든 HTTP 메서드 허용
		config.addAllowedMethod("*");

		// 모든 경로에 대해 위 CORS 설정을 적용
		source.registerCorsConfiguration("/**", config);
		CorsFilter corsFilter = new CorsFilter(source);

		// 인코딩 필터와 CORS 필터를 함께 반환
		return new Filter[] {encodingFilter, corsFilter};
	}
}