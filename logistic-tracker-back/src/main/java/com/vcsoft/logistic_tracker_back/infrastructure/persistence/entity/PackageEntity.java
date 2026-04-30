package com.vcsoft.logistic_tracker_back.infrastructure.persistence.entity;

import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * JPA entity — infrastructure concern only.
 * Never exposed beyond the persistence adapter.
 */
@Entity
@Table(
    name = "packages",
    indexes = {
        @Index(name = "idx_packages_status", columnList = "status"),
        @Index(name = "idx_packages_tracking_id", columnList = "tracking_id", unique = true)
    }
)
public class PackageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "tracking_id", nullable = false, unique = true, length = 100)
    private String trackingId;

    @Column(nullable = false)
    private double weight;

    @Column(nullable = false, length = 255)
    private String dimensions;

    @Column(name = "recipient_name", nullable = false, length = 255)
    private String recipientName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PackageStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // ── JPA required no-arg constructor ──────────────────────────────────────
    protected PackageEntity() {}

    // ── Full constructor ──────────────────────────────────────────────────────
    public PackageEntity(UUID id, String trackingId, double weight, String dimensions,
                         String recipientName, PackageStatus status,
                         Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.trackingId = trackingId;
        this.weight = weight;
        this.dimensions = dimensions;
        this.recipientName = recipientName;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ── Getters / Setters ────────────────────────────────────────────────────
    public UUID getId() { return id; }
    public String getTrackingId() { return trackingId; }
    public double getWeight() { return weight; }
    public String getDimensions() { return dimensions; }
    public String getRecipientName() { return recipientName; }
    public PackageStatus getStatus() { return status; }
    public void setStatus(PackageStatus status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
