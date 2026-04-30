package com.vcsoft.logistic_tracker_back.domain.model;

import java.time.Instant;
import java.util.UUID;

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

    private AppUser(UUID id, String username, String passwordHash, UserRole role, Instant createdAt) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public UserRole getRole() { return role; }
    public Instant getCreatedAt() { return createdAt; }
}