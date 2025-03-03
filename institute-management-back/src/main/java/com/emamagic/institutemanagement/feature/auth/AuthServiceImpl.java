package com.emamagic.institutemanagement.feature.auth;

import com.emamagic.institutemanagement.entity.UserApp;
import com.emamagic.institutemanagement.entity.UserRole;
import com.emamagic.institutemanagement.exception.CodeValidationException;
import com.emamagic.institutemanagement.feature.auth.code_verification.CodeVerificationCache;
import com.emamagic.institutemanagement.feature.auth.dto.CodeVerificationRequest;
import com.emamagic.institutemanagement.feature.auth.dto.LoginRequest;
import com.emamagic.institutemanagement.feature.auth.dto.LoginResponse;
import com.emamagic.institutemanagement.feature.auth.dto.RegisterRequest;
import com.emamagic.institutemanagement.feature.auth.jwt.JwtService;
import com.emamagic.institutemanagement.feature.auth.jwt.JwtToken;
import com.emamagic.institutemanagement.feature.email.EmailService;
import com.emamagic.institutemanagement.feature.email.EmailTemplateName;
import com.emamagic.institutemanagement.feature.user.UserRepository;
import com.emamagic.institutemanagement.feature.user.role.RoleType;
import com.emamagic.institutemanagement.feature.user.role.UserRoleRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final CodeVerificationCache codeVerificationCache;
    private final EmailService emailSvc;
    private final UserRepository userRepo;
    private final UserRoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtSvc;
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    @Transactional
    @Override
    public void register(RegisterRequest req) {
        checkVerificationCodeAlreadyExist(req.email());
        UserApp user = saveUserIfRequired(req);
        sendVerificationEmail(user);
    }

    @Transactional
    @Override
    public void verifyCode(CodeVerificationRequest req) {
        String existingCode = codeVerificationCache.lookup(req.email());
        if (!req.code().equals(existingCode)) {
            throw new IllegalArgumentException("Invalid verification code or email");
        }
        UserApp user = userRepo.findByEmail(req.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setIsVerified(true);

        codeVerificationCache.evict(req.email());
    }

    @Transactional
    @Override
    public LoginResponse login(LoginRequest req) {
        String errMessage = "email or password is not correct";
        UserApp user = userRepo.findByEmail(req.email()).orElseThrow(() -> new NoSuchElementException(errMessage));
        if (!passwordEncoder.matches(req.password(), user.getHashedPassword())) {
            throw new NoSuchElementException(errMessage);
        }
        JwtToken jwtToken = jwtSvc.generateToken(user);
        return new LoginResponse(jwtToken.accessToken(), jwtToken.refreshToken());
    }

    @Transactional
    @Override
    public void logout() {
        UserApp user = (UserApp) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        userRepo.updateVerificationCodeCompletion(user.getId(), false);
    }

    @Override
    public LoginResponse refresh() {
        UserApp user = (UserApp) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        JwtToken jwtToken = jwtSvc.generateToken(user);
        return new LoginResponse(jwtToken.accessToken(), jwtToken.refreshToken());
    }

    private void checkVerificationCodeAlreadyExist(String email) {
        String existingCode = codeVerificationCache.lookup(email);
        if (existingCode != null) {
            throw new CodeValidationException("A verification code was already sent recently. You can try a new one after 2 min");
        }
    }

    private UserApp saveUserIfRequired(RegisterRequest req) {
        UserRole role = roleRepo.findByType(RoleType.valueOf(req.role().toUpperCase()))
                .orElseThrow(() -> new NoSuchElementException("Role not found"));
        if (role.getType().equals(RoleType.ADMIN)) {
            throw new IllegalArgumentException("you can not set role as ADMIN directly");
        }
        return userRepo.findByEmail(req.email())
                .orElseGet(() -> userRepo.save(UserApp.builder()
                        .email(req.email())
                        .hashedPassword(passwordEncoder.encode(req.password()))
                        .role(role)
                        .build()));
    }

    private void sendVerificationEmail(UserApp user) {
        String verificationCode = codeVerificationCache.generateAndSave(user.getEmail(), 4);
        try {
            emailSvc.sendEmail(
                    user.getEmail(),
                    user.getName(),
                    EmailTemplateName.ACTIVATE_ACCOUNT,
                    activationUrl,
                    verificationCode,
                    "Account activation"
            );
        } catch (MessagingException e) {
            throw new IllegalArgumentException("Email verification exception");
        }
    }

}
