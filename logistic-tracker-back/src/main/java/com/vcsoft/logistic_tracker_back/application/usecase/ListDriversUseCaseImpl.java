package com.vcsoft.logistic_tracker_back.application.usecase;

import com.vcsoft.logistic_tracker_back.application.port.input.ListDriversUseCase;
import com.vcsoft.logistic_tracker_back.application.port.output.UserRepository;
import com.vcsoft.logistic_tracker_back.domain.model.AppUser;
import com.vcsoft.logistic_tracker_back.domain.model.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ListDriversUseCaseImpl implements ListDriversUseCase {

    private final UserRepository userRepository;

    public ListDriversUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<AppUser> listDrivers() {
        return userRepository.findAllByRole(UserRole.DRIVER);
    }
}