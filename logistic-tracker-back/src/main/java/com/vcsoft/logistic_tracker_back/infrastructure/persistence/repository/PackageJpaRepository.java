package com.vcsoft.logistic_tracker_back.infrastructure.persistence.repository;

import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PackageJpaRepository extends JpaRepository<PackageEntity, UUID> {

    Optional<PackageEntity> findByTrackingId(String trackingId);

    boolean existsByTrackingId(String trackingId);

    List<PackageEntity> findAllByStatus(PackageStatus status);
}
