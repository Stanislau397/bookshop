package edu.epam.bookshop.annotation;

import edu.epam.bookshop.validator.book.BookDescriptionValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static edu.epam.bookshop.constant.InvalidInputMessage.INVALID_BOOK_DESCRIPTION_INPUT;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = BookDescriptionValidator.class)
public @interface ValidateBookDescription {

    String message() default INVALID_BOOK_DESCRIPTION_INPUT;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
