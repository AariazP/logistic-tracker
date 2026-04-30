package com.vcsoft.logistic_tracker_back.infrastructure.persistence.mapper;

import com.vcsoft.logistic_tracker_back.domain.model.AppUser;
import com.vcsoft.logistic_tracker_back.domain.model.UserRole;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("UserPersistenceMapper tests")
class UserPersistenceMapperTest {

    private final UserPersistenceMapper mapper = new UserPersistenceMapper();

    @Test
    @DisplayName("toEntity and toDomain map fields")
    void toEntity_toDomain_mapFields() {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();
        AppUser domain = AppUser.reconstitute(id, "admin", "hash", UserRole.ADMIN, createdAt);

        UserEntity entity = mapper.toEntity(domain);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getUsername()).isEqualTo("admin");
        assertThat(entity.getPassword()).isEqualTo("hash");
        assertThat(entity.getRole()).isEqualTo("ADMIN");
        assertThat(entity.getCreatedAt()).isEqualTo(createdAt);

        AppUser mappedBack = mapper.toDomain(entity);

        assertThat(mappedBack.getId()).isEqualTo(id);
        assertThat(mappedBack.getRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    @DisplayName("toDomain throws when role string is invalid")
    void toDomain_throwsForInvalidRole() {
        UserEntity badEntity = new UserEntity(UUID.randomUUID(), "x", "p", "NOT_A_ROLE", Instant.now());

        assertThatThrownBy(() -> mapper.toDomain(badEntity))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
