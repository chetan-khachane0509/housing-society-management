package com.ys.hsm.resident.client;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class SocietyClient {

    private final RestClient.Builder restClientBuilder;

    public void validateResidence(
            String societyId,
            String wingId,
            String flatId, String authorizationHeader) {

//        JwtAuthenticationToken authentication =
//                (JwtAuthenticationToken) SecurityContextHolder
//                        .getContext()
//                        .getAuthentication();
//
//        String token = authentication.getToken().getTokenValue();

        restClientBuilder
                .baseUrl("http://society-service")
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/society/{societyId}/wings/{wingId}/flats/{flatId}/validate")
                        .build(societyId, wingId, flatId))
                .header("Authorization", authorizationHeader)
                .retrieve()
                .toBodilessEntity();
    }
}