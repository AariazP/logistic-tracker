package com.vcsoft.logistic_tracker_back.adapter.controller;

import com.vcsoft.logistic_tracker_back.application.usecase.packages.CreatePackageUseCase;
import com.vcsoft.logistic_tracker_back.application.usecase.packages.GetPackageByTrackingIdUseCase;
import com.vcsoft.logistic_tracker_back.application.usecase.packages.ListPackagesUseCase;
import com.vcsoft.logistic_tracker_back.application.usecase.packages.UpdatePackageStatusUseCase;
import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;
import com.vcsoft.logistic_tracker_back.application.dto.request.CreatePackageRequest;
import com.vcsoft.logistic_tracker_back.application.dto.request.UpdatePackageStatusRequest;
import com.vcsoft.logistic_tracker_back.application.dto.response.PackageResponse;
import com.vcsoft.logistic_tracker_back.adapter.mapper.PackageResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller — no business logic here.
 * Delegates entirely to the use-case layer.
 */
@RestController
@RequestMapping("/api/v1/packages")
@RequiredArgsConstructor
public class PackageController {

    private final CreatePackageUseCase createPackageUseCase;
    private final ListPackagesUseCase listPackagesUseCase;
    private final GetPackageByTrackingIdUseCase getPackageByTrackingIdUseCase;
    private final UpdatePackageStatusUseCase updatePackageStatusUseCase;
    private final PackageResponseMapper responseMapper;

    /**
     * POST /api/v1/packages — ADMIN only (enforced in SecurityConfig)
     */
    @PostMapping
    public ResponseEntity<PackageResponse> createPackage(
            @Valid @RequestBody CreatePackageRequest request) {
        var pkg = createPackageUseCase.createPackage(
                request.trackingId(),
                request.weight(),
                request.dimensions(),
            request.recipientId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMapper.toResponse(pkg));
    }

    /**
     * GET /api/v1/packages?status=IN_TRANSIT — filter by status (optional)
     */
    @GetMapping
    public ResponseEntity<List<PackageResponse>> listPackages(
            @RequestParam(required = false) PackageStatus status) {
        List<PackageResponse> packages = listPackagesUseCase.listPackages(status)
                .stream()
                .map(responseMapper::toResponse)
                .toList();
        return ResponseEntity.ok(packages);
    }

    /**
     * GET /api/v1/packages/{trackingId}
     */
    @GetMapping("/{trackingId}")
    public ResponseEntity<PackageResponse> getByTrackingId(@PathVariable String trackingId) {
        return ResponseEntity.ok(responseMapper.toResponse(
            getPackageByTrackingIdUseCase.getByTrackingId(trackingId)
        ));
    }

    /**
     * PATCH /api/v1/packages/{trackingId}/status — DRIVER only (enforced in SecurityConfig)
     */
    @PatchMapping("/{trackingId}/status")
    public ResponseEntity<PackageResponse> updateStatus(
            @PathVariable String trackingId,
            @Valid @RequestBody UpdatePackageStatusRequest request) {
        var pkg = updatePackageStatusUseCase.updateStatus(trackingId, request.status());
        return ResponseEntity.ok(responseMapper.toResponse(pkg));
    }
}
