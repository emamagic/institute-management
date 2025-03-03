package com.emamagic.institutemanagement.feature.auth.jwt;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {
    String extractUsername(String token);
    JwtToken generateToken(UserDetails userDetails);
    JwtToken generateToken(Map<String, Object> claims, UserDetails userDetails);
    boolean isTokenValid(String token, UserDetails userDetails);
}
