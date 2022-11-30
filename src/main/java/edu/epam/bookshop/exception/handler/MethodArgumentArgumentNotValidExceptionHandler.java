package edu.epam.bookshop.exception.handler;

import edu.epam.bookshop.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class MethodArgumentArgumentNotValidExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        String errorMessage = exception.getBindingResult()
                .getFieldError()
                .getDefaultMessage();
        ApiException apiException = new ApiException(
                errorMessage,
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, badRequest);
    }
}
