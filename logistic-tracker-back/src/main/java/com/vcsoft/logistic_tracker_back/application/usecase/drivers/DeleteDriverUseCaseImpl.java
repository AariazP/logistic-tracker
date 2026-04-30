package com.vcsoft.logistic_tracker_back.application.usecase.drivers;

import com.vcsoft.logistic_tracker_back.application.usecase.drivers.DeleteDriverUseCase;
import com.vcsoft.logistic_tracker_back.domain.port.out.UserRepository;
import com.vcsoft.logistic_tracker_back.domain.exception.UserNotFoundException;
import com.vcsoft.logistic_tracker_back.domain.model.AppUser;
import com.vcsoft.logistic_tracker_back.domain.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteDriverUseCaseImpl implements DeleteDriverUseCase {

    private final UserRepository userRepository;

    @Override
    public void deleteDriver(UUID driverId) {
        AppUser driver = userRepository.findByIdAndRole(driverId, UserRole.DRIVER)
                .orElseThrow(() -> new UserNotFoundException(driverId));
        userRepository.delete(driver);
    }
}