package com.vcsoft.logistic_tracker_back.application.usecase;

import com.vcsoft.logistic_tracker_back.application.port.output.PasswordHasher;
import com.vcsoft.logistic_tracker_back.application.port.output.UserRepository;
import com.vcsoft.logistic_tracker_back.domain.exception.UserNotFoundException;
import com.vcsoft.logistic_tracker_back.domain.model.AppUser;
import com.vcsoft.logistic_tracker_back.domain.model.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Driver management use case tests")
class DriverManagementUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @InjectMocks
    private CreateDriverUseCaseImpl createDriverUseCase;

    @InjectMocks
    private DeleteDriverUseCaseImpl deleteDriverUseCase;

    @Test
    @DisplayName("createDriver: creates driver with hashed password")
    void createDriver_success() {
        when(userRepository.existsByUsername("driver-a")).thenReturn(false);
        when(passwordHasher.hash("secret123")).thenReturn("hashed-secret");
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AppUser driver = createDriverUseCase.createDriver("driver-a", "secret123");

        assertThat(driver.getUsername()).isEqualTo("driver-a");
        assertThat(driver.getPasswordHash()).isEqualTo("hashed-secret");
        assertThat(driver.getRole()).isEqualTo(UserRole.DRIVER);
    }

    @Test
    @DisplayName("createDriver: rejects duplicate username")
    void createDriver_duplicateUsername() {
        when(userRepository.existsByUsername("driver-a")).thenReturn(true);

        assertThatThrownBy(() -> createDriverUseCase.createDriver("driver-a", "secret123"))
                .isInstanceOf(IllegalArgumentException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteDriver: removes existing driver")
    void deleteDriver_success() {
        AppUser driver = AppUser.create("driver-a", "hashed-secret", UserRole.DRIVER);
        when(userRepository.findByIdAndRole(driver.getId(), UserRole.DRIVER)).thenReturn(Optional.of(driver));

        deleteDriverUseCase.deleteDriver(driver.getId());

        verify(userRepository).delete(driver);
    }

    @Test
    @DisplayName("deleteDriver: throws when driver is missing")
    void deleteDriver_notFound() {
        UUID driverId = UUID.randomUUID();
        when(userRepository.findByIdAndRole(driverId, UserRole.DRIVER)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteDriverUseCase.deleteDriver(driverId))
                .isInstanceOf(UserNotFoundException.class);
    }
}