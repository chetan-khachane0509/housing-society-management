package com.ys.hsm.auth.service.impl;

import com.ys.hsm.auth.config.JwtProperties;
import com.ys.hsm.auth.dto.request.LoginRequest;
import com.ys.hsm.auth.dto.request.RegisterRequest;
import com.ys.hsm.auth.dto.response.LoginResponse;
import com.ys.hsm.auth.dto.response.RegisterResponse;
import com.ys.hsm.auth.entity.User;
import com.ys.hsm.auth.enums.AccountStatus;
import com.ys.hsm.auth.enums.RoleType;
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
    void login_shouldReturnTokens_whenCredentialsAreValid(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("khachanechetan94@gmail.com");
        loginRequest.setPassword("Chetan@0509");

        User saveUser = User.builder().Id("user-001")
                .email(loginRequest.getEmail())
                .password("$2a#1Apasswordhash")
                .role(RoleType.SOCIETY_ADMIN)
                .accountStatus(AccountStatus.ACTIVE)
                .emailVerified(false)
                .mobileVerified(false)
                .build();

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.ofNullable(saveUser));
        when(passwordEncoder.matches(loginRequest.getPassword(), saveUser.getPassword())).thenReturn(true);
        when(jwtService.generateAccessToken(saveUser)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(saveUser)).thenReturn("refresh-token");
        when(jwtProperties.getAccessTokenExpiration()).thenReturn(900000L);
        when(userRepository.save(any(User.class))).thenReturn(saveUser);


        LoginResponse loginResponse = authenticationService.login(loginRequest);

        assertNotNull(loginResponse);
        assertEquals("access-token",loginResponse.getAccessToken());
        assertEquals("refresh-token", loginResponse.getRefreshToken());
        assertEquals("Bearer", loginResponse.getTokenType());
        assertEquals(900000L, loginResponse.getExpiresIn());
        assertNotNull(saveUser.getLastLoginAt());

        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder).matches(loginRequest.getPassword(),saveUser.getPassword());
        verify(jwtService).generateAccessToken(saveUser);
        verify(jwtService).generateRefreshToken(saveUser);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void login_shouldThrowException_whenCredentialsAreNotValid(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("khachanechetan94@gmail.com");
        loginRequest.setPassword("Chetan0509");

        User user = User.builder().Id("user-001")
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
                .Id("user-001")
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
}
