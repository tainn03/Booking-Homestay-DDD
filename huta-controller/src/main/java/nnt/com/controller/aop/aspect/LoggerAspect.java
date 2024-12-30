package nnt.com.controller.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Aspect
@Component
public class LoggerAspect {
    @Around("execution(* nnt.com.controller.http..*.*(..))")
    public Object logAroundApiController(ProceedingJoinPoint joinPoint) throws Throwable {
        Instant start = Instant.now();
        Object result = joinPoint.proceed();
        Instant end = Instant.now();
        long time = end.toEpochMilli() - start.toEpochMilli();
        log.info("FINISH EXECUTION METHOD {} RUN IN {} ms", joinPoint.getSignature().getName(), time);
        return result;
    }

    @AfterThrowing(pointcut = "execution(* nnt.com.controller..*.*(..))", throwing = "ex")
    public void logAfterThrowingApiController(Exception ex) {
        log.error("FAIL METHOD {} DUE TO EXCEPTION: {}", ex.getStackTrace()[0].getMethodName(), ex.getMessage());
    }
}
