package com.vcsoft.logistic_tracker_back.domain.port.out;

import com.vcsoft.logistic_tracker_back.domain.model.Recipient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecipientRepository {
    Recipient save(Recipient recipient);
    Optional<Recipient> findById(UUID recipientId);
    List<Recipient> findAll();
    void delete(Recipient recipient);
}