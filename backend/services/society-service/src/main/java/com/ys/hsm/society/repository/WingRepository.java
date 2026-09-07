package com.ys.hsm.society.repository;

import com.ys.hsm.society.entity.Wing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WingRepository extends MongoRepository<Wing, String> {

    List<Wing> findBySocietyId(String societyId);

    boolean existsBySocietyIdAndWingName(
            String societyId,
            String wingName);
}
