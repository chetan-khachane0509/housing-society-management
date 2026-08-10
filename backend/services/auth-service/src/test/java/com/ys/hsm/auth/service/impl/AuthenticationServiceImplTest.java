package com.ys.hsm.auth.service.impl;

import com.ys.hsm.auth.dto.request.RegisterRequest;
import com.ys.hsm.auth.dto.response.RegisterResponse;
import com.ys.hsm.auth.entity.User;
import com.ys.hsm.auth.enums.RoleType;
import com.ys.hsm.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

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
        verify(userRepository, org.mockito.Mockito.never())
                .save(any(User.class));
    }
}
