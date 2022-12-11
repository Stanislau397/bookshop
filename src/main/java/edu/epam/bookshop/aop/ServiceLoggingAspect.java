package edu.epam.bookshop.aop;

import edu.epam.bookshop.exception.EntityNotFoundException;
import edu.epam.bookshop.exception.NothingFoundException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import static edu.epam.bookshop.aop.LogMessage.SERVICE_METHOD_EXECUTION;
import static edu.epam.bookshop.aop.LogMessage.SERVICE_METHOD_FINISHED_EXECUTION;
import static edu.epam.bookshop.aop.LogMessage.SERVICE_METHOD_THREW_EXCEPTION;

@Component
@Aspect
@Slf4j
public class ServiceLoggingAspect {

//    @Pointcut("within(edu.epam.bookshop.service..*)")
//    public void anyServiceMethod() {
//
//    }
//
//    @Before("anyServiceMethod()")
//    public void beforeFindLocalizedBooksByKeywordAndLanguageNameLimit6(JoinPoint serviceJoinPoint) {
//        String methodName = serviceJoinPoint.getSignature()
//                .getName();
//        log.info(SERVICE_METHOD_EXECUTION, methodName);
//    }
//
//    @After("anyServiceMethod()")
//    public void afterFindLocalizedBooksByKeywordAndLanguageNameLimit6(JoinPoint serviceJoinPoint) {
//        String methodName = serviceJoinPoint.getSignature()
//                .getName();
//        log.info(SERVICE_METHOD_FINISHED_EXECUTION, methodName);
//    }
//
//    @AfterThrowing(value = "anyServiceMethod()",
//            throwing = "nothingFoundException")
//    public void afterThrowingNothingFoundException(JoinPoint serviceJoinPoint,
//                                                   NothingFoundException nothingFoundException) {
//        String methodName = serviceJoinPoint.getSignature()
//                .getName();
//        String nothingFoundExceptionMessage = nothingFoundException.getMessage();
//        log.info(SERVICE_METHOD_THREW_EXCEPTION, methodName, nothingFoundExceptionMessage);
//    }
//
//    @AfterThrowing(value = "anyServiceMethod()",
//            throwing = "entityNotFoundException")
//    public void afterThrowingEntityNotFoundException(JoinPoint serviceJoinPoint,
//                                                     EntityNotFoundException entityNotFoundException) {
//        String methodName = serviceJoinPoint.getSignature()
//                .getName();
//        String nothingFoundExceptionMessage = entityNotFoundException.getMessage();
//        log.info(SERVICE_METHOD_THREW_EXCEPTION, methodName, nothingFoundExceptionMessage);
//    }
}
