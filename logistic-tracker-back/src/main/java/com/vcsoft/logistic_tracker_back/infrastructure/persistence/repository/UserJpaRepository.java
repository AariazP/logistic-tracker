package com.vcsoft.logistic_tracker_back.infrastructure.persistence.repository;

import com.vcsoft.logistic_tracker_back.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);
}
