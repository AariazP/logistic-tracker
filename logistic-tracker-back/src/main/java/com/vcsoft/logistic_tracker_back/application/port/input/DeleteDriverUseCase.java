package com.vcsoft.logistic_tracker_back.application.port.input;

import java.util.UUID;

public interface DeleteDriverUseCase {
    void deleteDriver(UUID driverId);
}