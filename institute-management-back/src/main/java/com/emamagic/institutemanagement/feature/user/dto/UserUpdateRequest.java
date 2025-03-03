package com.emamagic.institutemanagement.feature.user.dto;

import com.emamagic.institutemanagement.validation.ValidGender;
import lombok.Getter;

public @Getter
class UserUpdateRequest {
    private String name;
    private String age;
    @ValidGender
    private String gender;
}
