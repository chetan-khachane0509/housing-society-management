package com.ys.hsm.auth.service;

import com.ys.hsm.auth.entity.User;

public interface JwtService {

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    String extractUserName(String token);

    boolean validateToken(String token);
}
