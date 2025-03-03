package com.emamagic.institutemanagement.validation;

import com.emamagic.institutemanagement.feature.user.Gender;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GenderValidator implements ConstraintValidator<ValidGender, String> {

    @Override
    public boolean isValid(String param, ConstraintValidatorContext constraintValidatorContext) {
        if (param == null || param.isEmpty()) {
            return true;
        }
        String input = param.toUpperCase();
        return input.equals(Gender.FEMALE.name()) || input.equals(Gender.MALE.name());
    }

}
