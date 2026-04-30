package com.vcsoft.logistic_tracker_back.application.port.input;

import com.vcsoft.logistic_tracker_back.domain.model.Package;
import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;

public interface UpdatePackageStatusUseCase {
    Package updateStatus(String trackingId, PackageStatus newStatus);
}