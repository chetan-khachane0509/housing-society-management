package com.ys.hsm.auth.service;

import com.ys.hsm.auth.entity.RefreshToken;
import com.ys.hsm.auth.entity.User;

public interface RefreshTokenService {

    RefreshToken refreshToken(User user);

    RefreshToken verifyToken(String token);

    void revoke(String token);

    void revokeAll(String userId);
}
