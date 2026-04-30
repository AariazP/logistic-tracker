package com.vcsoft.logistic_tracker_back.infrastructure.persistence.adapter;

import com.vcsoft.logistic_tracker_back.application.port.output.PackageRepository;
import com.vcsoft.logistic_tracker_back.domain.model.Package;
import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.entity.PackageEntity;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.mapper.PackagePersistenceMapper;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.repository.PackageJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Implements the output port by delegating to Spring Data JPA.
 * Translates between domain model and JPA entity.
 */
@Component
public class PackageRepositoryAdapter implements PackageRepository {

    private final PackageJpaRepository jpaRepository;
    private final PackagePersistenceMapper mapper;

    public PackageRepositoryAdapter(PackageJpaRepository jpaRepository,
                                    PackagePersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Package save(Package pkg) {
        PackageEntity saved = jpaRepository.save(mapper.toEntity(pkg));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Package> findByTrackingId(String trackingId) {
        return jpaRepository.findByTrackingId(trackingId).map(mapper::toDomain);
    }

    @Override
    public boolean existsByTrackingId(String trackingId) {
        return jpaRepository.existsByTrackingId(trackingId);
    }

    @Override
    public List<Package> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Package> findAllByStatus(PackageStatus status) {
        return jpaRepository.findAllByStatus(status).stream().map(mapper::toDomain).toList();
    }
}
