package org.scoula.security.config;

import javax.servlet.ServletContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.mybatis.spring.annotation.MapperScan;
import org.scoula.security.filter.AuthenticationErrorFilter;
import org.scoula.security.filter.JwtAuthenticationFilter;
import org.scoula.security.filter.JwtUsernamePasswordAuthenticationFilter;
import org.scoula.security.handler.CustomAccessDeniedHandler;
import org.scoula.security.handler.CustomAuthenticationEntryPoint;
import org.scoula.security.util.JwtProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.multipart.support.MultipartFilter;

@Configuration
@EnableWebSecurity
@Log4j2
@MapperScan(basePackages = {
	"org.scoula.user.mapper"}) // user.mapper로 변경
@ComponentScan(basePackages = {"org.scoula.security"})
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final UserDetailsService userDetailsService;
	//Password 암호화 , 회원가입 암호화 등에 사용됨
	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	//final -> 생성자 주입
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	//얘느 final 붙이면 error 남
	private final AuthenticationErrorFilter authenticationErrorFilter;
	private final CustomAccessDeniedHandler accessDeniedHandler;
	private final CustomAuthenticationEntryPoint authenticationEntryPoint;
	@Autowired
	private JwtUsernamePasswordAuthenticationFilter jwtUsernamePasswordAuthenticationFilter;
	//접근 제한 무시 경로 설정 - resource

	@Override
	public void configure(HttpSecurity http) throws Exception{
		//한글인코딩 필터 설정
		http.addFilterBefore(encodingFilter(),CsrfFilter.class)
			//인증 에러 필터
			.addFilterBefore(authenticationErrorFilter,UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class)
			//로그인 인증 필터
			.addFilterBefore(jwtUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		http.httpBasic().disable()//기본 HTTP 인증 비활성화
			.csrf().disable()//CSRF 비활성화
			.formLogin().disable() //FormLogin 비활성화
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//세션생성모드 설정

		http
			.exceptionHandling() // 예외처리 단계
			.authenticationEntryPoint(authenticationEntryPoint)
			.accessDeniedHandler(accessDeniedHandler);

		http
			.authorizeRequests()
			.antMatchers(HttpMethod.OPTIONS).permitAll()
			.antMatchers(HttpMethod.POST,"/api/user/join").permitAll() // 회원가입은 인증 없이 허용
			.antMatchers(HttpMethod.POST,"/api/user/login").permitAll() // 로그인도 인증 없이 허용
			.antMatchers("/api/user/**").authenticated() // 나머지 user 관련 API는 인증 필요
			.antMatchers("/api/categories/**").authenticated()
			.antMatchers("/api/asset-details/**").authenticated()
			.antMatchers("/api/asset-info/**").authenticated()
			.antMatchers("/api/custom-recommends/**").authenticated()
			.antMatchers("/api/faqs/**").authenticated()
			.antMatchers("/api/banks/**").authenticated()
			.antMatchers("/api/bookings/**").authenticated()
			.anyRequest().permitAll();

	}
	//Authentication Manager 구성
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth
			.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder());
	}
	// 접근 제한 무시 경로 설정 - resource
	// Vue 연동 시 중요
	@Override
	public void configure(WebSecurity web) throws Exception{
		web.ignoring().antMatchers("/assets/**","/*",
			//Swagger 관련 url은 보안에서 제오ㅣ
			"/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs"
			);
	}
	// 문자셋 필터
	public CharacterEncodingFilter encodingFilter() {
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("UTF-8");
		encodingFilter.setForceEncoding(true);
		return encodingFilter;
	}
	//Authentication Manager 빈 등록
	//Config 생성 이후에 준비됨 ( || : <  ))
	@Bean
	public AuthenticationManager authenticationManager() throws Exception{
		return super.authenticationManager();
		//A M 은 로그인 성공 시 응답/ 실패 시 응답 을 담당하는 .
	}
	//Cross Origin 접근 허용
	@Bean
	public CorsFilter corsFilter(){
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOriginPattern("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**",config);
		return new CorsFilter(source);
	}
}