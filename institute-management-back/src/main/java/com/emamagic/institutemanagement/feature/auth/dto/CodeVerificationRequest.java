package com.emamagic.institutemanagement.feature.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CodeVerificationRequest(
        @Email(message = "invalid email")
        String email,
        @NotBlank(message = "verification code could not be empty")
        @Size(min = 4, max = 4, message = "verification code must be exactly 4 digits")
        String code
) {
}
