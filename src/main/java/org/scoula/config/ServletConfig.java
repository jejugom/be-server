package org.scoula.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@ComponentScan(basePackages = {"org.scoula.controller",
	"org.scoula.exception",
	"org.scoula.security",
	"org.scoula.auth.service",
	"org.scoula.user.controller",
	"org.scoula.asset.controller",
	"org.scoula.recommend.controller",
	"org.scoula.faq.controller",
	"org.scoula.branch.controller",
	"org.scoula.booking.controller",
	"org.scoula.product.controller",
	"org.scoula.codef.controller",
	"org.scoula.auth.controller",
}) //SPRING MVC용 컴포넌트 등록을 위한 스 캔 패키지
public class ServletConfig implements WebMvcConfigurer {
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/")
			.setViewName("forward:/resources/index.html");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
			.addResourceHandler("/resources/**") //url이 /resources/로 시작하는 모든 경로
			.addResourceLocations("/resources/"); //webapp/resource/경로로 매핑

		registry.addResourceHandler("/assets/**")
			.addResourceLocations("/resources/assets/");
		// Swagger UI 리소스를 위한 핸들러 설정
		registry.addResourceHandler("/swagger-ui.html")
			.addResourceLocations("classpath:/META-INF/resources/");

		// Swagger WebJar 리소스 설정 (Bootstrap, jQuery 등)
		registry.addResourceHandler("/webjars/**")
			.addResourceLocations("classpath:/META-INF/resources/webjars/");

		// Swagger 메타데이터 리소스 설정
		registry.addResourceHandler("/swagger-resources/**")
			.addResourceLocations("classpath:/META-INF/resources/");

		// API 문서 JSON 엔드포인트 설정
		registry.addResourceHandler("/v2/api-docs")
			.addResourceLocations("classpath:/META-INF/resources/");
	}
}
