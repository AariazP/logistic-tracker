package com.vcsoft.logistic_tracker_back.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateDriverRequest(
        @NotBlank(message = "Username must not be blank")
        @Size(max = 100, message = "Username must be at most 100 characters")
        String username,

        @NotBlank(message = "Password must not be blank")
        @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
        String password
) {
}