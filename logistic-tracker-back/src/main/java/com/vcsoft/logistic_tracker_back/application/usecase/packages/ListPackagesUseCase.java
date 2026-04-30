package com.vcsoft.logistic_tracker_back.application.usecase.packages;

import com.vcsoft.logistic_tracker_back.domain.model.Package;
import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;

import java.util.List;

public interface ListPackagesUseCase {
    List<Package> listPackages(PackageStatus statusFilter);
}