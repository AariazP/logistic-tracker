package com.vcsoft.logistic_tracker_back.application.usecase.packages;

import com.vcsoft.logistic_tracker_back.application.usecase.packages.GetPackageByTrackingIdUseCase;
import com.vcsoft.logistic_tracker_back.domain.port.out.PackageRepository;
import com.vcsoft.logistic_tracker_back.domain.exception.PackageNotFoundException;
import com.vcsoft.logistic_tracker_back.domain.model.Package;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetPackageByTrackingIdUseCaseImpl implements GetPackageByTrackingIdUseCase {

    private final PackageRepository packageRepository;

    public GetPackageByTrackingIdUseCaseImpl(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Override
    public Package getByTrackingId(String trackingId) {
        return packageRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new PackageNotFoundException(trackingId));
    }
}