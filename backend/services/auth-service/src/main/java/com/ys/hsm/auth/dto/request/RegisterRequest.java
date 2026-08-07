package com.ys.hsm.auth.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "first name is required")
    @Size(min = 2, max = 10)
    private String firstName;

    @NotBlank(message = "last name is required")
    @Size(min = 2, max = 10)
    private String lastName;

    @NotBlank(message = "email is required")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "mobile number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$",
    message = "Invalid mobile number")
    private String mobileNumber;

    @NotBlank(message = "enter password")
    @Size(min = 8, max = 15)
    private String password;
}
