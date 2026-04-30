package com.vcsoft.logistic_tracker_back.application.usecase.recipients;

import com.vcsoft.logistic_tracker_back.application.usecase.recipients.UpdateRecipientUseCase;
import com.vcsoft.logistic_tracker_back.domain.port.out.RecipientRepository;
import com.vcsoft.logistic_tracker_back.domain.exception.RecipientNotFoundException;
import com.vcsoft.logistic_tracker_back.domain.model.Recipient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateRecipientUseCaseImpl implements UpdateRecipientUseCase {

    private final RecipientRepository recipientRepository;

    @Override
    public Recipient updateRecipient(UUID recipientId, String name, String email, String phone,
                                     String address, String documentNumber) {
        Recipient recipient = recipientRepository.findById(recipientId)
                .orElseThrow(() -> new RecipientNotFoundException(recipientId));
        recipient.updateInfo(name, email, phone, address, documentNumber);
        return recipientRepository.save(recipient);
    }
}