package edu.epam.bookshop.exception.handler;

import edu.epam.bookshop.exception.ApiException;
import edu.epam.bookshop.exception.NothingFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class NothingFoundExceptionHandler {

    @ExceptionHandler(value = {NothingFoundException.class})
    public ResponseEntity<Object> handleInvalidInputException(NothingFoundException nothingFoundException) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                nothingFoundException.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, badRequest);
    }
}
