package com.emamagic.institutemanagement.feature.user.dto;

import com.emamagic.institutemanagement.validation.ValidGender;
import com.emamagic.institutemanagement.validation.ValidUserRole;

public //todo: filter by creation date(to-day, yesterday, etc)
record UserSearch(
        String name,
        @ValidGender
        String gender,
        String email,
        String age,
        @ValidUserRole
        String role,
        Boolean isApproved,
        Boolean isVerified,
        Boolean isProfileCompleted
) {
}
