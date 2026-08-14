package com.ys.hsm.auth.service;

public interface PasswordResetTokenService {

    String createToken(String userId);

    void validateToken(String token);

    void consumeToken(String token);
}
