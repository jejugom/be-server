package org.scoula.common.aop;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.ThreadContext;
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

	@Around("execution(* org.scoula..*Controller.*(..))")
	public Object logControllerMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {

		// --- 로깅에 필요한 모든 정보를 try 블록 실행 전에 미리 확보 ---
		long startTime = System.currentTimeMillis();
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes())
			.getRequest();

		String fullClassName = joinPoint.getTarget().getClass().getName();
		String domainName = extractDomainName(fullClassName);
		String httpMethod = request.getMethod();
		String requestUri = request.getRequestURI();
		String params = getRequestParams(request);
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		String className = signature.getDeclaringType().getSimpleName();
		String methodName = signature.getName();

		ThreadContext.put("domain", domainName);
		log.info("▶▶▶ [REQUEST] {} {} | {}.{} | PARAMS: {}", httpMethod, requestUri, className, methodName, params);

		try {
			// 대상 메소드 실행
			Object result = joinPoint.proceed();

			long endTime = System.currentTimeMillis();
			long executionTime = endTime - startTime;

			log.info("◀◀◀ [RESPONSE] {} {} | RETURN: {} | DURATION: {}ms", httpMethod, requestUri, result,
				executionTime);
			return result;

		} catch (Throwable e) {
			// --- startTime을 포함한 모든 변수를 여기서도 안전하게 사용 가능 ---
			long endTime = System.currentTimeMillis();
			long executionTime = endTime - startTime;

			log.error("❌❌❌ [EXCEPTION] {} {} | EXCEPTION_TYPE: {} | DURATION: {}ms",
				httpMethod, requestUri, e.getClass().getSimpleName(), executionTime, e);
			throw e;
		} finally {
			ThreadContext.clearMap();
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

	// 클래스 이름에서 도메인 이름을 추출하는 헬퍼 메소드
	private String extractDomainName(String fullClassName) {
		// 예: "org.scoula.booking.controller.BookingController"
		try {
			String[] parts = fullClassName.split("\\."); // "." 기준으로 분리
			if (parts.length > 2) {
				return parts[2]; // "booking" 반환
			}
		} catch (Exception e) {
			// 파싱 실패 시 기본값 반환
		}
		return "unknown";
	}
}
