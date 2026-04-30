package com.vcsoft.logistic_tracker_back.dto.request;

import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for status transition. The target status is the desired next state.
 */
public record UpdatePackageStatusRequest(

        @NotNull(message = "Status must not be null")
        PackageStatus status
) {}
