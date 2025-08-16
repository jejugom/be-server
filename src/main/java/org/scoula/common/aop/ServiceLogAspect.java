package org.scoula.common.aop;

import java.util.Arrays;

import org.apache.logging.log4j.ThreadContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class ServiceLogAspect {

	@Around("execution(* org.scoula..*Service.*(..))")
	public Object logServiceMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {

		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		String fullClassName = signature.getDeclaringTypeName();
		String domainName = extractDomainName(fullClassName);
		String className = signature.getDeclaringType().getSimpleName();
		String methodName = signature.getName();
		String params = Arrays.toString(joinPoint.getArgs());

		ThreadContext.put("domain", domainName);
		ThreadContext.put("params", params);

		long startTime = System.currentTimeMillis();

		log.info("▶▶▶ [SERVICE] {}.{} 호출 시작", className, methodName);

		try {
			Object result = joinPoint.proceed();
			long executionTime = System.currentTimeMillis() - startTime;

			ThreadContext.put("duration", String.valueOf(executionTime));
			ThreadContext.put("return", String.valueOf(result));

			log.info("◀◀◀ [SERVICE] {}.{} 호출 완료", className, methodName);
			return result;

		} catch (Throwable e) {
			long executionTime = System.currentTimeMillis() - startTime;

			ThreadContext.put("duration", String.valueOf(executionTime));
			ThreadContext.put("exceptionType", e.getClass().getSimpleName());

			log.error("XXX [SERVICE] {}.{} 호출 중 예외 발생", className, methodName, e);
			throw e;

		} finally {
			ThreadContext.clearMap();
		}
	}

	private String extractDomainName(String fullClassName) {
		try {
			String basePackage = "org.scoula.";
			if (fullClassName.startsWith(basePackage)) {
				String remainder = fullClassName.substring(basePackage.length());
				String[] parts = remainder.split("\\.");
				for (String part : parts) {
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
