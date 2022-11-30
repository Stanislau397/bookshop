package edu.epam.bookshop.validator.book;

import edu.epam.bookshop.annotation.ValidateBookIsbn;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class BookIsbnValidator implements ConstraintValidator<ValidateBookIsbn, String> {

    private static final Pattern ISBN_PATTERN =
            Pattern.compile("^(?:ISBN(?:-13)?:?\\ )?" +
                    "(?=[0-9]{13}$|(?=(?:[0-9]+[-\\ ]){4})" +
                    "[-\\ 0-9]{17}$)97[89][-\\ ]?[0-9]{1,5}" +
                    "[-\\ ]?[0-9]+[-\\ ]?[0-9]+[-\\ ]?[0-9]$");

    @Override
    public boolean isValid(String bookIsbn, ConstraintValidatorContext constraintValidatorContext) {
        return bookIsbn.matches(ISBN_PATTERN.pattern());
    }
}
