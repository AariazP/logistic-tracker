package com.vcsoft.logistic_tracker_back.application.usecase.recipients;

import com.vcsoft.logistic_tracker_back.application.usecase.recipients.ListRecipientsUseCase;
import com.vcsoft.logistic_tracker_back.domain.port.out.RecipientRepository;
import com.vcsoft.logistic_tracker_back.domain.model.Recipient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ListRecipientsUseCaseImpl implements ListRecipientsUseCase {

    private final RecipientRepository recipientRepository;

    @Override
    public List<Recipient> listRecipients() {
        return recipientRepository.findAll();
    }
}