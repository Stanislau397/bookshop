package edu.epam.bookshop.validator.book;

import edu.epam.bookshop.annotation.ValidateBookPrice;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

import static java.util.Objects.nonNull;

public class BookPriceValidator implements ConstraintValidator<ValidateBookPrice, BigDecimal> {

    @Override
    public boolean isValid(BigDecimal bookPrice, ConstraintValidatorContext constraintValidatorContext) {
        return nonNull(bookPrice) && bookPrice.signum() == 1;
    }
}
