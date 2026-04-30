package com.vcsoft.logistic_tracker_back.adapter.mapper;

import com.vcsoft.logistic_tracker_back.application.dto.response.PackageResponse;
import com.vcsoft.logistic_tracker_back.domain.model.Package;
import org.springframework.stereotype.Component;

/**
 * Maps domain Package to PackageResponse DTO.
 * Keeps mapping logic in one place (SRP).
 */
@Component
public class PackageResponseMapper {

    public PackageResponse toResponse(Package pkg) {
        return new PackageResponse(
                pkg.getId(),
                pkg.getTrackingId(),
                pkg.getWeight(),
                pkg.getDimensions(),
                pkg.getRecipientId(),
                pkg.getRecipientName(),
                pkg.getStatus(),
                pkg.getCreatedAt(),
                pkg.getUpdatedAt()
        );
    }
}
