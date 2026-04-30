package com.vcsoft.logistic_tracker_back.application.port.input;

import com.vcsoft.logistic_tracker_back.domain.model.Package;

import java.util.UUID;

public interface CreatePackageUseCase {
    Package createPackage(String trackingId, double weight, String dimensions, UUID recipientId);
}