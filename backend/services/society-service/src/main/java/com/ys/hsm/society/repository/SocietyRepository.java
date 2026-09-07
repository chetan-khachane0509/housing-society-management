package com.ys.hsm.society.repository;

import com.ys.hsm.society.entity.Society;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocietyRepository extends MongoRepository<Society, String> {

    Optional<Society> findByRegistrationNumber(String registrationNumber);

    boolean existsByRegistrationNumber(String registrationNumber);

}
