package com.vcsoft.logistic_tracker_back.infrastructure.persistence.adapter;

import com.vcsoft.logistic_tracker_back.domain.port.out.RecipientRepository;
import com.vcsoft.logistic_tracker_back.domain.model.Recipient;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.entity.RecipientEntity;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.mapper.RecipientPersistenceMapper;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.repository.RecipientJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RecipientRepositoryAdapter implements RecipientRepository {

    private final RecipientJpaRepository recipientJpaRepository;
    private final RecipientPersistenceMapper mapper;

    @Override
    public Recipient save(Recipient recipient) {
        RecipientEntity saved = recipientJpaRepository.save(mapper.toEntity(recipient));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Recipient> findById(UUID recipientId) {
        return recipientJpaRepository.findById(recipientId).map(mapper::toDomain);
    }

    @Override
    public List<Recipient> findAll() {
        return recipientJpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void delete(Recipient recipient) {
        recipientJpaRepository.deleteById(recipient.getId());
    }
}