package com.ys.hsm.gateway.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
public class SecurityConfig {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                .authorizeExchange(exchange -> exchange

                        // Public endpoints
                        .pathMatchers(
                                "/api/v1/auth/register",
                                "/api/v1/auth/login",
                                "/api/v1/auth/health",
                                "/api/v1/society/health"
                        ).permitAll()

                        // Everything else requires JWT
                        .anyExchange().authenticated()
                )

                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> {})
                )

                .build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {

        SecretKey key = Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8)
        );

        return NimbusReactiveJwtDecoder.withSecretKey(key).build();
    }
}
