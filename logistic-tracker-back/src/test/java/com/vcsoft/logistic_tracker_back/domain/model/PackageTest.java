package com.vcsoft.logistic_tracker_back.domain.model;

import com.vcsoft.logistic_tracker_back.domain.exception.InvalidStateTransitionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Package Domain Tests")
class PackageTest {

    private Package newPackage() {
        return Package.create("TRK-001", 2.5, "30x20x10", "John Doe");
    }

    // ── Creation ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("New package should have RECEIVED status")
    void shouldCreateWithReceivedStatus() {
        Package pkg = newPackage();
        assertThat(pkg.getStatus()).isEqualTo(PackageStatus.RECEIVED);
    }

    @Test
    @DisplayName("New package should have non-null ID and timestamps")
    void shouldCreateWithIdAndTimestamps() {
        Package pkg = newPackage();
        assertThat(pkg.getId()).isNotNull();
        assertThat(pkg.getCreatedAt()).isNotNull();
        assertThat(pkg.getUpdatedAt()).isNotNull();
    }

    // ── Valid transitions ─────────────────────────────────────────────────────

    @Test
    @DisplayName("RECEIVED → IN_TRANSIT should be valid")
    void shouldTransitionFromReceivedToInTransit() {
        Package pkg = newPackage();
        pkg.transitionTo(PackageStatus.IN_TRANSIT);
        assertThat(pkg.getStatus()).isEqualTo(PackageStatus.IN_TRANSIT);
    }

    @Test
    @DisplayName("IN_TRANSIT → DELIVERED should be valid")
    void shouldTransitionFromInTransitToDelivered() {
        Package pkg = newPackage();
        pkg.transitionTo(PackageStatus.IN_TRANSIT);
        pkg.transitionTo(PackageStatus.DELIVERED);
        assertThat(pkg.getStatus()).isEqualTo(PackageStatus.DELIVERED);
    }

    @Test
    @DisplayName("updatedAt should change after transition")
    void shouldUpdateTimestampOnTransition() throws InterruptedException {
        Package pkg = newPackage();
        var before = pkg.getUpdatedAt();
        Thread.sleep(5); // ensure time advances
        pkg.transitionTo(PackageStatus.IN_TRANSIT);
        assertThat(pkg.getUpdatedAt()).isAfter(before);
    }

    // ── Invalid transitions ───────────────────────────────────────────────────

    @Test
    @DisplayName("RECEIVED → DELIVERED must be forbidden")
    void shouldForbidReceivedToDelivered() {
        Package pkg = newPackage();
        assertThatThrownBy(() -> pkg.transitionTo(PackageStatus.DELIVERED))
                .isInstanceOf(InvalidStateTransitionException.class)
                .hasMessageContaining("RECEIVED")
                .hasMessageContaining("DELIVERED");
    }

    @Test
    @DisplayName("DELIVERED → any state must be forbidden (terminal state)")
    void shouldForbidAnyTransitionFromDelivered() {
        Package pkg = newPackage();
        pkg.transitionTo(PackageStatus.IN_TRANSIT);
        pkg.transitionTo(PackageStatus.DELIVERED);

        assertThatThrownBy(() -> pkg.transitionTo(PackageStatus.RECEIVED))
                .isInstanceOf(InvalidStateTransitionException.class);
        assertThatThrownBy(() -> pkg.transitionTo(PackageStatus.IN_TRANSIT))
                .isInstanceOf(InvalidStateTransitionException.class);
    }

    @Test
    @DisplayName("IN_TRANSIT → RECEIVED must be forbidden (backward transition)")
    void shouldForbidBackwardTransition() {
        Package pkg = newPackage();
        pkg.transitionTo(PackageStatus.IN_TRANSIT);
        assertThatThrownBy(() -> pkg.transitionTo(PackageStatus.RECEIVED))
                .isInstanceOf(InvalidStateTransitionException.class);
    }

    @Test
    @DisplayName("RECEIVED → RECEIVED (self-transition) must be forbidden")
    void shouldForbidSelfTransitionOnReceived() {
        Package pkg = newPackage();
        assertThatThrownBy(() -> pkg.transitionTo(PackageStatus.RECEIVED))
                .isInstanceOf(InvalidStateTransitionException.class);
    }

    // ── Reconstitution ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Reconstituted package in IN_TRANSIT should allow DELIVERED")
    void shouldReconstituteInTransitAndAllowDelivered() {
        Package pkg = Package.reconstitute(
                java.util.UUID.randomUUID(), "TRK-002", 1.0,
                "10x10x10", "Jane Doe",
                PackageStatus.IN_TRANSIT,
                java.time.Instant.now(), java.time.Instant.now()
        );
        pkg.transitionTo(PackageStatus.DELIVERED);
        assertThat(pkg.getStatus()).isEqualTo(PackageStatus.DELIVERED);
    }
}
