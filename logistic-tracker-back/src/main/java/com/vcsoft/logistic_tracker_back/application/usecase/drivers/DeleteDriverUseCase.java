package com.vcsoft.logistic_tracker_back.application.usecase.drivers;

import java.util.UUID;

public interface DeleteDriverUseCase {
    void deleteDriver(UUID driverId);
}