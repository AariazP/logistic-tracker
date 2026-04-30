package com.vcsoft.logistic_tracker_back.application.usecase.packages;

import com.vcsoft.logistic_tracker_back.domain.model.Package;
import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;

public interface UpdatePackageStatusUseCase {
    Package updateStatus(String trackingId, PackageStatus newStatus);
}