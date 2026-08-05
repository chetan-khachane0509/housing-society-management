package com.ys.hsm.auth.service.impl;

import com.ys.hsm.auth.dto.request.*;
import com.ys.hsm.auth.dto.response.LoginResponse;
import com.ys.hsm.auth.dto.response.RefreshTokenResponse;
import com.ys.hsm.auth.dto.response.RegisterResponse;
import com.ys.hsm.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    /**
     * @param registerRequest
     * @return
     */
    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        return null;
    }

    /**
     * @param loginRequest
     * @return
     */
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        return null;
    }

    /**
     * @param refreshTokenRequest
     * @return
     */
    @Override
    public RefreshTokenResponse refreshResponse(RefreshTokenRequest refreshTokenRequest) {
        return null;
    }

    /**
     * @param token
     */
    @Override
    public void logout(String token) {

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
