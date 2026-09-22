package com.ys.hsm.complaint.client;

import com.ys.hsm.complaint.dto.resident.ResidentDetailsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class ResidentClient {

    private final RestClient.Builder restClientBuilder;

    public ResidentDetailsResponse getResidentById(
            String residentId,
            String authorizationHeader) {

        return restClientBuilder.build()
                .get()
                .uri(
                        "http://RESIDENT-SERVICE/api/v1/residents/{residentId}",
                        residentId
                )
                .header("Authorization", authorizationHeader)
                .retrieve()
                .body(ResidentDetailsResponse.class);
    }
}