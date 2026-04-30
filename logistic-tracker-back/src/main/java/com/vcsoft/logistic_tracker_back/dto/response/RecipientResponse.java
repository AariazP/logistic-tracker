package com.vcsoft.logistic_tracker_back.dto.response;

import java.time.Instant;
import java.util.UUID;

public record RecipientResponse(
        UUID id,
        String name,
        String email,
        String phone,
        String address,
        String documentNumber,
        Instant createdAt,
        Instant updatedAt
) {
}