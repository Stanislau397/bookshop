package edu.epam.bookshop.annotation;

import edu.epam.bookshop.validator.book.BookPagesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static edu.epam.bookshop.constant.InvalidInputMessage.INVALID_BOOK_PAGES_INPUT;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = BookPagesValidator.class)
public @interface ValidateBookPages {

    String message() default INVALID_BOOK_PAGES_INPUT;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
