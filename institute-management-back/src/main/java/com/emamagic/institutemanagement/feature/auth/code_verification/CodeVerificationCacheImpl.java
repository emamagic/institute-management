package com.emamagic.institutemanagement.feature.auth.code_verification;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@CacheConfig(cacheNames = "CodeVerification")
public class CodeVerificationCacheImpl implements CodeVerificationCache {
    private final SecureRandom random = new SecureRandom();
    private static final String NUMS = "0123456789";

    @Override
    @CachePut(key = "#username")
    public String generateAndSave(String username, int len) {
        StringBuilder codeBuilder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char code = NUMS.charAt(random.nextInt(NUMS.length()));
            codeBuilder.append(code);
        }

        return codeBuilder.toString();
    }

    @Override
    @Cacheable(key = "#username")
    public String lookup(String username) {
        // The @Cacheable annotation checks the cache for the 'username' key.
        // If the key is not found, this method returns null.
        // In a real application, you might fetch the code from a persistent store here.
        return null;
    }

    @Override
    @CacheEvict(key = "#username")
    public void evict(String username) {
        // The @CacheEvict annotation removes the 'username' key from the cache.
        // Additional cleanup operations (e.g., removing from a database) can be performed here if necessary.
    }

}
