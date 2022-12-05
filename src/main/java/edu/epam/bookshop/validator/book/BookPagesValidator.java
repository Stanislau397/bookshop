package edu.epam.bookshop.validator.book;

import edu.epam.bookshop.annotation.ValidateBookPages;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.util.Objects.nonNull;

public class BookPagesValidator implements ConstraintValidator<ValidateBookPages, Integer> {

    private static final Integer MIN_AMOUNT_OF_PAGES = 24;
    private static final Integer MAX_AMOUNT_OF_PAGES = 1500;

    @Override
    public boolean isValid(Integer pages, ConstraintValidatorContext constraintValidatorContext) {
        return nonNull(pages) && pages >= MIN_AMOUNT_OF_PAGES
                && pages <= MAX_AMOUNT_OF_PAGES;
    }
}
