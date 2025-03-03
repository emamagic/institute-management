package com.emamagic.institutemanagement.feature.auth;

import com.emamagic.institutemanagement.feature.auth.dto.CodeVerificationRequest;
import com.emamagic.institutemanagement.feature.auth.dto.LoginRequest;
import com.emamagic.institutemanagement.feature.auth.dto.LoginResponse;
import com.emamagic.institutemanagement.feature.auth.dto.RegisterRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {
    private final AuthService svc;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest req) {
        svc.register(req);
        URI location = URI.create("/users/me");
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/verify")
    public ResponseEntity<Void> verifyCode(@RequestBody @Valid CodeVerificationRequest req) {
        svc.verifyCode(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest req) {
        return ResponseEntity.ok(svc.login(req));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        svc.logout();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh() {
        return ResponseEntity.ok(svc.refresh());
    }
}
