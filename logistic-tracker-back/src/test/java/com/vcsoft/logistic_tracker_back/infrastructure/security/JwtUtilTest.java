package com.vcsoft.logistic_tracker_back.infrastructure.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JwtUtil tests")
class JwtUtilTest {

    private static final String SECRET = "my-super-secret-key-with-at-least-32-bytes!!";

    @Test
    @DisplayName("generate token and extract claims")
    void generateAndExtractClaims() {
        JwtUtil jwtUtil = new JwtUtil(SECRET, 60_000);

        String token = jwtUtil.generateToken("john", "ADMIN");

        assertThat(jwtUtil.extractUsername(token)).isEqualTo("john");
        assertThat(jwtUtil.extractRole(token)).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("isTokenValid true for same user and not expired")
    void isTokenValid_true() {
        JwtUtil jwtUtil = new JwtUtil(SECRET, 60_000);
        UserDetails user = new User("john", "pwd", List.of());
        String token = jwtUtil.generateToken("john", "ADMIN");

        assertThat(jwtUtil.isTokenValid(token, user)).isTrue();
    }

    @Test
    @DisplayName("isTokenValid false for different username")
    void isTokenValid_falseWhenUsernameDiffers() {
        JwtUtil jwtUtil = new JwtUtil(SECRET, 60_000);
        UserDetails otherUser = new User("mary", "pwd", List.of());
        String token = jwtUtil.generateToken("john", "ADMIN");

        assertThat(jwtUtil.isTokenValid(token, otherUser)).isFalse();
    }

    @Test
    @DisplayName("isTokenValid false for expired token")
    void isTokenValid_falseWhenExpired() {
        JwtUtil expiredJwtUtil = new JwtUtil(SECRET, -1);
        UserDetails user = new User("john", "pwd", List.of());
        String token = expiredJwtUtil.generateToken("john", "ADMIN");

        assertThat(expiredJwtUtil.isTokenValid(token, user)).isFalse();
    }

    @Test
    @DisplayName("isTokenValid false for malformed token")
    void isTokenValid_falseForMalformedToken() {
        JwtUtil jwtUtil = new JwtUtil(SECRET, 60_000);
        UserDetails user = new User("john", "pwd", List.of());

        assertThat(jwtUtil.isTokenValid("not-a-token", user)).isFalse();
    }

    @Test
    @DisplayName("extractUsername throws for malformed token")
    void extractUsername_throwsForMalformedToken() {
        JwtUtil jwtUtil = new JwtUtil(SECRET, 60_000);

        assertThatThrownBy(() -> jwtUtil.extractUsername("bad-token"))
                .isInstanceOf(Exception.class);
    }
}
