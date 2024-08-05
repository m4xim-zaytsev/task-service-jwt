package com.example.task_service_jwt.repository;

import com.example.task_service_jwt.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUserId(Long userId);
}
