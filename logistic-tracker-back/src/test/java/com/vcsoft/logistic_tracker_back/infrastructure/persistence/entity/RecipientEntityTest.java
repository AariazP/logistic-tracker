package com.vcsoft.logistic_tracker_back.infrastructure.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RecipientEntity tests")
class RecipientEntityTest {

    @Test
    @DisplayName("all-args constructor and getters")
    void constructorAndGetters() {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();
        Instant updatedAt = createdAt.plusSeconds(1);

        RecipientEntity entity = new RecipientEntity(id, "Ana", "ana@mail.com", "123", "Street", "DOC", createdAt, updatedAt);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getName()).isEqualTo("Ana");
        assertThat(entity.getEmail()).isEqualTo("ana@mail.com");
        assertThat(entity.getPhone()).isEqualTo("123");
        assertThat(entity.getAddress()).isEqualTo("Street");
        assertThat(entity.getDocumentNumber()).isEqualTo("DOC");
        assertThat(entity.getCreatedAt()).isEqualTo(createdAt);
        assertThat(entity.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("protected no-arg constructor exists")
    void protectedNoArgConstructor_exists() throws Exception {
        Constructor<RecipientEntity> ctor = RecipientEntity.class.getDeclaredConstructor();
        ctor.setAccessible(true);

        RecipientEntity instance = ctor.newInstance();

        assertThat(instance).isNotNull();
    }
}
