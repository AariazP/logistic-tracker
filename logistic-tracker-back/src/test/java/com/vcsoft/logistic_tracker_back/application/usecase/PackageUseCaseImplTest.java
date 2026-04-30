package com.vcsoft.logistic_tracker_back.application.usecase;

import com.vcsoft.logistic_tracker_back.application.port.output.PackageRepository;
import com.vcsoft.logistic_tracker_back.domain.exception.InvalidStateTransitionException;
import com.vcsoft.logistic_tracker_back.domain.exception.PackageNotFoundException;
import com.vcsoft.logistic_tracker_back.domain.model.Package;
import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PackageUseCase Tests")
class PackageUseCaseImplTest {

    @Mock
    private PackageRepository packageRepository;

    @InjectMocks
    private PackageUseCaseImpl useCase;

    private Package existingPackage;

    @BeforeEach
    void setUp() {
        existingPackage = Package.create("TRK-100", 3.0, "20x20x20", "Alice");
    }

    // ── createPackage ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("createPackage: success when trackingId is unique")
    void createPackage_success() {
        when(packageRepository.existsByTrackingId("TRK-100")).thenReturn(false);
        when(packageRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Package result = useCase.createPackage("TRK-100", 3.0, "20x20x20", "Alice");

        assertThat(result.getTrackingId()).isEqualTo("TRK-100");
        assertThat(result.getStatus()).isEqualTo(PackageStatus.RECEIVED);
        verify(packageRepository).save(any());
    }

    @Test
    @DisplayName("createPackage: throws when trackingId already exists")
    void createPackage_duplicateTrackingId() {
        when(packageRepository.existsByTrackingId("TRK-100")).thenReturn(true);

        assertThatThrownBy(() -> useCase.createPackage("TRK-100", 3.0, "20x20x20", "Alice"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("TRK-100");

        verify(packageRepository, never()).save(any());
    }

    // ── listPackages ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("listPackages: returns all when no filter")
    void listPackages_noFilter() {
        when(packageRepository.findAll()).thenReturn(List.of(existingPackage));

        List<Package> result = useCase.listPackages(null);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("listPackages: filters by status")
    void listPackages_withStatusFilter() {
        when(packageRepository.findAllByStatus(PackageStatus.RECEIVED)).thenReturn(List.of(existingPackage));

        List<Package> result = useCase.listPackages(PackageStatus.RECEIVED);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(PackageStatus.RECEIVED);
    }

    // ── updateStatus ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("updateStatus: RECEIVED → IN_TRANSIT succeeds")
    void updateStatus_receivedToInTransit() {
        when(packageRepository.findByTrackingId("TRK-100")).thenReturn(Optional.of(existingPackage));
        when(packageRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Package result = useCase.updateStatus("TRK-100", PackageStatus.IN_TRANSIT);

        assertThat(result.getStatus()).isEqualTo(PackageStatus.IN_TRANSIT);
    }

    @Test
    @DisplayName("updateStatus: RECEIVED → DELIVERED must throw InvalidStateTransitionException")
    void updateStatus_receivedToDelivered_throws() {
        when(packageRepository.findByTrackingId("TRK-100")).thenReturn(Optional.of(existingPackage));

        assertThatThrownBy(() -> useCase.updateStatus("TRK-100", PackageStatus.DELIVERED))
                .isInstanceOf(InvalidStateTransitionException.class);

        verify(packageRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateStatus: throws PackageNotFoundException when tracking ID missing")
    void updateStatus_notFound() {
        when(packageRepository.findByTrackingId("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.updateStatus("UNKNOWN", PackageStatus.IN_TRANSIT))
                .isInstanceOf(PackageNotFoundException.class)
                .hasMessageContaining("UNKNOWN");
    }

    @Test
    @DisplayName("updateStatus: full valid path RECEIVED → IN_TRANSIT → DELIVERED")
    void updateStatus_fullLifecycle() {
        when(packageRepository.findByTrackingId("TRK-100")).thenReturn(Optional.of(existingPackage));
        when(packageRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        useCase.updateStatus("TRK-100", PackageStatus.IN_TRANSIT);

        // existingPackage is now IN_TRANSIT (mutated in-domain)
        Package result = useCase.updateStatus("TRK-100", PackageStatus.DELIVERED);

        assertThat(result.getStatus()).isEqualTo(PackageStatus.DELIVERED);
    }
}
