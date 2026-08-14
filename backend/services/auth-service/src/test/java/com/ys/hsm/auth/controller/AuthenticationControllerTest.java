package com.ys.hsm.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.hsm.auth.dto.request.*;
import com.ys.hsm.auth.dto.response.LoginResponse;
import com.ys.hsm.auth.dto.response.RegisterResponse;
import com.ys.hsm.auth.service.AuthenticationService;
import com.ys.hsm.auth.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void register_shouldReturn201_whenRequestIsValid() throws Exception {

        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("Chetan");
        request.setLastName("Khachane");
        request.setEmail("khachanechetan94@gmail.com");
        request.setMobileNumber("9146102405");
        request.setPassword("Chetan@0509");

        RegisterResponse registerResponse = RegisterResponse.builder()
                .userId("test-user-id")
                .email(request.getEmail())
                .message("user registered successfully")
                .build();

        when(authenticationService.register(any(RegisterRequest.class)))
                .thenReturn(registerResponse);

        // Act & Assert
        mockMvc.perform(
                        post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("test-user-id"))
                .andExpect(jsonPath("$.email")
                        .value("khachanechetan94@gmail.com"))
                .andExpect(jsonPath("$.message")
                        .value("user registered successfully"));
    }

    @Test
    void register_shouldReturn400_whenEmailIsInvalid() throws Exception {

        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("Chetan");
        request.setLastName("Khachane");
        request.setEmail("invalid-email");
        request.setMobileNumber("9146102405");
        request.setPassword("Chetan@0509");

        // Act & Assert
        mockMvc.perform(
                        post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());

        // Service should not be called
        verify(authenticationService, never())
                .register(any(RegisterRequest.class));
    }

    @Test
    void login_shouldReturn400_whenLoginCredentialsInvalid() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("khachane@gmail.com");
        loginRequest.setPassword("");

        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest))
                )
                .andExpect(status().isBadRequest());
        verify(authenticationService, never())
                .login(any(LoginRequest.class));
    }

    @Test
    void login_shouldReturn200_whenCredentialsAreValid() throws Exception {

        LoginRequest request = new LoginRequest();
        request.setEmail("khachanechetan94@gmail.com");
        request.setPassword("Chetan@0509");

        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .tokenType("Bearer")
                .expiresIn(900000L)
                .build();

        when(authenticationService.login(any(LoginRequest.class)))
                .thenReturn(loginResponse);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken")
                        .value("access-token"))
                .andExpect(jsonPath("$.refreshToken")
                        .value("refresh-token"))
                .andExpect(jsonPath("$.tokenType")
                        .value("Bearer"))
                .andExpect(jsonPath("$.expiresIn")
                        .value(900000));

        verify(authenticationService).login(any(LoginRequest.class));
    }

    @Test
    void refresh_shouldReturn400_whenRefreshTokenIsInvalid() throws Exception {

        String invalidToken = "invalid-refresh-token";

        when(authenticationService.refreshResponse(any(RefreshTokenRequest.class)))
                .thenThrow(new IllegalArgumentException("Invalid refresh token"));

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "refreshToken": "invalid-refresh-token"
                            }
                            """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void refresh_shouldReturn400_whenRefreshTokenIsRevoked() throws Exception {

        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("revoked-refresh-token");

        when(authenticationService.refreshResponse(any(RefreshTokenRequest.class)))
                .thenThrow(new IllegalStateException(
                        "Refresh token has been revoked"
                ));

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "refreshToken": "revoked-refresh-token"
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Refresh token has been revoked"));
    }

    @Test
    void logout_shouldReturn204_whenLogoutIsSuccessful() throws Exception {

        String token = "valid-refresh-token";

        doNothing().when(authenticationService).logout(token);

        mockMvc.perform(post("/api/v1/auth/logout")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "refreshToken": "valid-refresh-token"
                            }
                            """))
                .andExpect(status().isNoContent());

        verify(authenticationService).logout(token);
    }

    @Test
    void forgotPassword_shouldReturn204() throws Exception {

        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("user@gmail.com");

        doNothing()
                .when(authenticationService)
                .forgotPassword(any(ForgotPasswordRequest.class));

        mockMvc.perform(
                        post("/api/v1//auth/forgot-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNoContent());

        verify(authenticationService)
                .forgotPassword(any(ForgotPasswordRequest.class));
    }

    @Test
    void resetPassword_shouldReturn204() throws Exception {

        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setResetToken("reset-token-123");
        request.setNewPassword("NewPassword@123");

        doNothing()
                .when(authenticationService)
                .resetPassword(any(ResetPasswordRequest.class));

        mockMvc.perform(
                        post("/api/v1/auth/reset-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNoContent());

        verify(authenticationService)
                .resetPassword(any(ResetPasswordRequest.class));
    }

    @Test
    void changePassword_shouldReturn204() throws Exception {

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword("OldPassword@123");
        request.setNewPassword("NewPassword@456");

        doNothing()
                .when(authenticationService)
                .changePassword(any(ChangePasswordRequest.class));

        mockMvc.perform(
                        post("/api/v1/auth/change-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNoContent());

        verify(authenticationService)
                .changePassword(any(ChangePasswordRequest.class));
    }
}