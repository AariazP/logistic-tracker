package com.vcsoft.logistic_tracker_back.dto.response;

import com.vcsoft.logistic_tracker_back.domain.model.Recipient;
import org.springframework.stereotype.Component;

@Component
public class RecipientResponseMapper {

    public RecipientResponse toResponse(Recipient recipient) {
        return new RecipientResponse(
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
}