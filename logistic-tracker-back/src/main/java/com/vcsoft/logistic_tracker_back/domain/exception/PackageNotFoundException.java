package com.vcsoft.logistic_tracker_back.domain.exception;

/**
 * Thrown when a package is not found in the system.
 */
public class PackageNotFoundException extends RuntimeException {

    public PackageNotFoundException(String trackingId) {
        super(String.format("Package with tracking ID '%s' not found.", trackingId));
    }
}
