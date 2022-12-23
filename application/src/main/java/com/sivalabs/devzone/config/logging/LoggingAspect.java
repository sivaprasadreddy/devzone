package com.sivalabs.devzone.config.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut(
            "within(@org.springframework.stereotype.Repository *)"
                    + " || within(@org.springframework.stereotype.Service *)"
                    + " || within(@org.springframework.stereotype.Controller *)"
                    + " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
        // pointcut definition
    }

    @Pointcut(
            "@within(com.sivalabs.devzone.config.logging.Loggable) || "
                    + "@annotation(com.sivalabs.devzone.config.logging.Loggable)")
    public void applicationPackagePointcut() {
        // pointcut definition
    }

    @AfterThrowing(pointcut = "applicationPackagePointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error(
                "Exception in {}.{}() with cause = '{}'",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                e.getCause() == null ? "NULL" : e.getCause(),
                e);
    }

    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (log.isTraceEnabled()) {
            log.trace(
                    "Enter: {}.{}()",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());
        }
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        if (log.isTraceEnabled()) {
            log.trace(
                    "Exit: {}.{}(). Time taken: {} millis",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    end - start);
        }
        return result;
    }
}
