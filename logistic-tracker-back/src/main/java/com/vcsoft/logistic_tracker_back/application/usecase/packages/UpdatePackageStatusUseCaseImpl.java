package com.vcsoft.logistic_tracker_back.application.usecase.packages;

import com.vcsoft.logistic_tracker_back.application.usecase.packages.UpdatePackageStatusUseCase;
import com.vcsoft.logistic_tracker_back.domain.port.out.PackageRepository;
import com.vcsoft.logistic_tracker_back.domain.exception.PackageNotFoundException;
import com.vcsoft.logistic_tracker_back.domain.model.Package;
import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdatePackageStatusUseCaseImpl implements UpdatePackageStatusUseCase {

    private final PackageRepository packageRepository;

    @Override
    public Package updateStatus(String trackingId, PackageStatus newStatus) {
        Package pkg = packageRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new PackageNotFoundException(trackingId));

        pkg.transitionTo(newStatus);
        return packageRepository.save(pkg);
    }
}