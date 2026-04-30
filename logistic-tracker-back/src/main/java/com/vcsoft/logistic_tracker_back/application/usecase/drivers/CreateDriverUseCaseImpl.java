package com.vcsoft.logistic_tracker_back.application.usecase.drivers;

import com.vcsoft.logistic_tracker_back.application.usecase.drivers.CreateDriverUseCase;
import com.vcsoft.logistic_tracker_back.domain.port.out.PasswordHasher;
import com.vcsoft.logistic_tracker_back.domain.port.out.UserRepository;
import com.vcsoft.logistic_tracker_back.domain.model.AppUser;
import com.vcsoft.logistic_tracker_back.domain.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateDriverUseCaseImpl implements CreateDriverUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    @Override
    public AppUser createDriver(String username, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }

        AppUser driver = AppUser.create(username, passwordHasher.hash(rawPassword), UserRole.DRIVER);
        return userRepository.save(driver);
    }
}