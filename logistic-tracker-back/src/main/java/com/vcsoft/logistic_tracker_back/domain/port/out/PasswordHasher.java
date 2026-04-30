package com.vcsoft.logistic_tracker_back.domain.port.out;

public interface PasswordHasher {
    String hash(String rawPassword);
}