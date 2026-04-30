package com.vcsoft.logistic_tracker_back.application.port.input;

import com.vcsoft.logistic_tracker_back.domain.model.Recipient;

public interface CreateRecipientUseCase {
    Recipient createRecipient(String name, String email, String phone, String address, String documentNumber);
}