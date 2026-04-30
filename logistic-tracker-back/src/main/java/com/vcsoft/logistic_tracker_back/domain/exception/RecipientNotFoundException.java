package com.vcsoft.logistic_tracker_back.domain.exception;

import java.util.UUID;

public class RecipientNotFoundException extends RuntimeException {
    public RecipientNotFoundException(UUID recipientId) {
        super("Recipient not found: " + recipientId);
    }
}