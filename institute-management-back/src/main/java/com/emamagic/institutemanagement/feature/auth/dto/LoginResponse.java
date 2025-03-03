package com.emamagic.institutemanagement.feature.auth.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {
}
