package edu.epam.bookshop.validator.book;

import edu.epam.bookshop.annotation.ValidateBookTitle;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class BookTitleValidator implements ConstraintValidator<ValidateBookTitle, String> {

    private static final Pattern BOOK_TITLE_PATTERN =
            Pattern.compile("^[\\p{L}\\s\\-_,\\.:;()''\"\"]+$");

    @Override
    public boolean isValid(String bookTitle, ConstraintValidatorContext constraintValidatorContext) {
        return bookTitle.matches(BOOK_TITLE_PATTERN.pattern());
    }
}
