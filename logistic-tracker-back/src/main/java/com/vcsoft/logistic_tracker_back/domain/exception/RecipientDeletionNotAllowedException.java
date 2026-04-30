package com.vcsoft.logistic_tracker_back.domain.exception;

import java.util.UUID;

public class RecipientDeletionNotAllowedException extends RuntimeException {
    public RecipientDeletionNotAllowedException(UUID recipientId) {
        super("Recipient " + recipientId + " cannot be deleted because it has active packages.");
    }
}