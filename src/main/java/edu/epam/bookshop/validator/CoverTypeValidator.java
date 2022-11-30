package edu.epam.bookshop.validator;

import edu.epam.bookshop.entity.CoverType;
import org.apache.commons.lang3.EnumUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CoverTypeValidator implements ConstraintValidator<ValidateCoverType, CoverType> {

    @Override
    public boolean isValid(CoverType coverType, ConstraintValidatorContext constraintValidatorContext) {
        for (CoverType existingCoverType : CoverType.values()) {
            if (existingCoverType.name().equals(coverType.name())) {
                return true;
            }
        }
        return false;
    }
}
