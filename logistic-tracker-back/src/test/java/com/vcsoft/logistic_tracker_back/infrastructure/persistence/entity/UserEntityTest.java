package com.vcsoft.logistic_tracker_back.infrastructure.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserEntity tests")
class UserEntityTest {

    @Test
    @DisplayName("all-args constructor and getters")
    void constructorAndGetters() {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.now();

        UserEntity entity = new UserEntity(id, "u", "p", "ADMIN", createdAt);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getUsername()).isEqualTo("u");
        assertThat(entity.getPassword()).isEqualTo("p");
        assertThat(entity.getRole()).isEqualTo("ADMIN");
        assertThat(entity.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("protected no-arg constructor exists")
    void protectedNoArgConstructor_exists() throws Exception {
        Constructor<UserEntity> ctor = UserEntity.class.getDeclaredConstructor();
        ctor.setAccessible(true);

        UserEntity instance = ctor.newInstance();

        assertThat(instance).isNotNull();
    }
}
