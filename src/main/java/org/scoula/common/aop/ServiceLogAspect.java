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
		String className = signature.getDeclaringType().getSimpleName();
		String methodName = signature.getName();
		String params = Arrays.toString(joinPoint.getArgs());

		// --- 도메인명 추출 ---
		String[] packageParts = fullClassName.split("\\.");
		String domainName = packageParts.length >= 3 ? packageParts[2] : "unknown";

		// --- ThreadContext에 도메인 세팅 (RoutingAppender에서 참조) ---
		ThreadContext.put("domain", domainName);

		long startTime = System.currentTimeMillis();

		log.info("▶▶▶ [SERVICE] {}.{} | PARAMS: {}", className, methodName, params);

		try {
			Object result = joinPoint.proceed();

			long executionTime = System.currentTimeMillis() - startTime;

			log.info("◀◀◀ [SERVICE] {}.{} | RETURN: {} | DURATION: {}ms", className, methodName, result,
				executionTime);

			return result;

		} catch (Throwable e) {
			long executionTime = System.currentTimeMillis() - startTime;
			log.error("❌❌❌ [SERVICE] {}.{} | EXCEPTION_TYPE: {} | DURATION: {}ms", className, methodName,
				e.getClass().getSimpleName(), executionTime, e);
			throw e;

		} finally {
			ThreadContext.clearMap(); // 다른 요청에 영향 안 가도록 ThreadContext 초기화
		}
	}
}
