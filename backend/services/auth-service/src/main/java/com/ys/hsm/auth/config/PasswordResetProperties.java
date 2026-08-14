package com.ys.hsm.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application.security.password-reset")
public class PasswordResetProperties {

    private long resetTokenExpiration;
}
