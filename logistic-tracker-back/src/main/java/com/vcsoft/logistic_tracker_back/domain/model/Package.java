package com.vcsoft.logistic_tracker_back.domain.model;

import com.vcsoft.logistic_tracker_back.domain.state.PackageState;
import com.vcsoft.logistic_tracker_back.domain.state.PackageStateFactory;
import com.vcsoft.logistic_tracker_back.domain.state.ReceivedState;

import java.time.Instant;
import java.util.UUID;

/**
 * Pure domain aggregate root. No framework annotations — framework-agnostic
 * business model following Clean Architecture domain layer rules.
 */
public class Package {

    private final UUID id;
    private final String trackingId;
    private double weight;
    private String dimensions;
    private String recipientName;
    private PackageState state;
    private final Instant createdAt;
    private Instant updatedAt;

    /**
     * Factory method: creates a new package in RECEIVED state.
     */
    public static Package create(String trackingId, double weight, String dimensions, String recipientName) {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        return new Package(id, trackingId, weight, dimensions, recipientName, new ReceivedState(), now, now);
    }

    /**
     * Reconstitution constructor: restores a package from persistence.
     */
    public static Package reconstitute(UUID id, String trackingId, double weight,
                                       String dimensions, String recipientName,
                                       PackageStatus status, Instant createdAt, Instant updatedAt) {
        return new Package(id, trackingId, weight, dimensions, recipientName,
                PackageStateFactory.from(status), createdAt, updatedAt);
    }

    private Package(UUID id, String trackingId, double weight, String dimensions,
                    String recipientName, PackageState state, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.trackingId = trackingId;
        this.weight = weight;
        this.dimensions = dimensions;
        this.recipientName = recipientName;
        this.state = state;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Executes a state transition through the State pattern.
     * Business rule enforcement lives in the state classes — no if-else here.
     */
    public void transitionTo(PackageStatus next) {
        this.state = state.transition(next);
        this.updatedAt = Instant.now();
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public UUID getId() { return id; }
    public String getTrackingId() { return trackingId; }
    public double getWeight() { return weight; }
    public String getDimensions() { return dimensions; }
    public String getRecipientName() { return recipientName; }
    public PackageStatus getStatus() { return state.getStatus(); }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
