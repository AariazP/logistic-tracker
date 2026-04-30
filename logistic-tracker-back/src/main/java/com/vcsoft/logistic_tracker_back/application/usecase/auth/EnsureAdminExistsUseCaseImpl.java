package com.vcsoft.logistic_tracker_back.application.usecase.auth;

import com.vcsoft.logistic_tracker_back.application.usecase.auth.EnsureAdminExistsUseCase;
import com.vcsoft.logistic_tracker_back.domain.port.out.PasswordHasher;
import com.vcsoft.logistic_tracker_back.domain.port.out.UserRepository;
import com.vcsoft.logistic_tracker_back.domain.model.AppUser;
import com.vcsoft.logistic_tracker_back.domain.model.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EnsureAdminExistsUseCaseImpl implements EnsureAdminExistsUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public EnsureAdminExistsUseCaseImpl(UserRepository userRepository,
                                        PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public void ensureAdminExists(String username, String rawPassword) {
        if (userRepository.countByRole(UserRole.ADMIN) > 0) {
            return;
        }
        AppUser admin = AppUser.create(username, passwordHasher.hash(rawPassword), UserRole.ADMIN);
        userRepository.save(admin);
    }
}