package com.vcsoft.logistic_tracker_back.infrastructure.persistence.adapter;

import com.vcsoft.logistic_tracker_back.domain.model.Package;
import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.entity.PackageEntity;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.entity.RecipientEntity;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.mapper.PackagePersistenceMapper;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.repository.PackageJpaRepository;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.repository.RecipientJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PackageRepositoryAdapter tests")
class PackageRepositoryAdapterTest {

    @Mock
    private PackageJpaRepository jpaRepository;

    @Mock
    private RecipientJpaRepository recipientJpaRepository;

    @Mock
    private PackagePersistenceMapper mapper;

    @InjectMocks
    private PackageRepositoryAdapter adapter;

    @Test
    @DisplayName("save maps domain to entity and back")
    void save_mapsAndPersists() {
        UUID recipientId = UUID.randomUUID();
        UUID packageId = UUID.randomUUID();
        Instant now = Instant.now();

        Package pkg = Package.reconstitute(
                packageId,
                "TRK-100",
                4.2,
                "20x20x20",
                recipientId,
                "Jane",
                PackageStatus.RECEIVED,
                now,
                now
        );

        RecipientEntity recipientRef = new RecipientEntity(recipientId, "Jane", "jane@mail.com", "123", "Street", "DOC1", now, now);
        PackageEntity toSave = new PackageEntity(packageId, "TRK-100", 4.2, "20x20x20", recipientRef, PackageStatus.RECEIVED, now, now);
        PackageEntity saved = new PackageEntity(packageId, "TRK-100", 4.2, "20x20x20", recipientRef, PackageStatus.RECEIVED, now, now);
        Package mappedBack = Package.reconstitute(packageId, "TRK-100", 4.2, "20x20x20", recipientId, "Jane", PackageStatus.RECEIVED, now, now);

        when(recipientJpaRepository.getReferenceById(recipientId)).thenReturn(recipientRef);
        when(mapper.toEntity(pkg, recipientRef)).thenReturn(toSave);
        when(jpaRepository.save(toSave)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(mappedBack);

        Package result = adapter.save(pkg);

        assertThat(result).isSameAs(mappedBack);
        verify(recipientJpaRepository).getReferenceById(recipientId);
    }

    @Test
    @DisplayName("findByTrackingId maps present result")
    void findByTrackingId_mapsWhenPresent() {
        UUID recipientId = UUID.randomUUID();
        UUID packageId = UUID.randomUUID();
        Instant now = Instant.now();

        RecipientEntity recipient = new RecipientEntity(recipientId, "Jane", "jane@mail.com", "123", "Street", "DOC1", now, now);
        PackageEntity entity = new PackageEntity(packageId, "TRK-1", 1.0, "10x10x10", recipient, PackageStatus.RECEIVED, now, now);
        Package mapped = Package.reconstitute(packageId, "TRK-1", 1.0, "10x10x10", recipientId, "Jane", PackageStatus.RECEIVED, now, now);

        when(jpaRepository.findByTrackingId("TRK-1")).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(mapped);

        Optional<Package> result = adapter.findByTrackingId("TRK-1");

        assertThat(result).contains(mapped);
    }

    @Test
    @DisplayName("findByTrackingId returns empty when not found")
    void findByTrackingId_returnsEmptyWhenMissing() {
        when(jpaRepository.findByTrackingId("MISSING")).thenReturn(Optional.empty());

        Optional<Package> result = adapter.findByTrackingId("MISSING");

        assertThat(result).isEmpty();
        verify(mapper, never()).toDomain(org.mockito.ArgumentMatchers.any());
    }

    @Test
    @DisplayName("exists delegates to jpa")
    void exists_delegates() {
        UUID recipientId = UUID.randomUUID();
        when(jpaRepository.existsByTrackingId("TRK-X")).thenReturn(true);
        when(jpaRepository.existsByRecipient_IdAndStatusNot(recipientId, PackageStatus.DELIVERED)).thenReturn(false);

        assertThat(adapter.existsByTrackingId("TRK-X")).isTrue();
        assertThat(adapter.existsActiveByRecipientId(recipientId)).isFalse();
    }

    @Test
    @DisplayName("findAll and findAllByStatus map each item")
    void findAll_variantsMapItems() {
        UUID recipientId = UUID.randomUUID();
        Instant now = Instant.now();
        RecipientEntity recipient = new RecipientEntity(recipientId, "R", "r@mail.com", "123", "Addr", "DOC", now, now);

        PackageEntity e1 = new PackageEntity(UUID.randomUUID(), "TRK-A", 2.0, "1", recipient, PackageStatus.RECEIVED, now, now);
        PackageEntity e2 = new PackageEntity(UUID.randomUUID(), "TRK-B", 3.0, "2", recipient, PackageStatus.IN_TRANSIT, now, now);

        Package p1 = Package.reconstitute(e1.getId(), "TRK-A", 2.0, "1", recipientId, "R", PackageStatus.RECEIVED, now, now);
        Package p2 = Package.reconstitute(e2.getId(), "TRK-B", 3.0, "2", recipientId, "R", PackageStatus.IN_TRANSIT, now, now);

        when(jpaRepository.findAll()).thenReturn(List.of(e1, e2));
        when(jpaRepository.findAllByStatus(PackageStatus.IN_TRANSIT)).thenReturn(List.of(e2));
        when(mapper.toDomain(e1)).thenReturn(p1);
        when(mapper.toDomain(e2)).thenReturn(p2);

        assertThat(adapter.findAll()).containsExactly(p1, p2);
        assertThat(adapter.findAllByStatus(PackageStatus.IN_TRANSIT)).containsExactly(p2);
    }
}
