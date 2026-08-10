package com.ys.hsm.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.hsm.auth.dto.request.RegisterRequest;
import com.ys.hsm.auth.dto.response.RegisterResponse;
import com.ys.hsm.auth.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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
}