package com.ys.hsm.auth.service.impl;

import com.ys.hsm.auth.dto.request.*;
import com.ys.hsm.auth.dto.response.LoginResponse;
import com.ys.hsm.auth.dto.response.RefreshTokenResponse;
import com.ys.hsm.auth.dto.response.RegisterResponse;
import com.ys.hsm.auth.entity.User;import com.ys.hsm.auth.enums.RoleType;import com.ys.hsm.auth.repository.UserRepository;import com.ys.hsm.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
