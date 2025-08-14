package org.scoula.common.aop;

import java.util.Arrays;

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

	// org.scoula 패키지 하위의 모든 Service 클래스에 적용
	@Around("execution(* org.scoula..*Service.*(..))")
	public Object logServiceMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {

		long startTime = System.currentTimeMillis();

		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		String className = signature.getDeclaringType().getSimpleName();
		String methodName = signature.getName();
		String params = Arrays.toString(joinPoint.getArgs());

		log.info("  ▶ [BIZ] {}.{}({})", className, methodName, params);

		try {
			Object result = joinPoint.proceed();

			long endTime = System.currentTimeMillis();
			long executionTime = endTime - startTime;

			log.info("  ◀ [BIZ] {}.{} | RETURN: {} | EXECUTION_TIME: {}ms", className, methodName, result,
				executionTime);

			return result;
		} catch (Throwable e) {
			long endTime = System.currentTimeMillis();
			long executionTime = endTime - startTime;
			log.error("  ❌ [BIZ] {}.{} | EXCEPTION_TYPE: {} | EXECUTION_TIME: {}ms", className, methodName,
				e.getClass().getSimpleName(), executionTime, e);
			throw e;
		}
	}
}