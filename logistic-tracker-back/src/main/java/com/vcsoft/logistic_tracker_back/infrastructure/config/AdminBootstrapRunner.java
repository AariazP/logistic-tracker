package com.vcsoft.logistic_tracker_back.infrastructure.config;

import com.vcsoft.logistic_tracker_back.application.usecase.auth.EnsureAdminExistsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminBootstrapRunner implements ApplicationRunner {

    private final EnsureAdminExistsUseCase ensureAdminExistsUseCase;

    @Value("${app.bootstrap.admin.username:admin}")
    private String adminUsername;

    @Value("${app.bootstrap.admin.password:admin123}")
    private String adminPassword;

    @Override
    public void run(ApplicationArguments args) {
        ensureAdminExistsUseCase.ensureAdminExists(adminUsername, adminPassword);
    }
}