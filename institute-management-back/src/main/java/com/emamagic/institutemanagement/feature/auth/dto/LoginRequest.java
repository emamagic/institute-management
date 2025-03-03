package com.emamagic.institutemanagement.feature.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "email can not be empty")
        @Email(message = "invalid email")
        String email,
        @NotBlank(message = "password can not be empty")
        @Size(min = 3, max = 12, message = "password could be in range of 3 to 12")
        String password
) {
}
