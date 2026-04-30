package com.vcsoft.logistic_tracker_back.application.usecase;

import com.vcsoft.logistic_tracker_back.domain.port.out.PasswordHasher;
import com.vcsoft.logistic_tracker_back.domain.port.out.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Ensure admin exists use case tests")
class EnsureAdminExistsUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @InjectMocks
    private EnsureAdminExistsUseCaseImpl ensureAdminExistsUseCase;

    @Test
    @DisplayName("ensureAdminExists: creates admin when none exist")
    void ensureAdminExists_createsAdmin() {
        when(userRepository.countByRole(com.vcsoft.logistic_tracker_back.domain.model.UserRole.ADMIN)).thenReturn(0L);
        when(passwordHasher.hash("admin123")).thenReturn("hashed-admin");

        ensureAdminExistsUseCase.ensureAdminExists("admin", "admin123");

        verify(userRepository).save(any());
    }

    @Test
    @DisplayName("ensureAdminExists: does nothing when an admin already exists")
    void ensureAdminExists_noopWhenAdminExists() {
        when(userRepository.countByRole(com.vcsoft.logistic_tracker_back.domain.model.UserRole.ADMIN)).thenReturn(1L);

        ensureAdminExistsUseCase.ensureAdminExists("admin", "admin123");

        verify(passwordHasher, never()).hash(any());
        verify(userRepository, never()).save(any());
    }
}