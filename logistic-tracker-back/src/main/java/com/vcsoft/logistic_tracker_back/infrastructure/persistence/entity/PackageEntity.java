package com.vcsoft.logistic_tracker_back.infrastructure.persistence.entity;

import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_id", nullable = false)
    private RecipientEntity recipient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Setter
    private PackageStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    @Setter
    private Instant updatedAt;
}
