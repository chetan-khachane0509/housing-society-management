package com.ys.hsm.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application.security.jwt")
public class JwtProperties {


    /**
     * Secret used to sign JWT.
     */
    private String secretKey;

    /**
     * Access token validity (milliseconds)
     */
    private long accessTokenExpiration;

    /**
     * Refresh token validity (milliseconds)
     */
    private long refreshTokenExpiration;
}
