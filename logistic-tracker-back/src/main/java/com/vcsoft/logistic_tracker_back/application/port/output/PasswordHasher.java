package com.vcsoft.logistic_tracker_back.application.port.output;

public interface PasswordHasher {
    String hash(String rawPassword);
}