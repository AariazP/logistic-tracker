package com.vcsoft.logistic_tracker_back.application.port.input;

import com.vcsoft.logistic_tracker_back.domain.model.AppUser;

import java.util.List;

public interface ListDriversUseCase {
    List<AppUser> listDrivers();
}