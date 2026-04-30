package com.vcsoft.logistic_tracker_back.application.usecase.drivers;

import com.vcsoft.logistic_tracker_back.application.usecase.drivers.ListDriversUseCase;
import com.vcsoft.logistic_tracker_back.domain.port.out.UserRepository;
import com.vcsoft.logistic_tracker_back.domain.model.AppUser;
import com.vcsoft.logistic_tracker_back.domain.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ListDriversUseCaseImpl implements ListDriversUseCase {

    private final UserRepository userRepository;

    @Override
    public List<AppUser> listDrivers() {
        return userRepository.findAllByRole(UserRole.DRIVER);
    }
}