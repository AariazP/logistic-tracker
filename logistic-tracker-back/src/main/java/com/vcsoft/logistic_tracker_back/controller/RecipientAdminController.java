package com.vcsoft.logistic_tracker_back.controller;

import com.vcsoft.logistic_tracker_back.application.port.input.CreateRecipientUseCase;
import com.vcsoft.logistic_tracker_back.application.port.input.DeleteRecipientUseCase;
import com.vcsoft.logistic_tracker_back.application.port.input.ListRecipientsUseCase;
import com.vcsoft.logistic_tracker_back.application.port.input.UpdateRecipientUseCase;
import com.vcsoft.logistic_tracker_back.dto.request.CreateRecipientRequest;
import com.vcsoft.logistic_tracker_back.dto.request.UpdateRecipientRequest;
import com.vcsoft.logistic_tracker_back.dto.response.RecipientResponse;
import com.vcsoft.logistic_tracker_back.dto.response.RecipientResponseMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/recipients")
public class RecipientAdminController {

    private final CreateRecipientUseCase createRecipientUseCase;
    private final ListRecipientsUseCase listRecipientsUseCase;
    private final UpdateRecipientUseCase updateRecipientUseCase;
    private final DeleteRecipientUseCase deleteRecipientUseCase;
    private final RecipientResponseMapper recipientResponseMapper;

    public RecipientAdminController(CreateRecipientUseCase createRecipientUseCase,
                                    ListRecipientsUseCase listRecipientsUseCase,
                                    UpdateRecipientUseCase updateRecipientUseCase,
                                    DeleteRecipientUseCase deleteRecipientUseCase,
                                    RecipientResponseMapper recipientResponseMapper) {
        this.createRecipientUseCase = createRecipientUseCase;
        this.listRecipientsUseCase = listRecipientsUseCase;
        this.updateRecipientUseCase = updateRecipientUseCase;
        this.deleteRecipientUseCase = deleteRecipientUseCase;
        this.recipientResponseMapper = recipientResponseMapper;
    }

    @PostMapping
    public ResponseEntity<RecipientResponse> createRecipient(@Valid @RequestBody CreateRecipientRequest request) {
        var recipient = createRecipientUseCase.createRecipient(
                request.name(),
                request.email(),
                request.phone(),
                request.address(),
                request.documentNumber()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(recipientResponseMapper.toResponse(recipient));
    }

    @GetMapping
    public ResponseEntity<List<RecipientResponse>> listRecipients() {
        List<RecipientResponse> recipients = listRecipientsUseCase.listRecipients().stream()
                .map(recipientResponseMapper::toResponse)
                .toList();
        return ResponseEntity.ok(recipients);
    }

    @PutMapping("/{recipientId}")
    public ResponseEntity<RecipientResponse> updateRecipient(@PathVariable UUID recipientId,
                                                             @Valid @RequestBody UpdateRecipientRequest request) {
        var recipient = updateRecipientUseCase.updateRecipient(
                recipientId,
                request.name(),
                request.email(),
                request.phone(),
                request.address(),
                request.documentNumber()
        );
        return ResponseEntity.ok(recipientResponseMapper.toResponse(recipient));
    }

    @DeleteMapping("/{recipientId}")
    public ResponseEntity<Void> deleteRecipient(@PathVariable UUID recipientId) {
        deleteRecipientUseCase.deleteRecipient(recipientId);
        return ResponseEntity.noContent().build();
    }
}