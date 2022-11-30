package edu.epam.bookshop.annotation;

import edu.epam.bookshop.validator.book.BookTitleValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static edu.epam.bookshop.constant.InvalidInputMessage.INVALID_BOOK_TITLE_INPUT;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = BookTitleValidator.class)
public @interface ValidateBookTitle {

    String message() default INVALID_BOOK_TITLE_INPUT;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
