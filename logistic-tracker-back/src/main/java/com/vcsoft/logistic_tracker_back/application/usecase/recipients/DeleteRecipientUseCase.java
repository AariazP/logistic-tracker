package com.vcsoft.logistic_tracker_back.application.usecase.recipients;

import java.util.UUID;

public interface DeleteRecipientUseCase {
    void deleteRecipient(UUID recipientId);
}