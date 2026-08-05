package com.ys.hsm.auth.service.impl;

import com.ys.hsm.auth.entity.RefreshToken;
import com.ys.hsm.auth.entity.User;
import com.ys.hsm.auth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    /**
     * @param user
     * @return
     */
    @Override
    public RefreshToken refreshToken(User user) {
        return null;
    }

    /**
     * @param token
     * @return
     */
    @Override
    public RefreshToken verifyToken(String token) {
        return null;
    }

    /**
     * @param token
     */
    @Override
    public void revoke(String token) {

    }

    /**
     * @param userId
     */
    @Override
    public void revokeAll(String userId) {

    }
}
