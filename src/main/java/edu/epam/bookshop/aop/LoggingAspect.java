package edu.epam.bookshop.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import static edu.epam.bookshop.constant.LogMessage.FOUND_TOP_15_BOOKS_WHERE_SCORE_GREATER_THAN;
import static edu.epam.bookshop.constant.LogMessage.TRYING_TO_FIND_TOP_15_BOOKS_WHERE_SCORE_GREATER_THAN;

@Component
@Aspect
@Slf4j
public class LoggingAspect {


    @Before("execution(public java.util.List findTop15BooksHavingAverageScoreGreaterThan(Double))")
    public void beforeFindTop15BooksHavingAverageScoreGreaterThanAdvice(JoinPoint joinPoint) {
        Double givenScore = (Double) joinPoint.getArgs()[0];
        log.info(String.format(TRYING_TO_FIND_TOP_15_BOOKS_WHERE_SCORE_GREATER_THAN, givenScore));
    }

    @AfterReturning("execution(public java.util.List findTop15BooksHavingAverageScoreGreaterThan(Double))")
    public void afterFindTop15BooksHavingAverageScoreGreaterThanAdvice(JoinPoint joinPoint) {
        Double givenScore = (Double) joinPoint.getArgs()[0];
        log.info(String.format(FOUND_TOP_15_BOOKS_WHERE_SCORE_GREATER_THAN, givenScore));
    }

    @AfterThrowing(
            pointcut = "execution(public java.util.List findTop15BooksHavingAverageScoreGreaterThan(Double))",
            throwing = "nothingFoundException")
    public void afterThrowingNothingFoundExceptionInFindTop15BooksAdvice(Throwable nothingFoundException) {
        String nothingFoundExceptionMessage = nothingFoundException.getMessage();
        log.info(nothingFoundExceptionMessage);
    }
}
