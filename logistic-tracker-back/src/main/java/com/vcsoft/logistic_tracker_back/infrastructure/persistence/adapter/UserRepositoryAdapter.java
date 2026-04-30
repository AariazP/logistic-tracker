package com.vcsoft.logistic_tracker_back.infrastructure.persistence.adapter;

import com.vcsoft.logistic_tracker_back.domain.port.out.UserRepository;
import com.vcsoft.logistic_tracker_back.domain.model.AppUser;
import com.vcsoft.logistic_tracker_back.domain.model.UserRole;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.entity.UserEntity;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.mapper.UserPersistenceMapper;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserPersistenceMapper mapper;

    public UserRepositoryAdapter(UserJpaRepository userJpaRepository,
                                 UserPersistenceMapper mapper) {
        this.userJpaRepository = userJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public AppUser save(AppUser user) {
        UserEntity saved = userJpaRepository.save(mapper.toEntity(user));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<AppUser> findByUsername(String username) {
        return userJpaRepository.findByUsername(username).map(mapper::toDomain);
    }

    @Override
    public Optional<AppUser> findByIdAndRole(UUID userId, UserRole role) {
        return userJpaRepository.findByIdAndRole(userId, role.name()).map(mapper::toDomain);
    }

    @Override
    public List<AppUser> findAllByRole(UserRole role) {
        return userJpaRepository.findAllByRole(role.name()).stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }

    @Override
    public long countByRole(UserRole role) {
        return userJpaRepository.countByRole(role.name());
    }

    @Override
    public void delete(AppUser user) {
        userJpaRepository.deleteById(user.getId());
    }
}