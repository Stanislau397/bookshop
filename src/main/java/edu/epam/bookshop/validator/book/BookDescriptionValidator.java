package edu.epam.bookshop.validator.book;

import edu.epam.bookshop.annotation.ValidateBookDescription;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BookDescriptionValidator implements ConstraintValidator<ValidateBookDescription, String> {

    private static final Integer MIN_DESCRIPTION_LENGTH = 100;
    private static final Integer MAX_DESCRIPTION_LENGTH = 2000;

    @Override
    public boolean isValid(String bookDescription, ConstraintValidatorContext constraintValidatorContext) {
        return bookDescription.length() > MIN_DESCRIPTION_LENGTH
                && bookDescription.length() < MAX_DESCRIPTION_LENGTH;
    }
}
