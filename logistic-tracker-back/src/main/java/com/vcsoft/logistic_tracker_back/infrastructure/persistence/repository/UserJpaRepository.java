package com.vcsoft.logistic_tracker_back.infrastructure.persistence.repository;

import com.vcsoft.logistic_tracker_back.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByIdAndRole(UUID userId, String role);
    List<UserEntity> findAllByRole(String role);
    boolean existsByUsername(String username);
    long countByRole(String role);
}
