package com.vcsoft.logistic_tracker_back.infrastructure.persistence.mapper;

import com.vcsoft.logistic_tracker_back.domain.model.Recipient;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.entity.RecipientEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RecipientPersistenceMapper tests")
class RecipientPersistenceMapperTest {

    private final RecipientPersistenceMapper mapper = new RecipientPersistenceMapper();

    @Test
    @DisplayName("toEntity maps all fields")
    void toEntity_mapsAllFields() {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();
        Instant updatedAt = createdAt.plusSeconds(60);
        Recipient recipient = Recipient.reconstitute(id, "Ana", "ana@mail.com", "123", "Street", "DOC", createdAt, updatedAt);

        RecipientEntity entity = mapper.toEntity(recipient);

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
    @DisplayName("toDomain maps all fields")
    void toDomain_mapsAllFields() {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();
        Instant updatedAt = createdAt.plusSeconds(60);
        RecipientEntity entity = new RecipientEntity(id, "Ana", "ana@mail.com", "123", "Street", "DOC", createdAt, updatedAt);

        Recipient domain = mapper.toDomain(entity);

        assertThat(domain.getId()).isEqualTo(id);
        assertThat(domain.getName()).isEqualTo("Ana");
        assertThat(domain.getEmail()).isEqualTo("ana@mail.com");
        assertThat(domain.getPhone()).isEqualTo("123");
        assertThat(domain.getAddress()).isEqualTo("Street");
        assertThat(domain.getDocumentNumber()).isEqualTo("DOC");
        assertThat(domain.getCreatedAt()).isEqualTo(createdAt);
        assertThat(domain.getUpdatedAt()).isEqualTo(updatedAt);
    }
}
