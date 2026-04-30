package com.vcsoft.logistic_tracker_back.infrastructure.persistence.mapper;

import com.vcsoft.logistic_tracker_back.domain.model.Package;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.entity.PackageEntity;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.entity.RecipientEntity;
import org.springframework.stereotype.Component;

/**
 * Maps between domain aggregate (Package) and JPA entity (PackageEntity).
 * Single responsibility: data transformation only.
 */
@Component
public class PackagePersistenceMapper {

    public PackageEntity toEntity(Package pkg, RecipientEntity recipientEntity) {
        return new PackageEntity(
                pkg.getId(),
                pkg.getTrackingId(),
                pkg.getWeight(),
                pkg.getDimensions(),
                recipientEntity,
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
                entity.getRecipient().getId(),
                entity.getRecipient().getName(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
