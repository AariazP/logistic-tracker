package com.vcsoft.logistic_tracker_back.dto.response;

import com.vcsoft.logistic_tracker_back.domain.model.AppUser;
import org.springframework.stereotype.Component;

@Component
public class DriverResponseMapper {

    public DriverResponse toResponse(AppUser driver) {
        return new DriverResponse(
                driver.getId(),
                driver.getUsername(),
                driver.getRole(),
                driver.getCreatedAt()
        );
    }
}