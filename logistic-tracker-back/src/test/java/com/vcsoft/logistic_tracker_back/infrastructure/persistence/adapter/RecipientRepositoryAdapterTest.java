package com.vcsoft.logistic_tracker_back.infrastructure.persistence.adapter;

import com.vcsoft.logistic_tracker_back.domain.model.Recipient;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.entity.RecipientEntity;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.mapper.RecipientPersistenceMapper;
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
@DisplayName("RecipientRepositoryAdapter tests")
class RecipientRepositoryAdapterTest {

    @Mock
    private RecipientJpaRepository recipientJpaRepository;

    @Mock
    private RecipientPersistenceMapper mapper;

    @InjectMocks
    private RecipientRepositoryAdapter adapter;

    @Test
    @DisplayName("save maps entity to domain")
    void save_mapsEntityToDomain() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        Recipient recipient = Recipient.reconstitute(id, "Ana", "ana@mail.com", "999", "A", "DOC", now, now);
        RecipientEntity entity = new RecipientEntity(id, "Ana", "ana@mail.com", "999", "A", "DOC", now, now);

        when(mapper.toEntity(recipient)).thenReturn(entity);
        when(recipientJpaRepository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(recipient);

        Recipient result = adapter.save(recipient);

        assertThat(result).isSameAs(recipient);
    }

    @Test
    @DisplayName("findById maps when present and empty when missing")
    void findById_presentAndMissing() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        Recipient recipient = Recipient.reconstitute(id, "Ana", "ana@mail.com", "999", "A", "DOC", now, now);
        RecipientEntity entity = new RecipientEntity(id, "Ana", "ana@mail.com", "999", "A", "DOC", now, now);

        when(recipientJpaRepository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(recipient);

        assertThat(adapter.findById(id)).contains(recipient);

        UUID missing = UUID.randomUUID();
        when(recipientJpaRepository.findById(missing)).thenReturn(Optional.empty());

        assertThat(adapter.findById(missing)).isEmpty();
        verify(mapper, never()).toDomain((RecipientEntity) null);
    }

    @Test
    @DisplayName("findAll maps all and delete delegates by id")
    void findAll_andDelete_delegate() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        RecipientEntity entity = new RecipientEntity(id, "Ana", "ana@mail.com", "999", "A", "DOC", now, now);
        Recipient recipient = Recipient.reconstitute(id, "Ana", "ana@mail.com", "999", "A", "DOC", now, now);

        when(recipientJpaRepository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(recipient);

        assertThat(adapter.findAll()).containsExactly(recipient);

        adapter.delete(recipient);
        verify(recipientJpaRepository).deleteById(id);
    }
}
