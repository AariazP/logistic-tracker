package com.vcsoft.logistic_tracker_back.infrastructure.security;

import com.vcsoft.logistic_tracker_back.domain.model.AppUser;
import com.vcsoft.logistic_tracker_back.domain.model.UserRole;
import com.vcsoft.logistic_tracker_back.domain.port.out.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SmartShipUserDetailsService tests")
class SmartShipUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SmartShipUserDetailsService service;

    @Test
    @DisplayName("loadUserByUsername returns spring user with role authority")
    void loadUserByUsername_returnsMappedUser() {
        AppUser user = AppUser.reconstitute(UUID.randomUUID(), "driver", "hash", UserRole.DRIVER, Instant.now());
        when(userRepository.findByUsername("driver")).thenReturn(Optional.of(user));

        UserDetails details = service.loadUserByUsername("driver");

        assertThat(details.getUsername()).isEqualTo("driver");
        assertThat(details.getPassword()).isEqualTo("hash");
        assertThat(details.getAuthorities()).extracting("authority").containsExactly("ROLE_DRIVER");
    }

    @Test
    @DisplayName("loadUserByUsername throws when user does not exist")
    void loadUserByUsername_throwsWhenNotFound() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.loadUserByUsername("ghost"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("ghost");
    }
}
