package com.ys.hsm.resident.repository;

import com.ys.hsm.resident.entity.Resident;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResidentRepository extends MongoRepository<Resident, String> {

    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);
}
