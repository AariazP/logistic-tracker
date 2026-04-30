package com.vcsoft.logistic_tracker_back.adapter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vcsoft.logistic_tracker_back.application.usecase.drivers.CreateDriverUseCase;
import com.vcsoft.logistic_tracker_back.application.usecase.drivers.DeleteDriverUseCase;
import com.vcsoft.logistic_tracker_back.application.usecase.drivers.ListDriversUseCase;
import com.vcsoft.logistic_tracker_back.domain.exception.UserNotFoundException;
import com.vcsoft.logistic_tracker_back.domain.model.AppUser;
import com.vcsoft.logistic_tracker_back.domain.model.UserRole;
import com.vcsoft.logistic_tracker_back.application.dto.request.CreateDriverRequest;
import com.vcsoft.logistic_tracker_back.adapter.mapper.DriverResponseMapper;
import com.vcsoft.logistic_tracker_back.adapter.exception.GlobalExceptionHandler;
import com.vcsoft.logistic_tracker_back.infrastructure.security.SecurityConfig;
import com.vcsoft.logistic_tracker_back.infrastructure.security.SmartShipUserDetailsService;
import com.vcsoft.logistic_tracker_back.infrastructure.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DriverAdminController.class)
@Import({DriverResponseMapper.class, GlobalExceptionHandler.class, SecurityConfig.class})
@DisplayName("DriverAdminController Tests")
class DriverAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateDriverUseCase createDriverUseCase;

    @MockBean
    private ListDriversUseCase listDriversUseCase;

    @MockBean
    private DeleteDriverUseCase deleteDriverUseCase;

    @MockBean
    private SmartShipUserDetailsService smartShipUserDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    private AppUser sampleDriver;
    private UUID driverId;

    @BeforeEach
    void setUp() {
        driverId = UUID.randomUUID();
        sampleDriver = AppUser.reconstitute(driverId, "driver1", "hashed", UserRole.DRIVER,
                java.time.Instant.now());
    }

    // ── POST /api/v1/admin/drivers ────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /admin/drivers: ADMIN creates driver → 201")
    void createDriver_admin_201() throws Exception {
        when(createDriverUseCase.createDriver("driver1", "pass123")).thenReturn(sampleDriver);

        var request = new CreateDriverRequest("driver1", "pass123");

        mockMvc.perform(post("/api/v1/admin/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("driver1"))
                .andExpect(jsonPath("$.role").value("DRIVER"));
    }

    @Test
    @WithMockUser(roles = "DRIVER")
    @DisplayName("POST /admin/drivers: DRIVER role is forbidden → 403")
    void createDriver_driver_403() throws Exception {
        var request = new CreateDriverRequest("driver2", "pass123");

        mockMvc.perform(post("/api/v1/admin/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /admin/drivers: blank username → 400")
    void createDriver_blankUsername_400() throws Exception {
        var request = new CreateDriverRequest("", "pass123");

        mockMvc.perform(post("/api/v1/admin/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /admin/drivers: password too short → 400")
    void createDriver_shortPassword_400() throws Exception {
        var request = new CreateDriverRequest("driver3", "abc");

        mockMvc.perform(post("/api/v1/admin/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ── GET /api/v1/admin/drivers ─────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /admin/drivers: ADMIN lists drivers → 200")
    void listDrivers_admin_200() throws Exception {
        when(listDriversUseCase.listDrivers()).thenReturn(List.of(sampleDriver));

        mockMvc.perform(get("/api/v1/admin/drivers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("driver1"))
                .andExpect(jsonPath("$[0].role").value("DRIVER"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /admin/drivers: empty list → 200 with empty array")
    void listDrivers_empty_200() throws Exception {
        when(listDriversUseCase.listDrivers()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/admin/drivers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser(roles = "DRIVER")
    @DisplayName("GET /admin/drivers: DRIVER role is forbidden → 403")
    void listDrivers_driver_403() throws Exception {
        mockMvc.perform(get("/api/v1/admin/drivers"))
                .andExpect(status().isForbidden());
    }

    // ── DELETE /api/v1/admin/drivers/{driverId} ───────────────────────────────

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /admin/drivers/{id}: ADMIN deletes driver → 204")
    void deleteDriver_admin_204() throws Exception {
        doNothing().when(deleteDriverUseCase).deleteDriver(driverId);

        mockMvc.perform(delete("/api/v1/admin/drivers/{id}", driverId))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /admin/drivers/{id}: driver not found → 404")
    void deleteDriver_notFound_404() throws Exception {
        doThrow(new UserNotFoundException(driverId)).when(deleteDriverUseCase).deleteDriver(driverId);

        mockMvc.perform(delete("/api/v1/admin/drivers/{id}", driverId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "DRIVER")
    @DisplayName("DELETE /admin/drivers/{id}: DRIVER role is forbidden → 403")
    void deleteDriver_driver_403() throws Exception {
        mockMvc.perform(delete("/api/v1/admin/drivers/{id}", driverId))
                .andExpect(status().isForbidden());
    }
}
