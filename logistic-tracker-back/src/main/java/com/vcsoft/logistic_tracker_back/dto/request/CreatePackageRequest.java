package com.vcsoft.logistic_tracker_back.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Request DTO for package creation. Validated with JSR-303 annotations.
 */
public record CreatePackageRequest(

        @NotBlank(message = "Tracking ID must not be blank")
        @Size(max = 100, message = "Tracking ID must be at most 100 characters")
        String trackingId,

        @Positive(message = "Weight must be greater than 0")
        double weight,

        @NotBlank(message = "Dimensions must not be blank")
        @Size(max = 255)
        String dimensions,

        @NotNull(message = "Recipient ID must not be null")
        UUID recipientId
) {}
