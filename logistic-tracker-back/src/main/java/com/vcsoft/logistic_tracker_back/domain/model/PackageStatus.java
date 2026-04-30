package com.vcsoft.logistic_tracker_back.domain.model;

/**
 * Enum representing all valid package lifecycle states.
 * Ordering reflects the allowed forward-only progression.
 */
public enum PackageStatus {
    RECEIVED,
    IN_TRANSIT,
    DELIVERED
}
