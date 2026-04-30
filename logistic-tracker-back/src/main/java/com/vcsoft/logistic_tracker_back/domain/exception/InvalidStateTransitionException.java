package com.vcsoft.logistic_tracker_back.domain.exception;

import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;

/**
 * Thrown when a forbidden state transition is attempted.
 * Lives in the domain layer — no framework dependencies.
 */
public class InvalidStateTransitionException extends RuntimeException {

    public InvalidStateTransitionException(PackageStatus from, PackageStatus to) {
        super(String.format("Transition from %s to %s is not allowed.", from, to));
    }
}
