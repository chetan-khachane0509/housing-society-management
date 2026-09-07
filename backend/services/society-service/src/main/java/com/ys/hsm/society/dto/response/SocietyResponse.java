package com.ys.hsm.society.dto.response;

import com.ys.hsm.society.enums.RegistrationStatus;
import com.ys.hsm.society.enums.SocietyStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SocietyResponse {

    private String societyId;

    private String societyName;
    private String registrationNumber;
    private RegistrationStatus registrationStatus;
    private String address;
    private String city;
    private String state;
    private String pinCode;

    private Integer totalWings;
    private Integer totalFlats;

    private SocietyStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

