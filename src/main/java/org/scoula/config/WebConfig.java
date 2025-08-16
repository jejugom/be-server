package org.scoula.config;

import javax.servlet.Filter;
import javax.servlet.ServletRegistration;

import org.scoula.security.config.SecurityConfig;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

@EnableAspectJAutoProxy(proxyTargetClass = true)
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
		// 1. 기존 문자 인코딩 필터만 남깁니다.
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("UTF-8");
		encodingFilter.setForceEncoding(true);

		// ❌ CORS 관련 코드는 모두 삭제합니다.

		return new Filter[] {encodingFilter};
	}
}