package com.emamagic.institutemanagement.feature.auth;

import com.emamagic.institutemanagement.feature.auth.dto.CodeVerificationRequest;
import com.emamagic.institutemanagement.feature.auth.dto.LoginRequest;
import com.emamagic.institutemanagement.feature.auth.dto.LoginResponse;
import com.emamagic.institutemanagement.feature.auth.dto.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest req);
    void verifyCode(CodeVerificationRequest req);
    LoginResponse login(LoginRequest req);
    void logout();
    LoginResponse refresh();
}
