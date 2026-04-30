package com.vcsoft.logistic_tracker_back.application.dto.response;

import com.vcsoft.logistic_tracker_back.domain.model.UserRole;

import java.time.Instant;
import java.util.UUID;

public record DriverResponse(
        UUID id,
        String username,
        UserRole role,
        Instant createdAt
) {
}