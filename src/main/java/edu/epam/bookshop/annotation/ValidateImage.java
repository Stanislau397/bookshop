package edu.epam.bookshop.annotation;

import edu.epam.bookshop.validator.ImageValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static edu.epam.bookshop.constant.InvalidInputMessage.INVALID_IMAGE_INPUT;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageValidator.class)
public @interface ValidateImage {

    String message() default INVALID_IMAGE_INPUT;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
