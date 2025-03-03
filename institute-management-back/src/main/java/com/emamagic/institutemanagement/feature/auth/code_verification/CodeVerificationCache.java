package com.emamagic.institutemanagement.feature.auth.code_verification;

public interface CodeVerificationCache {
    String generateAndSave(String username, int len);
    String lookup(String username);
    void evict(String username);
}
