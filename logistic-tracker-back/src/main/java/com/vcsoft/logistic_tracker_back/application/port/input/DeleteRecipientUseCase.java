package com.vcsoft.logistic_tracker_back.application.port.input;

import java.util.UUID;

public interface DeleteRecipientUseCase {
    void deleteRecipient(UUID recipientId);
}