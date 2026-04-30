package com.vcsoft.logistic_tracker_back.application.usecase.packages;

import com.vcsoft.logistic_tracker_back.application.usecase.packages.CreatePackageUseCase;
import com.vcsoft.logistic_tracker_back.domain.port.out.PackageRepository;
import com.vcsoft.logistic_tracker_back.domain.port.out.RecipientRepository;
import com.vcsoft.logistic_tracker_back.domain.exception.RecipientNotFoundException;
import com.vcsoft.logistic_tracker_back.domain.model.Package;
import com.vcsoft.logistic_tracker_back.domain.model.Recipient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class CreatePackageUseCaseImpl implements CreatePackageUseCase {

    private final PackageRepository packageRepository;
    private final RecipientRepository recipientRepository;

    public CreatePackageUseCaseImpl(PackageRepository packageRepository,
                                    RecipientRepository recipientRepository) {
        this.packageRepository = packageRepository;
        this.recipientRepository = recipientRepository;
    }

    @Override
    public Package createPackage(String trackingId, double weight, String dimensions, UUID recipientId) {
        if (packageRepository.existsByTrackingId(trackingId)) {
            throw new IllegalArgumentException(
                    "Package with trackingId '" + trackingId + "' already exists.");
        }

        Recipient recipient = recipientRepository.findById(recipientId)
                .orElseThrow(() -> new RecipientNotFoundException(recipientId));

        Package pkg = Package.create(trackingId, weight, dimensions, recipient.getId(), recipient.getName());
        return packageRepository.save(pkg);
    }
}