package com.vcsoft.logistic_tracker_back.application.port.output;

import com.vcsoft.logistic_tracker_back.domain.model.AppUser;
import com.vcsoft.logistic_tracker_back.domain.model.UserRole;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    AppUser save(AppUser user);
    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByIdAndRole(UUID userId, UserRole role);
    List<AppUser> findAllByRole(UserRole role);
    boolean existsByUsername(String username);
    long countByRole(UserRole role);
    void delete(AppUser user);
}