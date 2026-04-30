package com.vcsoft.logistic_tracker_back.application.usecase;

import com.vcsoft.logistic_tracker_back.application.port.input.DeleteDriverUseCase;
import com.vcsoft.logistic_tracker_back.application.port.output.UserRepository;
import com.vcsoft.logistic_tracker_back.domain.exception.UserNotFoundException;
import com.vcsoft.logistic_tracker_back.domain.model.AppUser;
import com.vcsoft.logistic_tracker_back.domain.model.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class DeleteDriverUseCaseImpl implements DeleteDriverUseCase {

    private final UserRepository userRepository;

    public DeleteDriverUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void deleteDriver(UUID driverId) {
        AppUser driver = userRepository.findByIdAndRole(driverId, UserRole.DRIVER)
                .orElseThrow(() -> new UserNotFoundException(driverId));
        userRepository.delete(driver);
    }
}