package com.ys.hsm.resident.service.impl;

import com.ys.hsm.resident.client.SocietyClient;
import com.ys.hsm.resident.dto.request.ResidentRequest;
import com.ys.hsm.resident.entity.Resident;
import com.ys.hsm.resident.repository.ResidentRepository;
import com.ys.hsm.resident.service.ResidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ResidentServiceImpl implements ResidentService {

    private final ResidentRepository residentRepository;
    private final SocietyClient societyClient;

    @Override
    public Resident registerResident(ResidentRequest request, String authorizationHeader) {

        if(residentRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("email already exists.");
        }
        if(residentRepository.existsByMobile(request.getMobile())){
            throw new IllegalArgumentException("mobile number already registered.");
        }

        societyClient.validateResidence(
                request.getSocietyId(),
                request.getWingId(),
                request.getFlatId(),
                authorizationHeader
        );

        Resident resident = Resident.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .flatId(request.getFlatId())
                .societyId(request.getSocietyId())
                .wingId(request.getWingId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return residentRepository.save(resident);

    }
}
