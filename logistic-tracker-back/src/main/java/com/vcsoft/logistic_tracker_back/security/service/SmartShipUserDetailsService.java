package com.vcsoft.logistic_tracker_back.security.service;

import com.vcsoft.logistic_tracker_back.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmartShipUserDetailsService implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;

    public SmartShipUserDetailsService(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userJpaRepository.findByUsername(username)
                .map(entity -> new User(
                        entity.getUsername(),
                        entity.getPassword(),
                        List.of(new SimpleGrantedAuthority("ROLE_" + entity.getRole()))
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
