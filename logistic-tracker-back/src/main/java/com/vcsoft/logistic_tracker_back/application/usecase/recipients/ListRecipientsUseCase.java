package com.vcsoft.logistic_tracker_back.application.usecase.recipients;

import com.vcsoft.logistic_tracker_back.domain.model.Recipient;

import java.util.List;

public interface ListRecipientsUseCase {
    List<Recipient> listRecipients();
}