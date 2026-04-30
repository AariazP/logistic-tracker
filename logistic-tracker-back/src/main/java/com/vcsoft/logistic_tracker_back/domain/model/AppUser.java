package com.vcsoft.logistic_tracker_back.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AppUser {

    private final UUID id;
    private final String username;
    private final String passwordHash;
    private final UserRole role;
    private final Instant createdAt;

    public static AppUser create(String username, String passwordHash, UserRole role) {
        return new AppUser(UUID.randomUUID(), username, passwordHash, role, Instant.now());
    }

    public static AppUser reconstitute(UUID id, String username, String passwordHash,
                                       UserRole role, Instant createdAt) {
        return new AppUser(id, username, passwordHash, role, createdAt);
    }

}