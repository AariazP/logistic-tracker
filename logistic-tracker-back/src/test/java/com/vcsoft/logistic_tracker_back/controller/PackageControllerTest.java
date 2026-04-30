package com.vcsoft.logistic_tracker_back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vcsoft.logistic_tracker_back.application.port.input.PackageUseCase;
import com.vcsoft.logistic_tracker_back.domain.exception.InvalidStateTransitionException;
import com.vcsoft.logistic_tracker_back.domain.exception.PackageNotFoundException;
import com.vcsoft.logistic_tracker_back.domain.model.Package;
import com.vcsoft.logistic_tracker_back.domain.model.PackageStatus;
import com.vcsoft.logistic_tracker_back.dto.request.CreatePackageRequest;
import com.vcsoft.logistic_tracker_back.dto.request.UpdatePackageStatusRequest;
import com.vcsoft.logistic_tracker_back.dto.response.PackageResponseMapper;
import com.vcsoft.logistic_tracker_back.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PackageController.class)
@Import({PackageResponseMapper.class, GlobalExceptionHandler.class})
@DisplayName("PackageController Tests")
class PackageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PackageUseCase packageUseCase;

    private Package samplePackage;

    @BeforeEach
    void setUp() {
        samplePackage = Package.create("TRK-001", 2.5, "30x20x10", "John Doe");
    }

    // ── POST /packages ────────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /packages: ADMIN creates package → 201")
    void createPackage_admin_201() throws Exception {
        when(packageUseCase.createPackage(any(), anyDouble(), any(), any())).thenReturn(samplePackage);

        var request = new CreatePackageRequest("TRK-001", 2.5, "30x20x10", "John Doe");

        mockMvc.perform(post("/api/v1/packages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.trackingId").value("TRK-001"))
                .andExpect(jsonPath("$.status").value("RECEIVED"));
    }

    @Test
    @WithMockUser(roles = "DRIVER")
    @DisplayName("POST /packages: DRIVER is forbidden → 403")
    void createPackage_driver_403() throws Exception {
        var request = new CreatePackageRequest("TRK-001", 2.5, "30x20x10", "John Doe");

        mockMvc.perform(post("/api/v1/packages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /packages: invalid payload → 400")
    void createPackage_invalidPayload_400() throws Exception {
        var request = new CreatePackageRequest("", -1.0, "", "");

        mockMvc.perform(post("/api/v1/packages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ── GET /packages ─────────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /packages: returns list → 200")
    void listPackages_200() throws Exception {
        when(packageUseCase.listPackages(null)).thenReturn(List.of(samplePackage));

        mockMvc.perform(get("/api/v1/packages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].trackingId").value("TRK-001"));
    }

    @Test
    @WithMockUser(roles = "DRIVER")
    @DisplayName("GET /packages?status=RECEIVED: DRIVER can filter → 200")
    void listPackages_filterByStatus_200() throws Exception {
        when(packageUseCase.listPackages(PackageStatus.RECEIVED)).thenReturn(List.of(samplePackage));

        mockMvc.perform(get("/api/v1/packages").param("status", "RECEIVED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("RECEIVED"));
    }

    // ── PATCH /packages/{trackingId}/status ───────────────────────────────────

    @Test
    @WithMockUser(roles = "DRIVER")
    @DisplayName("PATCH /packages/{id}/status: DRIVER updates to IN_TRANSIT → 200")
    void updateStatus_driver_200() throws Exception {
        Package inTransit = Package.create("TRK-001", 2.5, "30x20x10", "John Doe");
        inTransit.transitionTo(PackageStatus.IN_TRANSIT);

        when(packageUseCase.updateStatus("TRK-001", PackageStatus.IN_TRANSIT)).thenReturn(inTransit);

        var request = new UpdatePackageStatusRequest(PackageStatus.IN_TRANSIT);

        mockMvc.perform(patch("/api/v1/packages/TRK-001/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_TRANSIT"));
    }

    @Test
    @WithMockUser(roles = "DRIVER")
    @DisplayName("PATCH /packages/{id}/status: invalid transition → 409")
    void updateStatus_invalidTransition_409() throws Exception {
        when(packageUseCase.updateStatus(eq("TRK-001"), eq(PackageStatus.DELIVERED)))
                .thenThrow(new InvalidStateTransitionException(PackageStatus.RECEIVED, PackageStatus.DELIVERED));

        var request = new UpdatePackageStatusRequest(PackageStatus.DELIVERED);

        mockMvc.perform(patch("/api/v1/packages/TRK-001/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflict"));
    }

    @Test
    @WithMockUser(roles = "DRIVER")
    @DisplayName("PATCH /packages/{id}/status: package not found → 404")
    void updateStatus_notFound_404() throws Exception {
        when(packageUseCase.updateStatus(eq("UNKNOWN"), any()))
                .thenThrow(new PackageNotFoundException("UNKNOWN"));

        var request = new UpdatePackageStatusRequest(PackageStatus.IN_TRANSIT);

        mockMvc.perform(patch("/api/v1/packages/UNKNOWN/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PATCH /packages/{id}/status: ADMIN is forbidden → 403")
    void updateStatus_admin_403() throws Exception {
        var request = new UpdatePackageStatusRequest(PackageStatus.IN_TRANSIT);

        mockMvc.perform(patch("/api/v1/packages/TRK-001/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}
