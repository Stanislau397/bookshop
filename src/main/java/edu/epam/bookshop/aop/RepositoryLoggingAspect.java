package edu.epam.bookshop.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import static edu.epam.bookshop.aop.LogMessage.REPOSITORY_METHOD_EXECUTION;
import static edu.epam.bookshop.aop.LogMessage.REPOSITORY_METHOD_FINISHED_EXECUTION;

@Component
@Aspect
@Slf4j
public class RepositoryLoggingAspect {

//    @Pointcut("within(edu.epam.bookshop.repository..*)")
//    public void anyRepositoryMethod() {}
//
//    @Before("anyRepositoryMethod()")
//    public void beforeAnyRepositoryMethod(JoinPoint repositoryJoinPoint) {
//        String repositoryMethodName = repositoryJoinPoint.getSignature()
//                .getName();
//        log.info(REPOSITORY_METHOD_EXECUTION, repositoryMethodName);
//    }
//
//    @After("anyRepositoryMethod()")
//    public void afterAnyRepositoryMethod(JoinPoint repositoryJoinPoint) {
//        String repositoryMethodName = repositoryJoinPoint.getSignature()
//                .getName();
//        log.info(REPOSITORY_METHOD_FINISHED_EXECUTION, repositoryMethodName);
//    }
}
