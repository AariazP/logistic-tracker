package com.vcsoft.logistic_tracker_back.application.usecase;

import com.vcsoft.logistic_tracker_back.application.port.input.ListRecipientsUseCase;
import com.vcsoft.logistic_tracker_back.application.port.output.RecipientRepository;
import com.vcsoft.logistic_tracker_back.domain.model.Recipient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ListRecipientsUseCaseImpl implements ListRecipientsUseCase {

    private final RecipientRepository recipientRepository;

    public ListRecipientsUseCaseImpl(RecipientRepository recipientRepository) {
        this.recipientRepository = recipientRepository;
    }

    @Override
    public List<Recipient> listRecipients() {
        return recipientRepository.findAll();
    }
}