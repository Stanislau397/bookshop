package edu.epam.bookshop.aop;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import static edu.epam.bookshop.aop.LogMessage.CONTROLLER_METHOD_FINISHED_EXECUTION;
import static edu.epam.bookshop.aop.LogMessage.CONTROLLER_METHOD_EXECUTION;

@Component
@Aspect
@Slf4j
@AllArgsConstructor
public class ControllerLoggingAspect {

    @Pointcut("within(edu.epam.bookshop.controller..*)")
    public void anyControllerMethod() {
    }

    @Before("anyControllerMethod()")
    public void beforeAnyControllerMethod(JoinPoint joinPoint) {
        log.info(CONTROLLER_METHOD_EXECUTION, joinPoint.getSignature().getName());
    }

    @After("anyControllerMethod()")
    public void afterAnyControllerMethod(JoinPoint joinPoint) {
        log.info(CONTROLLER_METHOD_FINISHED_EXECUTION, joinPoint.getSignature().getName());
    }
}
