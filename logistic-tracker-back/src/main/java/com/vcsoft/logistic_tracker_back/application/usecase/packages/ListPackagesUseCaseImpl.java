package com.vcsoft.logistic_tracker_back.application.usecase.packages;

import com.vcsoft.logistic_tracker_back.application.usecase.packages.ListPackagesUseCase;
import com.vcsoft.logistic_tracker_back.domain.port.out.PackageRepository;
import com.vcsoft.logistic_tracker_back.domain.model.Package;
import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ListPackagesUseCaseImpl implements ListPackagesUseCase {

    private final PackageRepository packageRepository;

    @Override
    public List<Package> listPackages(PackageStatus statusFilter) {
        if (statusFilter != null) {
            return packageRepository.findAllByStatus(statusFilter);
        }
        return packageRepository.findAll();
    }
}