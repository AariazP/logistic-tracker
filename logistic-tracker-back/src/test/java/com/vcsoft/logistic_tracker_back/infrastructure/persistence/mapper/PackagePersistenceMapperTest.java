package com.vcsoft.logistic_tracker_back.infrastructure.persistence.mapper;

import com.vcsoft.logistic_tracker_back.domain.model.Package;
import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.entity.PackageEntity;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.entity.RecipientEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("PackagePersistenceMapper tests")
class PackagePersistenceMapperTest {

    private final PackagePersistenceMapper mapper = new PackagePersistenceMapper();

    @Test
    @DisplayName("toEntity and toDomain map fields")
    void toEntity_toDomain_mapFields() {
        UUID packageId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        Instant now = Instant.now();

        Package pkg = Package.reconstitute(packageId, "TRK-1", 2.4, "10x10x10", recipientId, "John", PackageStatus.RECEIVED, now, now);
        RecipientEntity recipient = new RecipientEntity(recipientId, "John", "john@mail.com", "123", "Addr", "DOC", now, now);

        PackageEntity entity = mapper.toEntity(pkg, recipient);

        assertThat(entity.getId()).isEqualTo(packageId);
        assertThat(entity.getTrackingId()).isEqualTo("TRK-1");
        assertThat(entity.getRecipient()).isEqualTo(recipient);

        Package domain = mapper.toDomain(entity);

        assertThat(domain.getId()).isEqualTo(packageId);
        assertThat(domain.getRecipientId()).isEqualTo(recipientId);
        assertThat(domain.getRecipientName()).isEqualTo("John");
        assertThat(domain.getStatus()).isEqualTo(PackageStatus.RECEIVED);
    }

    @Test
    @DisplayName("toDomain fails when recipient is null")
    void toDomain_failsWhenRecipientMissing() {
        Instant now = Instant.now();
        PackageEntity entity = new PackageEntity(UUID.randomUUID(), "TRK-ERR", 1.0, "X", null, PackageStatus.RECEIVED, now, now);

        assertThatThrownBy(() -> mapper.toDomain(entity))
                .isInstanceOf(NullPointerException.class);
    }
}
