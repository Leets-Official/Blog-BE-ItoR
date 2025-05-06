package com.blog.global.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(* com.blog..*Service.*(..))")
    public void logPointcut() {}

    @Before("logPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("📌 BEFORE: " + joinPoint.getSignature().toShortString());
        System.out.println("📦 Arguments: " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "logPointcut()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        System.out.println("✅ AFTER RETURN: " + joinPoint.getSignature().toShortString());
        System.out.println("🎁 Returned: " + result);
    }

    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        System.out.println("❌ EXCEPTION in: " + joinPoint.getSignature().toShortString());
        System.out.println("⚠️ Exception: " + e.getMessage());
    }
}
