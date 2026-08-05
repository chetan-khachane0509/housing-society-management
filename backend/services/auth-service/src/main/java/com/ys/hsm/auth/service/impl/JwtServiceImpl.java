package com.ys.hsm.auth.service.impl;

import com.ys.hsm.auth.entity.User;
import com.ys.hsm.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    /**
     * @param user
     * @return
     */
    @Override
    public String generateAccessToken(User user) {
        return "";
    }

    /**
     * @param user
     * @return
     */
    @Override
    public String generateRefreshToken(User user) {
        return "";
    }

    /**
     * @param token
     * @return
     */
    @Override
    public String extractUserName(String token) {
        return "";
    }

    /**
     * @param token
     * @return
     */
    @Override
    public boolean validateToken(String token) {
        return false;
    }
}
