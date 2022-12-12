package edu.epam.bookshop.validator.book;

import edu.epam.bookshop.annotation.ValidateBookPrice;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

import static java.util.Objects.nonNull;

public class BookPriceValidator implements ConstraintValidator<ValidateBookPrice, String> {

    @Override
    public boolean isValid(String bookPriceAsString, ConstraintValidatorContext constraintValidatorContext) {
        if (bookPriceAsString.isEmpty()) {
            return false;
        }
        double bookPriceAsDouble = Double.parseDouble(bookPriceAsString);
        BigDecimal bookPrice = BigDecimal.valueOf(bookPriceAsDouble);
        return nonNull(bookPrice) && bookPrice.signum() == 1;
    }
}
