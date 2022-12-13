package edu.epam.bookshop.annotation;

import edu.epam.bookshop.validator.CoverTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static edu.epam.bookshop.constant.InvalidInputMessage.INVALID_COVER_TYPE_INPUT;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CoverTypeValidator.class)
public @interface ValidateCoverType {

    String message() default INVALID_COVER_TYPE_INPUT;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
