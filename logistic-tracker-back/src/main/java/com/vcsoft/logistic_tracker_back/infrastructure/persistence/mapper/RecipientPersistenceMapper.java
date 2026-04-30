package com.vcsoft.logistic_tracker_back.infrastructure.persistence.mapper;

import com.vcsoft.logistic_tracker_back.domain.model.Recipient;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.entity.RecipientEntity;
import org.springframework.stereotype.Component;

@Component
public class RecipientPersistenceMapper {

    public RecipientEntity toEntity(Recipient recipient) {
        return new RecipientEntity(
                recipient.getId(),
                recipient.getName(),
                recipient.getEmail(),
                recipient.getPhone(),
                recipient.getAddress(),
                recipient.getDocumentNumber(),
                recipient.getCreatedAt(),
                recipient.getUpdatedAt()
        );
    }

    public Recipient toDomain(RecipientEntity entity) {
        return Recipient.reconstitute(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getAddress(),
                entity.getDocumentNumber(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}