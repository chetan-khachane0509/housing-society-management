package com.ys.hsm.society.config;


import com.ys.hsm.society.repository.SocietyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Component
public class RegistrationNumberGenerator {

    private final SocietyRepository societyRepository;
    private final StateCodeResolver stateCodeResolver;

    public String generate(String state, String city) {

        String stateCode = stateCodeResolver.resolve(state);

        String cityCode = city.trim()
                .toUpperCase()
                .substring(0, Math.min(3, city.trim().length()));

        String registrationNumber;

        do {
            int randomNumber = ThreadLocalRandom.current()
                    .nextInt(100000, 1000000);

            registrationNumber =
                    stateCode + "/" + cityCode + "/" + randomNumber;

        } while (societyRepository.existsByRegistrationNumber(
                registrationNumber));

        return registrationNumber;
    }
}
