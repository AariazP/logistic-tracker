package com.vcsoft.logistic_tracker_back.application.usecase;

import com.vcsoft.logistic_tracker_back.application.port.input.PackageUseCase;
import com.vcsoft.logistic_tracker_back.application.port.output.PackageRepository;
import com.vcsoft.logistic_tracker_back.domain.exception.PackageNotFoundException;
import com.vcsoft.logistic_tracker_back.domain.model.Package;
import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Application service: orchestrates domain and repository.
 * Business rule enforcement (state transitions) is delegated to the domain.
 * Transaction boundary is at this layer per Clean Architecture.
 */
@Service
@Transactional
public class PackageUseCaseImpl implements PackageUseCase {

    private final PackageRepository packageRepository;

    public PackageUseCaseImpl(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Override
    public Package createPackage(String trackingId, double weight,
                                 String dimensions, String recipientName) {
        if (packageRepository.existsByTrackingId(trackingId)) {
            throw new IllegalArgumentException(
                    "Package with trackingId '" + trackingId + "' already exists.");
        }
        Package pkg = Package.create(trackingId, weight, dimensions, recipientName);
        return packageRepository.save(pkg);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Package> listPackages(PackageStatus statusFilter) {
        if (statusFilter != null) {
            return packageRepository.findAllByStatus(statusFilter);
        }
        return packageRepository.findAll();
    }

    @Override
    public Package updateStatus(String trackingId, PackageStatus newStatus) {
        Package pkg = packageRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new PackageNotFoundException(trackingId));

        pkg.transitionTo(newStatus);   // State pattern enforces rules
        return packageRepository.save(pkg);
    }
}
