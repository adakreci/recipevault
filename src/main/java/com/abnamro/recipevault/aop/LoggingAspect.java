package com.abnamro.recipevault.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * for logging execution of service and repository spring components
 * the implementations are in the advices
 */
@Aspect
public class LoggingAspect {

    /**
     * pointcut that matches all repositories, services REST endpoints
     */
    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() { }

    /**
     * pointcut that matches all spring beans in the application's main packages
     */
    @Pointcut("within(com.abnamro.recipevault.repository..*)" +
            " || within(com.abnamro.recipevault.service..*)" +
            " || within(com.abnamro.recipevault.controller..*)"
    )
    public void applicationPackagePointcut() { }

    private Logger logger( JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
    }

    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        logger(joinPoint)
                .error(
                        "exception in {}() with cause = '{}' and exception = '{}'",
                        joinPoint.getSignature().getName(),
                        e.getCause() != null ? e.getCause() : "NULL",
                        e.getMessage(),
                        e
                );
    }

    @Around("applicationPackagePointcut() && springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = logger(joinPoint);
        log.debug("Enter: {}() with argument[s] = {}", joinPoint.getSignature().getName(), joinPoint.getArgs());
        Object result = joinPoint.proceed();
        log.debug("Exit: {}() with result = {}", joinPoint.getSignature().getName(), result);
        return result;
    }

}
