package com.ys.hsm.auth.repository;

import com.ys.hsm.auth.entity.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {


    Optional<RefreshToken> findByToken(String token);

    void deleteByUserId(String userId);
}
