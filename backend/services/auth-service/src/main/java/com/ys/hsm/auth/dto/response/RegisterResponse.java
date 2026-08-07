package com.ys.hsm.auth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {

    private String userId;
    private String email;
    private String message;
}
