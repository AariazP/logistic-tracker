package com.vcsoft.logistic_tracker_back.application.port.input;

public interface EnsureAdminExistsUseCase {
    void ensureAdminExists(String username, String rawPassword);
}