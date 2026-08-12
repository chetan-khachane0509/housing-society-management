package com.ys.hsm.auth.service.impl;

import com.ys.hsm.auth.config.JwtProperties;
import com.ys.hsm.auth.dto.request.*;
import com.ys.hsm.auth.dto.response.LoginResponse;
import com.ys.hsm.auth.dto.response.RefreshTokenResponse;
import com.ys.hsm.auth.dto.response.RegisterResponse;
import com.ys.hsm.auth.entity.RefreshToken;
import com.ys.hsm.auth.entity.User;
import com.ys.hsm.auth.enums.AccountStatus;
import com.ys.hsm.auth.enums.RoleType;
import com.ys.hsm.auth.repository.RefreshTokenRepository;
import com.ys.hsm.auth.repository.UserRepository;import com.ys.hsm.auth.service.AuthenticationService;
import com.ys.hsm.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    public RegisterResponse register(RegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.getEmail())){
            throw new IllegalArgumentException("Email already registered.");
        }
        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .mobileNumber(registerRequest.getMobileNumber())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(RoleType.SOCIETY_ADMIN)
                .emailVerified(false)
                .mobileVerified(false)
                .build();
        User saveUser = userRepository.save(user);
        return RegisterResponse.builder()
                .userId(saveUser.getId())
                .email(saveUser.getEmail())
                .message("user registered successfully")
                .build();
    }


    public LoginResponse login(LoginRequest loginRequest) {

       User user = userRepository.findByEmail(loginRequest.getEmail())
               .orElseThrow(()-> new IllegalArgumentException("Invalid Email or Password"));

       if (user.getAccountStatus()!= AccountStatus.ACTIVE){
           throw new IllegalStateException("User account is not active");
       }

       if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
           throw new IllegalArgumentException("Invalid Email or Password");
       }

       String accessToken = jwtService.generateAccessToken(user);
       String refreshToken = jwtService.generateRefreshToken(user);

        RefreshToken refreshTokenEntity = RefreshToken.builder().userId(user.getId())
                        .token(refreshToken)
                                .expiryDate(LocalDateTime.now()
                                        .plusNanos(jwtProperties.getRefreshTokenExpiration()
                                        * 1_000_000))
                                        .revoked(false)
                                                .build();
        refreshTokenRepository.save(refreshTokenEntity);
       user.setLastLoginAt(LocalDateTime.now());
       userRepository.save(user);

       return LoginResponse.builder().accessToken(accessToken)
               .refreshToken(refreshToken)
               .tokenType("Bearer")
               .expiresIn(jwtProperties.getAccessTokenExpiration())
               .build();
    }

    /**
     * @param refreshTokenRequest
     * @return
     */
    @Override
    public RefreshTokenResponse refreshResponse(RefreshTokenRequest refreshTokenRequest) {

        String token = refreshTokenRequest.getRefreshToken();
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(()-> new IllegalArgumentException("Invalid refresh token"));

        if (refreshToken.isRevoked()){
            throw new IllegalStateException("refresh token is revoked.");
        }

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new IllegalStateException("refresh token has expired");
        }

        if (!jwtService.validateToken(token)){
            throw new IllegalArgumentException("Invalid refresh token");
        }

        User user = userRepository.findById(refreshToken.getUserId()).orElseThrow(
                ()-> new IllegalArgumentException("User not found")
        );

        if (user.getAccountStatus()!= AccountStatus.ACTIVE){
            throw new IllegalStateException("User account is not active");
        }

        String accessToken = jwtService.generateAccessToken(user);
        return RefreshTokenResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .expiresIn(jwtProperties.getAccessTokenExpiration())
                .build();
    }

    /**
     * @param token
     */
    @Override
    public void logout(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        if (refreshToken.isRevoked()) {
            throw new IllegalStateException("Refresh token has already been revoked");
        }
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    /**
     * @param forgotPasswordRequest
     */
    @Override
    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {

    }

    /**
     * @param resetPasswordRequest
     */
    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {

    }

    /**
     * @param changePasswordRequest
     */
    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {

    }
}
