package com.vcsoft.logistic_tracker_back.application.usecase.packages;

import com.vcsoft.logistic_tracker_back.domain.model.Package;

public interface GetPackageByTrackingIdUseCase {
    Package getByTrackingId(String trackingId);
}