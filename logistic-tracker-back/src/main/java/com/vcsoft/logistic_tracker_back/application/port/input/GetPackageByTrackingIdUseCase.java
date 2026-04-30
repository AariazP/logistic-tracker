package com.vcsoft.logistic_tracker_back.application.port.input;

import com.vcsoft.logistic_tracker_back.domain.model.Package;

public interface GetPackageByTrackingIdUseCase {
    Package getByTrackingId(String trackingId);
}