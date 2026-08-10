package com.ys.hsm.auth.controller;


import com.ys.hsm.auth.constants.ApplicationConstants;
import com.ys.hsm.auth.dto.request.LoginRequest;
import com.ys.hsm.auth.dto.request.RegisterRequest;
import com.ys.hsm.auth.dto.response.LoginResponse;
import com.ys.hsm.auth.dto.response.RegisterResponse;
import com.ys.hsm.auth.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApplicationConstants.AUTH_BASE)
public class AuthenticationController {

    private final AuthenticationService authService;

    @GetMapping(ApplicationConstants.HEALTH)
    public String health(){
        return "running";
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest registerRequest){
        RegisterResponse response = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
