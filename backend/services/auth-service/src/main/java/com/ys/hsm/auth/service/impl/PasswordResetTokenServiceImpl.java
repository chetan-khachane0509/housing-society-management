package com.ys.hsm.auth.service.impl;

import com.ys.hsm.auth.config.JwtProperties;
import com.ys.hsm.auth.config.PasswordResetProperties;
import com.ys.hsm.auth.entity.PasswordResetToken;
import com.ys.hsm.auth.repository.PasswordResetTokenRepository;
import com.ys.hsm.auth.service.PasswordResetTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final JwtProperties jwtProperties;
    private final PasswordResetProperties passwordResetProperties;

    public String createToken(String userId) {

        passwordResetTokenRepository.deleteByUserId(userId);

        String token = UUID.randomUUID().toString();

        LocalDateTime expiryDate = LocalDateTime.now()
                .plusNanos(passwordResetProperties.getResetTokenExpiration() * 1_000_000);

        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .userId(userId)
                .token(token)
                .expiryDate(expiryDate)
                .used(false)
                .build();
        passwordResetTokenRepository.save(passwordResetToken);

        return token;
    }

    public void validateToken(String token) {

        PasswordResetToken passwordResetToken = passwordResetTokenRepository
                .findByToken(token)
                .orElseThrow(()-> new IllegalArgumentException("Invalid reset token"));

        if (passwordResetToken.isUsed()){
            throw new IllegalArgumentException("Reset token has already been used");
        }

        if (passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Reset token is expired");
        }

    }

    public void consumeToken(String token) {

        PasswordResetToken passwordResetToken = passwordResetTokenRepository
                .findByToken(token)
                .orElseThrow(()-> new IllegalArgumentException("Invalid reset token"));

        passwordResetToken.setUsed(true);

        passwordResetTokenRepository.save(passwordResetToken);
    }
}
