package com.vcsoft.logistic_tracker_back.application.usecase.drivers;

import com.vcsoft.logistic_tracker_back.domain.model.AppUser;

public interface CreateDriverUseCase {
    AppUser createDriver(String username, String rawPassword);
}