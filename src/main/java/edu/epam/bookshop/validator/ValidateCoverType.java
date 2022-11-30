package edu.epam.bookshop.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CoverTypeValidator.class)
public @interface ValidateCoverType {

    String message() default "fuck you";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
