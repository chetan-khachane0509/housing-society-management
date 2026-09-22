package com.ys.hsm.complaint.repository;

import com.ys.hsm.complaint.entity.Counter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounterRepository extends MongoRepository<Counter, String> {
}