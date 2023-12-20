package com.github.whatasame.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AopAspect {

    private final AopLogger logger;

    public AopAspect(final AopLogger logger) {
        this.logger = logger;
    }

    @Before("execution(* com.github.whatasame.aop..*.doBefore())")
    public void before() {
        logger.log("before");
    }

    @Around("execution(void com.github.whatasame.aop.AopComponent.doAround(..))")
    public void around(final ProceedingJoinPoint joinPoint) throws Throwable {
        logger.log("around before");
        joinPoint.proceed();
        logger.log("around after");
    }

    @After("execution(public * com.github.whatasame.aop.AopComponent.doAfter())")
    public void after() {
        logger.log("after");
    }
}
