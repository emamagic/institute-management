package com.emamagic.institutemanagement.config;

import com.emamagic.institutemanagement.entity.UserApp;
import com.emamagic.institutemanagement.feature.auth.jwt.JwtService;
import com.emamagic.institutemanagement.feature.user.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static com.emamagic.institutemanagement.config.SecurityConfig.PUBLIC_ENDPOINTS;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtSvc;
    private final UserService userSvc;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String jwt = authHeader.substring(7);
        String userId;
        try {
            userId = jwtSvc.extractUsername(jwt);
        } catch (ExpiredJwtException e) {
            response.setStatus(401);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Jwt token expired.\"}");
            return;
        }
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserApp userDetails = (UserApp) userSvc.loadUserByUsername(userId);
            if (!userDetails.getIsProfileCompleted() && !request.getServletPath().contains("users/profile")) {
                response.setStatus(428);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"The user has to complete their profile first.\"}");
                return;
            }
            if (!userDetails.isEnabled() && !request.getServletPath().contains("users/profile")) {
                response.setStatus(403);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Your account is disabled. Waiting for Admin approval.\"}");
                return;
            }
            if (jwtSvc.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getServletPath();
        if (Arrays.asList(PUBLIC_ENDPOINTS).contains(path)) {
            return true;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return authHeader == null || !authHeader.toLowerCase().startsWith("bearer ");
    }

}
