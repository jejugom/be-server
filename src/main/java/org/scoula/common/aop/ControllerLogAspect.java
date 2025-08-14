package org.scoula.common.aop;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class ControllerLogAspect {

	// org.scoula 패키지 하위의 모든 Controller 클래스에 적용
	@Around("execution(* org.scoula..*Controller.*(..))")
	public Object logControllerMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {

		// --- 요청 정보 가져오기 ---
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String httpMethod = request.getMethod();
		String requestURI = request.getRequestURI();
		String params = getRequestParams(request); // GET, POST 파라미터를 문자열로 변환

		// --- 메소드 정보 가져오기 ---
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		String className = signature.getDeclaringType().getSimpleName();
		String methodName = signature.getName();

		long startTime = System.currentTimeMillis();

		log.info("▶▶▶ [REQUEST] {} {} | {}.{} | PARAMS: {}", httpMethod, requestURI, className, methodName, params);

		try {
			// 대상 메소드 실행
			Object result = joinPoint.proceed();

			long endTime = System.currentTimeMillis();
			long executionTime = endTime - startTime;

			log.info("◀◀◀ [RESPONSE] {} {} | RETURN: {} | DURATION: {}ms", httpMethod, requestURI, result,
				executionTime);

			return result;
		} catch (Throwable e) {
			long endTime = System.currentTimeMillis();
			long executionTime = endTime - startTime;
			log.error("❌❌❌ [EXCEPTION] {} {} | EXCEPTION_TYPE: {} | DURATION: {}ms", httpMethod, requestURI,
				e.getClass().getSimpleName(), executionTime, e);
			throw e;
		}
	}

	// HttpServletRequest의 파라미터를 문자열로 변환하는 헬퍼 메소드
	private String getRequestParams(HttpServletRequest request) {
		Map<String, String[]> paramMap = request.getParameterMap();
		if (paramMap.isEmpty()) {
			return "None";
		}
		return paramMap.entrySet().stream()
			.map(entry -> String.format("%s=%s", entry.getKey(), Arrays.toString(entry.getValue())))
			.collect(Collectors.joining(", "));
	}
}