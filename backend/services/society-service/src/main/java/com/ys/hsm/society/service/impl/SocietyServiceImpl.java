package com.ys.hsm.society.service.impl;

import com.ys.hsm.society.config.RegistrationNumberGenerator;
import com.ys.hsm.society.dto.request.SocietyRequest;
import com.ys.hsm.society.dto.response.SocietyResponse;
import com.ys.hsm.society.entity.Flat;
import com.ys.hsm.society.entity.Society;
import com.ys.hsm.society.entity.Wing;
import com.ys.hsm.society.enums.RegistrationStatus;
import com.ys.hsm.society.enums.SocietyStatus;
import com.ys.hsm.society.repository.FlatRepository;
import com.ys.hsm.society.repository.SocietyRepository;
import com.ys.hsm.society.repository.WingRepository;
import com.ys.hsm.society.service.SocietyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SocietyServiceImpl implements SocietyService {

    private final RegistrationNumberGenerator registrationNumberGenerator;
    private final SocietyRepository societyRepository;
    private final WingRepository wingRepository;
    private final FlatRepository flatRepository;

    public SocietyResponse createSociety(SocietyRequest societyRequest){

        String regNumber = registrationNumberGenerator.generate(
                societyRequest.getState(),
                societyRequest.getCity()
        );

        int totalWings = societyRequest.getWings().size();
        int totalFlats = societyRequest.getWings()
                .stream()
                .mapToInt(wing ->
                        wing.getTotalFloors() * wing.getFlatsPerFloor())
                .sum();

        Society society = Society.builder()
                .registrationNumber(regNumber)
                .registrationStatus(RegistrationStatus.PENDING)
                .societyName(societyRequest.getSocietyName())
                .address(societyRequest.getAddress())
                .state(societyRequest.getState())
                .city(societyRequest.getCity())
                .pinCode(societyRequest.getPinCode())
                .status(SocietyStatus.ACTIVE)
                .totalFlats(totalFlats)
                .totalWings(totalWings)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Society savedSociety = societyRepository.save(society);
        List<Wing> wings = societyRequest.getWings()
                .stream()
                .map(wingRequest ->
                        Wing.builder()
                                .societyId(savedSociety.getId())
                                .wingName(wingRequest.getWingName())
                                .totalFloors(wingRequest.getTotalFloors())
                                .totalFlats(
                                        wingRequest.getTotalFloors()
                                                * wingRequest.getFlatsPerFloor()
                                )
                                .build()
                )
                .toList();

        wingRepository.saveAll(wings);

        List<Flat> flats = new ArrayList<>();
        for(Wing wing : wings){
            Wing savedWing = wingRepository
                    .findById(wing.getId())
                    .orElseThrow(() -> new RuntimeException(
                            "Wing not found: " + wing.getId()));
            for(int floor = 1; floor <= savedWing.getTotalFloors(); floor++){
                int flatsPerFloor = savedWing.getTotalFlats()/savedWing.getTotalFloors();

                for (int flatSequence = 1;
                     flatSequence <= flatsPerFloor;
                     flatSequence++){

                    String flatNumber = savedWing.getWingName() + "-" + floor*100 + flatSequence;
                    Flat flat = Flat.builder()
                            .societyId(savedSociety.getId())
                            .wingId(savedWing.getId())
                            .flatNumber(flatNumber)
                            .floorNumber(floor)
                            .flatSequence(flatSequence)
                            .build();

                    flats.add(flat);
                }
            }


        }
        flatRepository.saveAll(flats);
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
