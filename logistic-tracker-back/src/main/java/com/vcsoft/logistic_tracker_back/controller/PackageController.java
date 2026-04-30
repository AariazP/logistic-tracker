package com.vcsoft.logistic_tracker_back.controller;

import com.vcsoft.logistic_tracker_back.application.port.input.PackageUseCase;
import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;
import com.vcsoft.logistic_tracker_back.dto.request.CreatePackageRequest;
import com.vcsoft.logistic_tracker_back.dto.request.UpdatePackageStatusRequest;
import com.vcsoft.logistic_tracker_back.dto.response.PackageResponse;
import com.vcsoft.logistic_tracker_back.dto.response.PackageResponseMapper;
import jakarta.validation.Valid;
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
public class PackageController {

    private final PackageUseCase packageUseCase;
    private final PackageResponseMapper responseMapper;

    public PackageController(PackageUseCase packageUseCase,
                             PackageResponseMapper responseMapper) {
        this.packageUseCase = packageUseCase;
        this.responseMapper = responseMapper;
    }

    /**
     * POST /api/v1/packages — ADMIN only (enforced in SecurityConfig)
     */
    @PostMapping
    public ResponseEntity<PackageResponse> createPackage(
            @Valid @RequestBody CreatePackageRequest request) {
        var pkg = packageUseCase.createPackage(
                request.trackingId(),
                request.weight(),
                request.dimensions(),
                request.recipientName()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMapper.toResponse(pkg));
    }

    /**
     * GET /api/v1/packages?status=IN_TRANSIT — filter by status (optional)
     */
    @GetMapping
    public ResponseEntity<List<PackageResponse>> listPackages(
            @RequestParam(required = false) PackageStatus status) {
        List<PackageResponse> packages = packageUseCase.listPackages(status)
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
        return packageUseCase.listPackages(null)
                .stream()
                .filter(p -> p.getTrackingId().equals(trackingId))
                .findFirst()
                .map(p -> ResponseEntity.ok(responseMapper.toResponse(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PATCH /api/v1/packages/{trackingId}/status — DRIVER only (enforced in SecurityConfig)
     */
    @PatchMapping("/{trackingId}/status")
    public ResponseEntity<PackageResponse> updateStatus(
            @PathVariable String trackingId,
            @Valid @RequestBody UpdatePackageStatusRequest request) {
        var pkg = packageUseCase.updateStatus(trackingId, request.status());
        return ResponseEntity.ok(responseMapper.toResponse(pkg));
    }
}
