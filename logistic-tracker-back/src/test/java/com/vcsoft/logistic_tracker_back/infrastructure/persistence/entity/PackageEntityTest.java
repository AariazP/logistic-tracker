package com.vcsoft.logistic_tracker_back.infrastructure.persistence.entity;

import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PackageEntity tests")
class PackageEntityTest {

    @Test
    @DisplayName("constructor, getters and mutable fields")
    void constructorGettersAndSetters() {
        UUID packageId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        Instant createdAt = Instant.now();
        Instant updatedAt = createdAt.plusSeconds(5);
        RecipientEntity recipient = new RecipientEntity(recipientId, "R", "r@mail.com", "123", "Addr", "DOC", createdAt, updatedAt);

        PackageEntity entity = new PackageEntity(
                packageId,
                "TRK-10",
                9.5,
                "10x20x30",
                recipient,
                PackageStatus.RECEIVED,
                createdAt,
                updatedAt
        );

        assertThat(entity.getId()).isEqualTo(packageId);
        assertThat(entity.getTrackingId()).isEqualTo("TRK-10");
        assertThat(entity.getWeight()).isEqualTo(9.5);
        assertThat(entity.getDimensions()).isEqualTo("10x20x30");
        assertThat(entity.getRecipient()).isEqualTo(recipient);
        assertThat(entity.getStatus()).isEqualTo(PackageStatus.RECEIVED);
        assertThat(entity.getCreatedAt()).isEqualTo(createdAt);
        assertThat(entity.getUpdatedAt()).isEqualTo(updatedAt);

        entity.setStatus(PackageStatus.IN_TRANSIT);
        Instant newUpdatedAt = updatedAt.plusSeconds(5);
        entity.setUpdatedAt(newUpdatedAt);

        assertThat(entity.getStatus()).isEqualTo(PackageStatus.IN_TRANSIT);
        assertThat(entity.getUpdatedAt()).isEqualTo(newUpdatedAt);
    }

    @Test
    @DisplayName("protected no-arg constructor exists")
    void protectedNoArgConstructor_exists() throws Exception {
        Constructor<PackageEntity> ctor = PackageEntity.class.getDeclaredConstructor();
        ctor.setAccessible(true);

        PackageEntity instance = ctor.newInstance();

        assertThat(instance).isNotNull();
    }
}
