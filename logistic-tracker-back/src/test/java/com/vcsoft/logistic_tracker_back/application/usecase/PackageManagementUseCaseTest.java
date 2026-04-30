package com.vcsoft.logistic_tracker_back.application.usecase;

import com.vcsoft.logistic_tracker_back.application.port.output.PackageRepository;
import com.vcsoft.logistic_tracker_back.application.port.output.RecipientRepository;
import com.vcsoft.logistic_tracker_back.domain.exception.InvalidStateTransitionException;
import com.vcsoft.logistic_tracker_back.domain.exception.PackageNotFoundException;
import com.vcsoft.logistic_tracker_back.domain.exception.RecipientNotFoundException;
import com.vcsoft.logistic_tracker_back.domain.model.Package;
import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;
import com.vcsoft.logistic_tracker_back.domain.model.Recipient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Package management use case tests")
class PackageManagementUseCaseTest {

    @Mock
    private PackageRepository packageRepository;

    @Mock
    private RecipientRepository recipientRepository;

    @InjectMocks
    private CreatePackageUseCaseImpl createPackageUseCase;

    @InjectMocks
    private ListPackagesUseCaseImpl listPackagesUseCase;

    @InjectMocks
    private GetPackageByTrackingIdUseCaseImpl getPackageByTrackingIdUseCase;

    @InjectMocks
    private UpdatePackageStatusUseCaseImpl updatePackageStatusUseCase;

    private Recipient recipient;
    private Package existingPackage;

    @BeforeEach
    void setUp() {
        recipient = Recipient.create("Alice", "alice@example.com", "123456", "Street 123", "DOC-1");
        existingPackage = Package.create("TRK-100", 3.0, "20x20x20", recipient.getId(), recipient.getName());
    }

    @Test
    @DisplayName("createPackage: success when trackingId is unique and recipient exists")
    void createPackage_success() {
        when(packageRepository.existsByTrackingId("TRK-100")).thenReturn(false);
        when(recipientRepository.findById(recipient.getId())).thenReturn(Optional.of(recipient));
        when(packageRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Package result = createPackageUseCase.createPackage("TRK-100", 3.0, "20x20x20", recipient.getId());

        assertThat(result.getTrackingId()).isEqualTo("TRK-100");
        assertThat(result.getRecipientId()).isEqualTo(recipient.getId());
        assertThat(result.getStatus()).isEqualTo(PackageStatus.RECEIVED);
    }

    @Test
    @DisplayName("createPackage: throws when trackingId already exists")
    void createPackage_duplicateTrackingId() {
        when(packageRepository.existsByTrackingId("TRK-100")).thenReturn(true);

        assertThatThrownBy(() -> createPackageUseCase.createPackage("TRK-100", 3.0, "20x20x20", recipient.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("TRK-100");

        verify(packageRepository, never()).save(any());
    }

    @Test
    @DisplayName("createPackage: throws when recipient does not exist")
    void createPackage_missingRecipient() {
        UUID missingRecipientId = UUID.randomUUID();
        when(packageRepository.existsByTrackingId("TRK-100")).thenReturn(false);
        when(recipientRepository.findById(missingRecipientId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> createPackageUseCase.createPackage("TRK-100", 3.0, "20x20x20", missingRecipientId))
                .isInstanceOf(RecipientNotFoundException.class);
    }

    @Test
    @DisplayName("listPackages: returns all when no filter")
    void listPackages_noFilter() {
        when(packageRepository.findAll()).thenReturn(List.of(existingPackage));

        List<Package> result = listPackagesUseCase.listPackages(null);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("listPackages: filters by status")
    void listPackages_withStatusFilter() {
        when(packageRepository.findAllByStatus(PackageStatus.RECEIVED)).thenReturn(List.of(existingPackage));

        List<Package> result = listPackagesUseCase.listPackages(PackageStatus.RECEIVED);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(PackageStatus.RECEIVED);
    }

    @Test
    @DisplayName("getByTrackingId: throws when tracking ID missing")
    void getByTrackingId_notFound() {
        when(packageRepository.findByTrackingId("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getPackageByTrackingIdUseCase.getByTrackingId("UNKNOWN"))
                .isInstanceOf(PackageNotFoundException.class);
    }

    @Test
    @DisplayName("updateStatus: RECEIVED to IN_TRANSIT succeeds")
    void updateStatus_receivedToInTransit() {
        when(packageRepository.findByTrackingId("TRK-100")).thenReturn(Optional.of(existingPackage));
        when(packageRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Package result = updatePackageStatusUseCase.updateStatus("TRK-100", PackageStatus.IN_TRANSIT);

        assertThat(result.getStatus()).isEqualTo(PackageStatus.IN_TRANSIT);
    }

    @Test
    @DisplayName("updateStatus: invalid transition throws")
    void updateStatus_invalidTransition() {
        when(packageRepository.findByTrackingId("TRK-100")).thenReturn(Optional.of(existingPackage));

        assertThatThrownBy(() -> updatePackageStatusUseCase.updateStatus("TRK-100", PackageStatus.DELIVERED))
                .isInstanceOf(InvalidStateTransitionException.class);

        verify(packageRepository, never()).save(any());
    }
}