package edu.epam.bookshop.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import edu.epam.bookshop.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class InvalidFormatExceptionHandler {

    @ExceptionHandler(value = {InvalidFormatException.class})
    public ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException exception) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(
                exception.getMessage(),
                notFound,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, notFound);
    }
}
