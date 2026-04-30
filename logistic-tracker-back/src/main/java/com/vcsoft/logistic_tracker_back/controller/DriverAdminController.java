package com.vcsoft.logistic_tracker_back.controller;

import com.vcsoft.logistic_tracker_back.application.port.input.CreateDriverUseCase;
import com.vcsoft.logistic_tracker_back.application.port.input.DeleteDriverUseCase;
import com.vcsoft.logistic_tracker_back.application.port.input.ListDriversUseCase;
import com.vcsoft.logistic_tracker_back.dto.request.CreateDriverRequest;
import com.vcsoft.logistic_tracker_back.dto.response.DriverResponse;
import com.vcsoft.logistic_tracker_back.dto.response.DriverResponseMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/drivers")
public class DriverAdminController {

    private final CreateDriverUseCase createDriverUseCase;
    private final ListDriversUseCase listDriversUseCase;
    private final DeleteDriverUseCase deleteDriverUseCase;
    private final DriverResponseMapper driverResponseMapper;

    public DriverAdminController(CreateDriverUseCase createDriverUseCase,
                                 ListDriversUseCase listDriversUseCase,
                                 DeleteDriverUseCase deleteDriverUseCase,
                                 DriverResponseMapper driverResponseMapper) {
        this.createDriverUseCase = createDriverUseCase;
        this.listDriversUseCase = listDriversUseCase;
        this.deleteDriverUseCase = deleteDriverUseCase;
        this.driverResponseMapper = driverResponseMapper;
    }

    @PostMapping
    public ResponseEntity<DriverResponse> createDriver(@Valid @RequestBody CreateDriverRequest request) {
        var driver = createDriverUseCase.createDriver(request.username(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(driverResponseMapper.toResponse(driver));
    }

    @GetMapping
    public ResponseEntity<List<DriverResponse>> listDrivers() {
        List<DriverResponse> drivers = listDriversUseCase.listDrivers().stream()
                .map(driverResponseMapper::toResponse)
                .toList();
        return ResponseEntity.ok(drivers);
    }

    @DeleteMapping("/{driverId}")
    public ResponseEntity<Void> deleteDriver(@PathVariable UUID driverId) {
        deleteDriverUseCase.deleteDriver(driverId);
        return ResponseEntity.noContent().build();
    }
}