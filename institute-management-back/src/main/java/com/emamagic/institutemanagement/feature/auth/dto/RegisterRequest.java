package com.emamagic.institutemanagement.feature.auth.dto;

import com.emamagic.institutemanagement.validation.ValidUserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Email(message = "invalid email")
        String email,
        @NotBlank(message = "password can not be empty")
        @Size(min = 3, max = 12, message = "password could be in range of 3 to 12")
        String password,
        @NotBlank(message = "role can not be empty")
        @ValidUserRole
        String role
) {
}
