package com.vcsoft.logistic_tracker_back.infrastructure.persistence.repository;

import com.vcsoft.logistic_tracker_back.infrastructure.persistence.entity.RecipientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecipientJpaRepository extends JpaRepository<RecipientEntity, UUID> {
}