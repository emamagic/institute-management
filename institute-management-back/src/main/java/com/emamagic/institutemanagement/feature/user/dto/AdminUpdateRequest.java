package com.emamagic.institutemanagement.feature.user.dto;

import com.emamagic.institutemanagement.validation.ValidUserRole;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;

public @AllArgsConstructor
@Getter
class AdminUpdateRequest extends UserUpdateRequest {
    private Boolean isApproved;
    @Email
    private String email;
    @ValidUserRole
    private String role;
}
