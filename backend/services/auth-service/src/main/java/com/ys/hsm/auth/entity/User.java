package com.ys.hsm.auth.entity;

import com.ys.hsm.auth.enums.AccountStatus;
import com.ys.hsm.auth.enums.RoleType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User extends BaseDocument{
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String password;
    private RoleType role;
    private boolean emailVerified;
    private boolean mobileVerified;
    private AccountStatus accountStatus;
    private LocalDateTime lastLoginAt;
}
