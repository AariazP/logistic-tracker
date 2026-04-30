package com.vcsoft.logistic_tracker_back.domain.model;

import com.vcsoft.logistic_tracker_back.domain.state.PackageState;
import com.vcsoft.logistic_tracker_back.domain.state.PackageStateFactory;
import com.vcsoft.logistic_tracker_back.domain.state.ReceivedState;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * Pure domain aggregate root. No framework annotations — framework-agnostic
 * business model following Clean Architecture domain layer rules.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Package {

    private final UUID id;
    private final String trackingId;
    private double weight;
    private String dimensions;
    private final UUID recipientId;
    private String recipientName;
    @Getter(AccessLevel.NONE)
    private PackageState state;
    private final Instant createdAt;
    private Instant updatedAt;

    /**
     * Factory method: creates a new package in RECEIVED state.
     */
    public static Package create(String trackingId, double weight, String dimensions,
                                 UUID recipientId, String recipientName) {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        return new Package(id, trackingId, weight, dimensions, recipientId, recipientName, new ReceivedState(), now, now);
    }

    /**
     * Reconstitution constructor: restores a package from persistence.
     */
    public static Package reconstitute(UUID id, String trackingId, double weight,
                                       String dimensions, UUID recipientId, String recipientName,
                                       PackageStatus status, Instant createdAt, Instant updatedAt) {
        return new Package(id, trackingId, weight, dimensions, recipientId, recipientName,
                PackageStateFactory.from(status), createdAt, updatedAt);
    }

    /**
     * Executes a state transition through the State pattern.
     * Business rule enforcement lives in the state classes — no if-else here.
     */
    public void transitionTo(PackageStatus next) {
        this.state = state.transition(next);
        this.updatedAt = Instant.now();
    }

    public PackageStatus getStatus() { return state.getStatus(); }
}
