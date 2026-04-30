package com.vcsoft.logistic_tracker_back.application.usecase;

import com.vcsoft.logistic_tracker_back.application.port.input.CreateRecipientUseCase;
import com.vcsoft.logistic_tracker_back.application.port.output.RecipientRepository;
import com.vcsoft.logistic_tracker_back.domain.model.Recipient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateRecipientUseCaseImpl implements CreateRecipientUseCase {

    private final RecipientRepository recipientRepository;

    public CreateRecipientUseCaseImpl(RecipientRepository recipientRepository) {
        this.recipientRepository = recipientRepository;
    }

    @Override
    public Recipient createRecipient(String name, String email, String phone,
                                     String address, String documentNumber) {
        return recipientRepository.save(Recipient.create(name, email, phone, address, documentNumber));
    }
}