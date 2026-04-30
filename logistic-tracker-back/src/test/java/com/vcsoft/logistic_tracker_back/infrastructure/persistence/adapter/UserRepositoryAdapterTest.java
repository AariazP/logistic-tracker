package com.vcsoft.logistic_tracker_back.infrastructure.persistence.adapter;

import com.vcsoft.logistic_tracker_back.domain.model.AppUser;
import com.vcsoft.logistic_tracker_back.domain.model.UserRole;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.entity.UserEntity;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.mapper.UserPersistenceMapper;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.repository.UserJpaRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserRepositoryAdapter tests")
class UserRepositoryAdapterTest {

    @Mock
    private UserJpaRepository userJpaRepository;

    @Mock
    private UserPersistenceMapper mapper;

    @InjectMocks
    private UserRepositoryAdapter adapter;

    @Test
    @DisplayName("save, findByUsername and exists delegate and map")
    void save_findByUsername_exists_delegateAndMap() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        AppUser user = AppUser.reconstitute(id, "driver", "hash", UserRole.DRIVER, now);
        UserEntity entity = new UserEntity(id, "driver", "hash", "DRIVER", now);

        when(mapper.toEntity(user)).thenReturn(entity);
        when(userJpaRepository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(user);
        when(userJpaRepository.findByUsername("driver")).thenReturn(Optional.of(entity));
        when(userJpaRepository.existsByUsername("driver")).thenReturn(true);

        assertThat(adapter.save(user)).isSameAs(user);
        assertThat(adapter.findByUsername("driver")).contains(user);
        assertThat(adapter.existsByUsername("driver")).isTrue();
    }

    @Test
    @DisplayName("findByIdAndRole and findAllByRole map results")
    void findByIdAndRole_findAllByRole_map() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        AppUser user = AppUser.reconstitute(id, "admin", "hash", UserRole.ADMIN, now);
        UserEntity entity = new UserEntity(id, "admin", "hash", "ADMIN", now);

        when(userJpaRepository.findByIdAndRole(id, "ADMIN")).thenReturn(Optional.of(entity));
        when(userJpaRepository.findAllByRole("ADMIN")).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(user);

        assertThat(adapter.findByIdAndRole(id, UserRole.ADMIN)).contains(user);
        assertThat(adapter.findAllByRole(UserRole.ADMIN)).containsExactly(user);
    }

    @Test
    @DisplayName("countByRole and delete delegate")
    void countAndDelete_delegate() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        AppUser user = AppUser.reconstitute(id, "admin", "hash", UserRole.ADMIN, now);

        when(userJpaRepository.countByRole("ADMIN")).thenReturn(3L);

        assertThat(adapter.countByRole(UserRole.ADMIN)).isEqualTo(3L);

        adapter.delete(user);
        verify(userJpaRepository).deleteById(id);
    }
}
