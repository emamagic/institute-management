package com.emamagic.institutemanagement.config;

import com.emamagic.institutemanagement.entity.UserApp;
import com.emamagic.institutemanagement.exception.ProfileCompletionException;
import com.emamagic.institutemanagement.feature.user.UserRepository;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class ProfileCompletionInterceptor implements HandlerInterceptor {
    private final UserRepository userRepo;

    @Override
    public boolean preHandle(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull Object handler
    ) {
        String userId = request.getUserPrincipal().getName();
        UserApp user = userRepo.findById(Long.valueOf(userId)).orElseThrow(() -> new NoSuchElementException("user not found"));
        if (!user.getIsProfileCompleted()) {
            throw new ProfileCompletionException("The user has to complete their profile first.");
        }

        return true;
    }
}
