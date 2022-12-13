package edu.epam.bookshop.validator;

import edu.epam.bookshop.annotation.ValidateCoverType;
import edu.epam.bookshop.entity.CoverType;
import org.apache.commons.lang3.EnumUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CoverTypeValidator implements ConstraintValidator<ValidateCoverType, String> {

    @Override
    public boolean isValid(String coverType, ConstraintValidatorContext constraintValidatorContext) {
        return EnumUtils.isValidEnum(CoverType.class, coverType.toUpperCase());
    }
}
