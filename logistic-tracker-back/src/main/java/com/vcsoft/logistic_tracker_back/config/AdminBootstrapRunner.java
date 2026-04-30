package com.vcsoft.logistic_tracker_back.config;

import com.vcsoft.logistic_tracker_back.application.port.input.EnsureAdminExistsUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminBootstrapRunner implements ApplicationRunner {

    private final EnsureAdminExistsUseCase ensureAdminExistsUseCase;

    @Value("${app.bootstrap.admin.username:admin}")
    private String adminUsername;

    @Value("${app.bootstrap.admin.password:admin123}")
    private String adminPassword;

    public AdminBootstrapRunner(EnsureAdminExistsUseCase ensureAdminExistsUseCase) {
        this.ensureAdminExistsUseCase = ensureAdminExistsUseCase;
    }

    @Override
    public void run(ApplicationArguments args) {
        ensureAdminExistsUseCase.ensureAdminExists(adminUsername, adminPassword);
    }
}