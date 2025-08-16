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

		// MDC / ThreadContext 설정
		ThreadContext.put("domain", domainName);
		ThreadContext.put("params", params);

		log.info("▶▶▶ [REQUEST] {} {} | {}.{} 호출 시작", httpMethod, requestUri, className, methodName);

		try {
			Object result = joinPoint.proceed();
			long executionTime = System.currentTimeMillis() - startTime;

			// duration, return 별도 MDC 필드에 기록
			ThreadContext.put("duration", String.valueOf(executionTime));
			ThreadContext.put("return", String.valueOf(result));

			log.info("◀◀◀ [RESPONSE] {} {} | {}.{} 호출 완료", httpMethod, requestUri, className, methodName);

			return result;

		} catch (Throwable e) {
			long executionTime = System.currentTimeMillis() - startTime;

			ThreadContext.put("duration", String.valueOf(executionTime));
			ThreadContext.put("exceptionType", e.getClass().getSimpleName());

			log.error("XXX [EXCEPTION] {} {} | {}.{} 호출 중 예외 발생", httpMethod, requestUri, className, methodName, e);
			throw e;

		} finally {
			ThreadContext.clearMap();
		}
	}

	private String getRequestParams(HttpServletRequest request) {
		Map<String, String[]> paramMap = request.getParameterMap();
		if (paramMap.isEmpty()) {
			return "None";
		}
		return paramMap.entrySet().stream()
			.map(entry -> String.format("%s=%s", entry.getKey(), Arrays.toString(entry.getValue())))
			.collect(Collectors.joining(", "));
	}

	private String extractDomainName(String fullClassName) {
		try {
			// "org.scoula." 이후 부분만 가져오기
			String basePackage = "org.scoula.";
			if (fullClassName.startsWith(basePackage)) {
				String remainder = fullClassName.substring(basePackage.length());
				// 패키지를 . 기준으로 쪼개기
				String[] parts = remainder.split("\\.");
				for (String part : parts) {
					// Controller 또는 Service 직전의 패키지를 domain으로 잡음
					if (!part.toLowerCase().contains("controller") && !part.toLowerCase().contains("service")) {
						return part;
					}
				}
			}
		} catch (Exception e) {
			// 실패 시 기본값
		}
		return "unknown";
	}
}
