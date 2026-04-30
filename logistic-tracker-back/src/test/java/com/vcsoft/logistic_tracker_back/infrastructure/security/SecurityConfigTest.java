package com.vcsoft.logistic_tracker_back.infrastructure.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SecurityConfig tests")
class SecurityConfigTest {

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private SmartShipUserDetailsService userDetailsService;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @Test
    @DisplayName("corsConfigurationSource trims and filters empty origins")
    void corsConfigurationSource_parsesOrigins() {
        SecurityConfig config = new SecurityConfig(jwtAuthenticationFilter, userDetailsService);
        ReflectionTestUtils.setField(config, "allowedOrigins", "http://a.com, ,http://b.com  ");

        CorsConfigurationSource source = config.corsConfigurationSource();
        CorsConfiguration cors = source.getCorsConfiguration(new MockHttpServletRequest("GET", "/api/v1/packages"));

        assertThat(cors).isNotNull();
        assertThat(cors.getAllowedOrigins()).containsExactly("http://a.com", "http://b.com");
        assertThat(cors.getAllowedMethods()).contains("GET", "POST", "PATCH", "DELETE", "OPTIONS");
        assertThat(cors.getAllowCredentials()).isTrue();
    }

    @Test
    @DisplayName("password encoder encodes and verifies")
    void passwordEncoder_encodesAndMatches() {
        SecurityConfig config = new SecurityConfig(jwtAuthenticationFilter, userDetailsService);

        PasswordEncoder encoder = config.passwordEncoder();
        String hash = encoder.encode("secret");

        assertThat(hash).isNotBlank();
        assertThat(hash).isNotEqualTo("secret");
        assertThat(encoder.matches("secret", hash)).isTrue();
        assertThat(encoder.matches("other", hash)).isFalse();
    }

    @Test
    @DisplayName("authentication provider uses configured userDetailsService and encoder")
    void authenticationProvider_usesDependencies() {
        SecurityConfig config = new SecurityConfig(jwtAuthenticationFilter, userDetailsService);

        var provider = config.authenticationProvider();

        assertThat(provider).isNotNull();
        assertThat(provider.supports(org.springframework.security.authentication.UsernamePasswordAuthenticationToken.class)).isTrue();
    }

    @Test
    @DisplayName("authenticationManager delegates to AuthenticationConfiguration")
    void authenticationManager_delegates() throws Exception {
        SecurityConfig config = new SecurityConfig(jwtAuthenticationFilter, userDetailsService);
        AuthenticationManager expected = authentication -> authentication;
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(expected);

        AuthenticationManager result = config.authenticationManager(authenticationConfiguration);

        assertThat(result).isSameAs(expected);
    }
}
