package com.vcsoft.logistic_tracker_back.domain.model;

import java.time.Instant;
import java.util.UUID;

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

    private Recipient(UUID id, String name, String email, String phone,
                      String address, String documentNumber,
                      Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.documentNumber = documentNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getDocumentNumber() { return documentNumber; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}