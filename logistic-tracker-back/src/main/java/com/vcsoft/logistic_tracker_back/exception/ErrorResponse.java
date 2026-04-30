package com.vcsoft.logistic_tracker_back.exception;

import java.time.Instant;
import java.util.Map;

/**
 * Standardized error response envelope.
 */
public record ErrorResponse(
        int status,
        String error,
        String message,
        Instant timestamp,
        Map<String, String> fieldErrors
) {
    public static ErrorResponse of(int status, String error, String message) {
        return new ErrorResponse(status, error, message, Instant.now(), null);
    }

    public static ErrorResponse withFields(int status, String error, String message,
                                           Map<String, String> fieldErrors) {
        return new ErrorResponse(status, error, message, Instant.now(), fieldErrors);
    }
}
