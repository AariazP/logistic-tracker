package com.vcsoft.logistic_tracker_back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vcsoft.logistic_tracker_back.application.port.input.CreateRecipientUseCase;
import com.vcsoft.logistic_tracker_back.application.port.input.DeleteRecipientUseCase;
import com.vcsoft.logistic_tracker_back.application.port.input.ListRecipientsUseCase;
import com.vcsoft.logistic_tracker_back.application.port.input.UpdateRecipientUseCase;
import com.vcsoft.logistic_tracker_back.domain.exception.RecipientDeletionNotAllowedException;
import com.vcsoft.logistic_tracker_back.domain.exception.RecipientNotFoundException;
import com.vcsoft.logistic_tracker_back.domain.model.Recipient;
import com.vcsoft.logistic_tracker_back.dto.request.CreateRecipientRequest;
import com.vcsoft.logistic_tracker_back.dto.request.UpdateRecipientRequest;
import com.vcsoft.logistic_tracker_back.dto.response.RecipientResponseMapper;
import com.vcsoft.logistic_tracker_back.exception.GlobalExceptionHandler;
import com.vcsoft.logistic_tracker_back.security.config.SecurityConfig;
import com.vcsoft.logistic_tracker_back.security.service.SmartShipUserDetailsService;
import com.vcsoft.logistic_tracker_back.security.util.JwtUtil;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipientAdminController.class)
@Import({RecipientResponseMapper.class, GlobalExceptionHandler.class, SecurityConfig.class})
@DisplayName("RecipientAdminController Tests")
class RecipientAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateRecipientUseCase createRecipientUseCase;

    @MockBean
    private ListRecipientsUseCase listRecipientsUseCase;

    @MockBean
    private UpdateRecipientUseCase updateRecipientUseCase;

    @MockBean
    private DeleteRecipientUseCase deleteRecipientUseCase;

    @MockBean
    private SmartShipUserDetailsService smartShipUserDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    private Recipient sampleRecipient;
    private UUID recipientId;

    @BeforeEach
    void setUp() {
        recipientId = UUID.randomUUID();
        sampleRecipient = Recipient.reconstitute(
                recipientId, "John Doe", "john@example.com", "555-1234",
                "123 Main St", "DOC-001",
                java.time.Instant.now(), java.time.Instant.now());
    }

    // ── POST /api/v1/admin/recipients ─────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /admin/recipients: ADMIN creates recipient → 201")
    void createRecipient_admin_201() throws Exception {
        when(createRecipientUseCase.createRecipient(any(), any(), any(), any(), any()))
                .thenReturn(sampleRecipient);

        var request = new CreateRecipientRequest(
                "John Doe", "john@example.com", "555-1234", "123 Main St", "DOC-001");

        mockMvc.perform(post("/api/v1/admin/recipients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @WithMockUser(roles = "DRIVER")
    @DisplayName("POST /admin/recipients: DRIVER role is forbidden → 403")
    void createRecipient_driver_403() throws Exception {
        var request = new CreateRecipientRequest(
                "John Doe", "john@example.com", "555-1234", "123 Main St", "DOC-001");

        mockMvc.perform(post("/api/v1/admin/recipients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /admin/recipients: invalid email → 400")
    void createRecipient_invalidEmail_400() throws Exception {
        var request = new CreateRecipientRequest(
                "John Doe", "not-an-email", "555-1234", "123 Main St", "DOC-001");

        mockMvc.perform(post("/api/v1/admin/recipients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /admin/recipients: blank name → 400")
    void createRecipient_blankName_400() throws Exception {
        var request = new CreateRecipientRequest(
                "", "john@example.com", "555-1234", "123 Main St", "DOC-001");

        mockMvc.perform(post("/api/v1/admin/recipients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ── GET /api/v1/admin/recipients ──────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /admin/recipients: ADMIN lists recipients → 200")
    void listRecipients_admin_200() throws Exception {
        when(listRecipientsUseCase.listRecipients()).thenReturn(List.of(sampleRecipient));

        mockMvc.perform(get("/api/v1/admin/recipients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /admin/recipients: empty list → 200 with empty array")
    void listRecipients_empty_200() throws Exception {
        when(listRecipientsUseCase.listRecipients()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/admin/recipients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser(roles = "DRIVER")
    @DisplayName("GET /admin/recipients: DRIVER role is forbidden → 403")
    void listRecipients_driver_403() throws Exception {
        mockMvc.perform(get("/api/v1/admin/recipients"))
                .andExpect(status().isForbidden());
    }

    // ── PUT /api/v1/admin/recipients/{recipientId} ────────────────────────────

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /admin/recipients/{id}: ADMIN updates recipient → 200")
    void updateRecipient_admin_200() throws Exception {
        Recipient updated = Recipient.reconstitute(
                recipientId, "Jane Doe", "jane@example.com", "555-9999",
                "456 Oak Ave", "DOC-002",
                java.time.Instant.now(), java.time.Instant.now());

        when(updateRecipientUseCase.updateRecipient(eq(recipientId), any(), any(), any(), any(), any()))
                .thenReturn(updated);

        var request = new UpdateRecipientRequest(
                "Jane Doe", "jane@example.com", "555-9999", "456 Oak Ave", "DOC-002");

        mockMvc.perform(put("/api/v1/admin/recipients/{id}", recipientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("jane@example.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("PUT /admin/recipients/{id}: not found → 404")
    void updateRecipient_notFound_404() throws Exception {
        when(updateRecipientUseCase.updateRecipient(eq(recipientId), any(), any(), any(), any(), any()))
                .thenThrow(new RecipientNotFoundException(recipientId));

        var request = new UpdateRecipientRequest(
                "Jane Doe", "jane@example.com", "555-9999", "456 Oak Ave", "DOC-002");

        mockMvc.perform(put("/api/v1/admin/recipients/{id}", recipientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // ── DELETE /api/v1/admin/recipients/{recipientId} ─────────────────────────

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /admin/recipients/{id}: ADMIN deletes recipient → 204")
    void deleteRecipient_admin_204() throws Exception {
        doNothing().when(deleteRecipientUseCase).deleteRecipient(recipientId);

        mockMvc.perform(delete("/api/v1/admin/recipients/{id}", recipientId))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /admin/recipients/{id}: recipient not found → 404")
    void deleteRecipient_notFound_404() throws Exception {
        doThrow(new RecipientNotFoundException(recipientId))
                .when(deleteRecipientUseCase).deleteRecipient(recipientId);

        mockMvc.perform(delete("/api/v1/admin/recipients/{id}", recipientId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /admin/recipients/{id}: active packages exist → 409")
    void deleteRecipient_activePackages_409() throws Exception {
        doThrow(new RecipientDeletionNotAllowedException(recipientId))
                .when(deleteRecipientUseCase).deleteRecipient(recipientId);

        mockMvc.perform(delete("/api/v1/admin/recipients/{id}", recipientId))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(roles = "DRIVER")
    @DisplayName("DELETE /admin/recipients/{id}: DRIVER role is forbidden → 403")
    void deleteRecipient_driver_403() throws Exception {
        mockMvc.perform(delete("/api/v1/admin/recipients/{id}", recipientId))
                .andExpect(status().isForbidden());
    }
}
