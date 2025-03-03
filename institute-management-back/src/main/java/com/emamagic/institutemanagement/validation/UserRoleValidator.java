package com.emamagic.institutemanagement.validation;

import com.emamagic.institutemanagement.feature.user.role.UserRoleService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRoleValidator implements ConstraintValidator<ValidUserRole, String> {
    private final UserRoleService roleSvc;

    @Override
    public boolean isValid(String param, ConstraintValidatorContext constraintValidatorContext) {
        if (param == null || param.isEmpty()) {
            return true;
        }

        String normalizedParam = param.toUpperCase();
        return roleSvc.findAll().stream()
                .map(String::toUpperCase)
                .anyMatch(name -> name.equals(normalizedParam));

    }
}
