package com.vcsoft.logistic_tracker_back.infrastructure.persistence.mapper;

import com.vcsoft.logistic_tracker_back.domain.model.Package;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.entity.PackageEntity;
import org.springframework.stereotype.Component;

/**
 * Maps between domain aggregate (Package) and JPA entity (PackageEntity).
 * Single responsibility: data transformation only.
 */
@Component
public class PackagePersistenceMapper {

    public PackageEntity toEntity(Package pkg) {
        return new PackageEntity(
                pkg.getId(),
                pkg.getTrackingId(),
                pkg.getWeight(),
                pkg.getDimensions(),
                pkg.getRecipientName(),
                pkg.getStatus(),
                pkg.getCreatedAt(),
                pkg.getUpdatedAt()
        );
    }

    public Package toDomain(PackageEntity entity) {
        return Package.reconstitute(
                entity.getId(),
                entity.getTrackingId(),
                entity.getWeight(),
                entity.getDimensions(),
                entity.getRecipientName(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
