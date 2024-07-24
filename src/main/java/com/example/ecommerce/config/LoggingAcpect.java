package com.example.ecommerce.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAcpect {
    private final static Logger logger = LoggerFactory.getLogger(LoggingAcpect.class);

    @Before("execution(* com.example.ecommerce.services.impl.CategoryServiceImpl.*(..))")
    public void logBeforeMethod() {
        logger.info("A method is about to be executed");
    }

    @AfterReturning(pointcut = "execution(* com.example.ecommerce.services.impl.CategoryServiceImpl.*(..))", returning = "result")
    public void logAfterMethod(Object result) {
        logger.info("A method has successfully executed with result: " + result);
    }

    @AfterThrowing(pointcut = "execution(* com.example.ecommerce.services.impl.CategoryServiceImpl.*(..))", throwing = "error")
    public void logAfterThrowingMethod(Throwable error) {
        logger.error("An exception occurred: " + error);
    }

    @Around("execution(* com.example.ecommerce.services.impl.CategoryServiceImpl.*(..))")
    public Object logAroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("Method " + joinPoint.getSignature().getName() + " is about to be executed");
        Object result;
        try {
            result = joinPoint.proceed();
            logger.info("Method " + joinPoint.getSignature().getName() + " executed successfully with result: " + result);
        } catch (Throwable throwable) {
            logger.error("Method " + joinPoint.getSignature().getName() + " threw an exception: " + throwable);
            throw throwable;
        }
        return result;
    }

}
