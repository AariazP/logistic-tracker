package com.vcsoft.logistic_tracker_back.domain.port.out;

import com.vcsoft.logistic_tracker_back.domain.model.Package;
import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port: the use-case layer depends on this interface.
 * Infrastructure provides the implementation (Adapter pattern).
 */
public interface PackageRepository {

    Package save(Package pkg);

    Optional<Package> findByTrackingId(String trackingId);

    boolean existsByTrackingId(String trackingId);

    boolean existsActiveByRecipientId(UUID recipientId);

    List<Package> findAll();

    List<Package> findAllByStatus(PackageStatus status);
}
