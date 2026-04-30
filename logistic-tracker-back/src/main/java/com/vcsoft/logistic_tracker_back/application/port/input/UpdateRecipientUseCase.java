package com.vcsoft.logistic_tracker_back.application.port.input;

import com.vcsoft.logistic_tracker_back.domain.model.Recipient;

import java.util.UUID;

public interface UpdateRecipientUseCase {
    Recipient updateRecipient(UUID recipientId, String name, String email, String phone,
                              String address, String documentNumber);
}