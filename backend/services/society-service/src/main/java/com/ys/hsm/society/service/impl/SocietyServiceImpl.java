package com.ys.hsm.society.service.impl;

import com.ys.hsm.society.config.RegistrationNumberGenerator;
import com.ys.hsm.society.dto.request.SocietyRequest;
import com.ys.hsm.society.dto.response.SocietyResponse;
import com.ys.hsm.society.entity.Society;
import com.ys.hsm.society.enums.RegistrationStatus;
import com.ys.hsm.society.enums.SocietyStatus;
import com.ys.hsm.society.repository.SocietyRepository;
import com.ys.hsm.society.service.SocietyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class SocietyServiceImpl implements SocietyService {

    private final RegistrationNumberGenerator registrationNumberGenerator;
    private final SocietyRepository societyRepository;

    public SocietyResponse createSociety(SocietyRequest societyRequest){

        String regNumber = registrationNumberGenerator.generate(
                societyRequest.getState(),
                societyRequest.getCity()
        );

        Society society = Society.builder()
                .registrationNumber(regNumber)
                .registrationStatus(RegistrationStatus.PENDING)
                .societyName(societyRequest.getSocietyName())
                .address(societyRequest.getAddress())
                .state(societyRequest.getState())
                .city(societyRequest.getCity())
                .pinCode(societyRequest.getPinCode())
                .status(SocietyStatus.ACTIVE)
                .totalFlats(0)
                .totalWings(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Society savedSociety = societyRepository.save(society);
        return mapToResponse(savedSociety);
    }

    @Override
    public SocietyResponse approveSociety(String societyId) {
        Society society = societyRepository.findById(societyId)
                .orElseThrow(() ->
                        new RuntimeException("Society not found: " + societyId));

        if (society.getRegistrationStatus() != RegistrationStatus.PENDING) {
            throw new IllegalStateException(
                    "Only PENDING societies can be approved");
        }

        society.setRegistrationStatus(RegistrationStatus.VERIFIED);
        society.setUpdatedAt(LocalDateTime.now());

        Society savedSociety = societyRepository.save(society);

        return mapToResponse(savedSociety);
    }

    @Override
    public SocietyResponse rejectSociety(String societyId) {
        Society society = societyRepository.findById(societyId)
                .orElseThrow(() ->
                        new RuntimeException("Society not found: " + societyId));

        if (society.getRegistrationStatus() != RegistrationStatus.PENDING) {
            throw new IllegalStateException(
                    "Only PENDING societies can be rejected");
        }

        society.setRegistrationStatus(RegistrationStatus.REJECTED);
        society.setUpdatedAt(LocalDateTime.now());

        Society savedSociety = societyRepository.save(society);

        return mapToResponse(savedSociety);
    }

    private SocietyResponse mapToResponse(Society savedSociety) {
        return SocietyResponse.builder()
                .societyId(savedSociety.getId())
                .registrationNumber(savedSociety.getRegistrationNumber())
                .registrationStatus(savedSociety.getRegistrationStatus())
                .societyName(savedSociety.getSocietyName())
                .address(savedSociety.getAddress())
                .city(savedSociety.getCity())
                .state(savedSociety.getState())
                .pinCode(savedSociety.getPinCode())
                .totalWings(savedSociety.getTotalWings())
                .totalFlats(savedSociety.getTotalFlats())
                .status(savedSociety.getStatus())
                .createdAt(savedSociety.getCreatedAt())
                .updatedAt(savedSociety.getUpdatedAt())
                .build();
    }
}
