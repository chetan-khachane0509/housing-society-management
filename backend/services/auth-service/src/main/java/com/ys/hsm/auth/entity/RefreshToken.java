package com.ys.hsm.auth.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collation = "refresh_tokens")
public class RefreshToken {

    @Id
    private String id;

    private String userId;

    private String token;

    private LocalDateTime expiryDate;

    private boolean revoked;
}
