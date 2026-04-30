package com.vcsoft.logistic_tracker_back.infrastructure.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("BCryptPasswordHasher tests")
class BCryptPasswordHasherTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private BCryptPasswordHasher hasher;

    @Test
    @DisplayName("hash delegates to password encoder")
    void hash_delegatesToEncoder() {
        when(passwordEncoder.encode("raw-pass")).thenReturn("encoded-pass");

        String result = hasher.hash("raw-pass");

        assertThat(result).isEqualTo("encoded-pass");
        verify(passwordEncoder).encode("raw-pass");
    }
}
