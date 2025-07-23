package org.scoula.config;

import javax.servlet.Filter;
import javax.servlet.ServletRegistration;

import org.scoula.security.config.SecurityConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected void customizeRegistration(ServletRegistration.Dynamic registration) {
		registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");
		registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");
	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] {RootConfig.class, SecurityConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] {ServletConfig.class, SwaggerConfig.class};
	}

	//스프링의 FrontController 인 DisplatcherServlet 이 담당할 url 매핑 패턴,
	@Override
	protected String[] getServletMappings() {
		return new String[] {"/"};
	}

	// Post body 문자 인코딩 필터 설정
	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("UTF-8");
		encodingFilter.setForceEncoding(true);
		return new Filter[] { encodingFilter };
	}

}
