package com.vcsoft.logistic_tracker_back.application.usecase.auth;

public interface EnsureAdminExistsUseCase {
    void ensureAdminExists(String username, String rawPassword);
}