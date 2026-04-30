package com.vcsoft.logistic_tracker_back.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Recipient {

    private final UUID id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String documentNumber;
    private final Instant createdAt;
    private Instant updatedAt;

    public static Recipient create(String name, String email, String phone,
                                   String address, String documentNumber) {
        Instant now = Instant.now();
        return new Recipient(UUID.randomUUID(), name, email, phone, address, documentNumber, now, now);
    }

    public static Recipient reconstitute(UUID id, String name, String email, String phone,
                                         String address, String documentNumber,
                                         Instant createdAt, Instant updatedAt) {
        return new Recipient(id, name, email, phone, address, documentNumber, createdAt, updatedAt);
    }

    public void updateInfo(String name, String email, String phone,
                           String address, String documentNumber) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.documentNumber = documentNumber;
        this.updatedAt = Instant.now();
    }

}