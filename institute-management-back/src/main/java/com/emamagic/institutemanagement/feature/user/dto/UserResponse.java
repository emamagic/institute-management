package com.emamagic.institutemanagement.feature.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

public @JsonInclude(JsonInclude.Include.NON_EMPTY)
record UserResponse(
        String id,
        String name,
        String email,
        String age,
        String gender,
        String role,
        boolean isVerified,
        boolean isApproved,
        LocalDateTime createdAt
) {
}
