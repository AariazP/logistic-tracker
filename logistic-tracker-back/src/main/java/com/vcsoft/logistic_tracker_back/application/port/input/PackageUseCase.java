package com.vcsoft.logistic_tracker_back.application.port.input;

import com.vcsoft.logistic_tracker_back.domain.model.Package;
import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;

import java.util.List;

public interface PackageUseCase {
    Package createPackage(String trackingId, double weight, String dimensions, String recipientName);
    List<Package> listPackages(PackageStatus statusFilter);
    Package updateStatus(String trackingId, PackageStatus newStatus);
}
