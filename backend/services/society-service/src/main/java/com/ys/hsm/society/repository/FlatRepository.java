package com.ys.hsm.society.repository;

import com.ys.hsm.society.entity.Flat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlatRepository extends MongoRepository<Flat, String> {
    List<Flat> findByWingId(String wingId);

    boolean existsByWingIdAndFlatNumber(String wingId, String flatNumber);

}
