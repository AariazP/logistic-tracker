package com.vcsoft.logistic_tracker_back.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateRecipientRequest(
        @NotBlank(message = "Recipient name must not be blank")
        @Size(max = 255, message = "Recipient name must be at most 255 characters")
        String name,

        @NotBlank(message = "Email must not be blank")
        @Email(message = "Email must be valid")
        @Size(max = 255, message = "Email must be at most 255 characters")
        String email,

        @NotBlank(message = "Phone must not be blank")
        @Size(max = 50, message = "Phone must be at most 50 characters")
        String phone,

        @NotBlank(message = "Address must not be blank")
        @Size(max = 255, message = "Address must be at most 255 characters")
        String address,

        @NotBlank(message = "Document number must not be blank")
        @Size(max = 50, message = "Document number must be at most 50 characters")
        String documentNumber
) {
}