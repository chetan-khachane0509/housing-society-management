package com.ys.hsm.auth.service.impl;

import com.ys.hsm.auth.config.JwtProperties;
import com.ys.hsm.auth.dto.request.LoginRequest;
import com.ys.hsm.auth.dto.request.RefreshTokenRequest;
import com.ys.hsm.auth.dto.request.RegisterRequest;
import com.ys.hsm.auth.dto.response.LoginResponse;
import com.ys.hsm.auth.dto.response.RegisterResponse;
import com.ys.hsm.auth.entity.RefreshToken;
import com.ys.hsm.auth.entity.User;
import com.ys.hsm.auth.enums.AccountStatus;
import com.ys.hsm.auth.enums.RoleType;
import com.ys.hsm.auth.repository.RefreshTokenRepository;
import com.ys.hsm.auth.repository.UserRepository;
import com.ys.hsm.auth.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private JwtService jwtService;

    @Mock
    private JwtProperties jwtProperties;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void setUp(){}

    @Test
    void register_shouldRegisterUserSuccessfully(){
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("Chetan");
        request.setLastName("Khachane");
        request.setEmail("khachanechetan94@gmail.com");
        request.setMobileNumber("9146102405");
        request.setPassword("Chetan@0509");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("$2a#1Apasswordhash");

        User savedUser = User.builder().firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .mobileNumber(request.getMobileNumber())
                .password("$2a#1Apasswordhash")
                .emailVerified(false)
                .mobileVerified(false)
                .role(RoleType.SOCIETY_ADMIN)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        RegisterResponse response = authenticationService.register(request);

        assertNotNull(response);
        assertEquals("khachanechetan94@gmail.com", response.getEmail());
        assertEquals("user registered successfully", response.getMessage());

        verify(userRepository).existsByEmail("khachanechetan94@gmail.com");
        verify(passwordEncoder).encode("Chetan@0509");

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository)
                .save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        // Assert - User object
        assertEquals(
                request.getFirstName(),
                capturedUser.getFirstName()
        );

        assertEquals(
                request.getLastName(),
                capturedUser.getLastName()
        );

        assertEquals(
                request.getEmail(),
                capturedUser.getEmail()
        );

        assertEquals(
                request.getMobileNumber(),
                capturedUser.getMobileNumber()
        );

        // Verify password is encoded
        assertEquals(
                "$2a#1Apasswordhash",
                capturedUser.getPassword()
        );

        assertEquals(
                RoleType.SOCIETY_ADMIN,
                capturedUser.getRole()
        );

        assertFalse(capturedUser.isEmailVerified());
        assertFalse(capturedUser.isMobileVerified());


    }

    @Test
    void register_shouldThrowException_whenEmailAlreadyExists() {


        RegisterRequest request = new RegisterRequest();
        request.setFirstName("Chetan");
        request.setLastName("Khachane");
        request.setEmail("khachanechetan94@gmail.com");
        request.setMobileNumber("9146102405");
        request.setPassword("Chetan@0509");

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);


        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authenticationService.register(request)
        );

        assertEquals(
                "Email already registered.",
                exception.getMessage()
        );

        // Verify save was never called
        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void login_shouldReturnTokens_whenCredentialsAreValid() {

            // Arrange
            LoginRequest request = new LoginRequest();
            request.setEmail("khachanechetan94@gmail.com");
            request.setPassword("Chetan@0509");

            User user = User.builder()
                    .id("user-001")
                    .email("khachanechetan94@gmail.com")
                    .password("$2a$10$hashedPassword")
                    .role(RoleType.SOCIETY_ADMIN)
                    .accountStatus(AccountStatus.ACTIVE)
                    .emailVerified(false)
                    .mobileVerified(false)
                    .build();

            when(userRepository.findByEmail(request.getEmail()))
                    .thenReturn(Optional.of(user));

            when(passwordEncoder.matches(
                    request.getPassword(),
                    user.getPassword()))
                    .thenReturn(true);

            when(jwtService.generateAccessToken(user))
                    .thenReturn("access-token");

            when(jwtService.generateRefreshToken(user))
                    .thenReturn("refresh-token");

            when(jwtProperties.getAccessTokenExpiration())
                    .thenReturn(900000L);

            when(jwtProperties.getRefreshTokenExpiration())
                    .thenReturn(604800000L);

            when(refreshTokenRepository.save(any(RefreshToken.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            when(userRepository.save(any(User.class)))
                    .thenReturn(user);

            // Act
            LoginResponse response = authenticationService.login(request);

            // Assert
            assertNotNull(response);

            assertEquals(
                    "access-token",
                    response.getAccessToken()
            );

            assertEquals(
                    "refresh-token",
                    response.getRefreshToken()
            );

            assertEquals(
                    "Bearer",
                    response.getTokenType()
            );

            assertEquals(
                    900000L,
                    response.getExpiresIn()
            );

            // Verify user lookup
            verify(userRepository)
                    .findByEmail(request.getEmail());

            // Verify password
            verify(passwordEncoder)
                    .matches(
                            request.getPassword(),
                            user.getPassword()
                    );

            // Verify JWT generation
            verify(jwtService)
                    .generateAccessToken(user);

            verify(jwtService)
                    .generateRefreshToken(user);

            // Verify refresh token persistence
            verify(refreshTokenRepository)
                    .save(argThat((RefreshToken token) ->
                            token.getUserId().equals(user.getId())
                                    && token.getToken().equals("refresh-token")
                                    && !token.isRevoked()
                                    && token.getExpiryDate() != null
                    ));

            // Verify user update
            verify(userRepository)
                    .save(user);

            assertNotNull(user.getLastLoginAt());

    }

    @Test
    void login_shouldThrowException_whenCredentialsAreNotValid(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("khachanechetan94@gmail.com");
        loginRequest.setPassword("Chetan0509");

        User user = User.builder().id("user-001")
                .email(loginRequest.getEmail())
                .password("$2a#1Apasswordhash")
                .role(RoleType.SOCIETY_ADMIN)
                .accountStatus(AccountStatus.ACTIVE)
                .emailVerified(false)
                .mobileVerified(false)
                .build();
        //when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        //remove the below comment to test for invalid password
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                ()-> authenticationService.login(loginRequest));
        assertEquals("Invalid Email or Password", exception.getMessage());
        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder).matches(loginRequest.getPassword(),user.getPassword());
        verify(userRepository, never())
                .save(any(User.class));

    }

    @Test
    void login_shouldThrowException_whenAccountIsNotActive() {

        LoginRequest request = new LoginRequest();
        request.setEmail("khachanechetan94@gmail.com");
        request.setPassword("Chetan@0509");

        User user = User.builder()
                .id("user-001")
                .email(request.getEmail())
                .password("$2a$10$hashedPassword")
                .role(RoleType.SOCIETY_ADMIN)
                .accountStatus(AccountStatus.PENDING_VERIFICATION)
                .emailVerified(false)
                .mobileVerified(false)
                .build();

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> authenticationService.login(request)
        );

        assertEquals("User account is not active", exception.getMessage());

        // Verify
        verify(userRepository).findByEmail(request.getEmail());

        verify(passwordEncoder, never())
                .matches(anyString(), anyString());

        verify(jwtService, never())
                .generateAccessToken(any(User.class));

        verify(jwtService, never())
                .generateRefreshToken(any(User.class));

        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void refreshResponse_shouldThrowException_whenTokenIsInvalid(){
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setRefreshToken("invalid-refresh-token");

        when(refreshTokenRepository.findByToken(refreshTokenRequest.getRefreshToken()))
                .thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, ()-> authenticationService.refreshResponse(refreshTokenRequest)
        );

        assertEquals("Invalid refresh token",exception.getMessage());
        verify(refreshTokenRepository).findByToken(refreshTokenRequest.getRefreshToken());

    }
    @Test
    void refreshResponse_shouldThrowException_whenTokenIsRevoked(){
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setRefreshToken("refresh-token");

        RefreshToken refreshToken = RefreshToken.builder()
                .id("refresh-001")
                .userId("user-001")
                .token(refreshTokenRequest.getRefreshToken())
                .revoked(true)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();

        when(refreshTokenRepository.findByToken(refreshTokenRequest.getRefreshToken()))
                .thenReturn(Optional.of(refreshToken));
        IllegalStateException exception = assertThrows(
                IllegalStateException.class, ()-> authenticationService.refreshResponse(refreshTokenRequest)
        );

        assertEquals("refresh token is revoked.",exception.getMessage());
        verify(refreshTokenRepository).findByToken(refreshTokenRequest.getRefreshToken());

    }

    @Test
    void refreshResponse_shouldThrowException_whenTokenIsExpired(){
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setRefreshToken("expired-refresh-token");

        RefreshToken refreshToken = RefreshToken.builder()
                .id("refresh-001")
                .userId("user-001")
                .token(refreshTokenRequest.getRefreshToken())
                .revoked(false)
                .expiryDate(LocalDateTime.now().minusDays(1))
                .build();

        when(refreshTokenRepository.findByToken(refreshTokenRequest.getRefreshToken()))
                .thenReturn(Optional.of(refreshToken));
        IllegalStateException exception = assertThrows(
                IllegalStateException.class, ()-> authenticationService.refreshResponse(refreshTokenRequest)
        );

        assertEquals("refresh token has expired",exception.getMessage());
        verify(refreshTokenRepository).findByToken(refreshTokenRequest.getRefreshToken());

    }

    @Test
    void logout_shouldRevokeRefreshToken_whenTokenIsValid() {

        // Arrange
        String refreshTokenValue = "valid-refresh-token";

        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken(refreshTokenValue);

        RefreshToken refreshToken = RefreshToken.builder()
                .id("refresh-001")
                .userId("user-001")
                .token(refreshTokenValue)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .build();

        when(refreshTokenRepository.findByToken(refreshTokenValue))
                .thenReturn(Optional.of(refreshToken));

        // Act
        authenticationService.logout(request.getRefreshToken());

        // Assert
        assertTrue(refreshToken.isRevoked());

        // Verify token lookup
        verify(refreshTokenRepository)
                .findByToken(refreshTokenValue);

        // Verify the SAME entity was saved
        verify(refreshTokenRepository)
                .save(refreshToken);
    }
}
