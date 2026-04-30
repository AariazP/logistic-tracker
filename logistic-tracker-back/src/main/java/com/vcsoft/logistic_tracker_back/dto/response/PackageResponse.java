package com.vcsoft.logistic_tracker_back.dto.response;

import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;

import java.time.Instant;
import java.util.UUID;

/**
 * Response DTO for package data. Never exposes the domain entity directly.
 */
public record PackageResponse(
        UUID id,
        String trackingId,
        double weight,
        String dimensions,
        UUID recipientId,
        String recipientName,
        PackageStatus status,
        Instant createdAt,
        Instant updatedAt
) {}
