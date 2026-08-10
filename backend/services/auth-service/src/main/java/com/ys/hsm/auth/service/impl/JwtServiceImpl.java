package com.ys.hsm.auth.service.impl;

import com.ys.hsm.auth.config.JwtProperties;
import com.ys.hsm.auth.entity.User;
import com.ys.hsm.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtProperties jwtProperties;

    private SecretKey getSignInKey(){
        return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }
    public String generateAccessToken(User user) {

        return generateToken(user, jwtProperties.getAccessTokenExpiration());
    }

    private String generateToken(User user, long accessTokenExpiration) {
        Date issuedAt = new Date();
        Date expiryDate = new Date(issuedAt.getTime() + accessTokenExpiration);

        return Jwts.builder().subject(user.getEmail())
                .issuedAt(issuedAt)
                .expiration(expiryDate)
                .claim("userId", user.getId())
                .claim("role", user.getRole().name())
                .signWith(getSignInKey())
                .compact();
    }


    public String generateRefreshToken(User user) {

        return generateToken(user, jwtProperties.getRefreshTokenExpiration());
    }


    public String extractUserName(String token) {

        return extractAllClaims(token).getSubject();
    }


    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception exception) {
            return false;
        }

    }

    private Claims extractAllClaims(String token){
        return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token)
                .getPayload();
    }
}
