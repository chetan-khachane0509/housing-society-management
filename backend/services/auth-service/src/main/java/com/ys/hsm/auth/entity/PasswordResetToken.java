package com.ys.hsm.auth.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    private String id;

    private String userId;

    private String token;

    private LocalDateTime expiryDate;

    private boolean used;
}
