package com.ys.hsm.auth.service;


import com.ys.hsm.auth.dto.request.*;
import com.ys.hsm.auth.dto.response.LoginResponse;
import com.ys.hsm.auth.dto.response.RefreshTokenResponse;
import com.ys.hsm.auth.dto.response.RegisterResponse;

public interface AuthenticationService {

    RegisterResponse register(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest loginRequest);

    RefreshTokenResponse refreshResponse(RefreshTokenRequest refreshTokenRequest);

    void logout(String token);

    void forgotPassword(ForgotPasswordRequest forgotPasswordRequest);

    void resetPassword(ResetPasswordRequest resetPasswordRequest);

    void changePassword(ChangePasswordRequest changePasswordRequest);
}
