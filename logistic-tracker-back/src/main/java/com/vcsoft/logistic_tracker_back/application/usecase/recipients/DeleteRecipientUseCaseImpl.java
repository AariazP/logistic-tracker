package com.vcsoft.logistic_tracker_back.application.usecase.recipients;

import com.vcsoft.logistic_tracker_back.application.usecase.recipients.DeleteRecipientUseCase;
import com.vcsoft.logistic_tracker_back.domain.port.out.PackageRepository;
import com.vcsoft.logistic_tracker_back.domain.port.out.RecipientRepository;
import com.vcsoft.logistic_tracker_back.domain.exception.RecipientDeletionNotAllowedException;
import com.vcsoft.logistic_tracker_back.domain.exception.RecipientNotFoundException;
import com.vcsoft.logistic_tracker_back.domain.model.Recipient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteRecipientUseCaseImpl implements DeleteRecipientUseCase {

    private final RecipientRepository recipientRepository;
    private final PackageRepository packageRepository;

    @Override
    public void deleteRecipient(UUID recipientId) {
        Recipient recipient = recipientRepository.findById(recipientId)
                .orElseThrow(() -> new RecipientNotFoundException(recipientId));
        if (packageRepository.existsActiveByRecipientId(recipientId)) {
            throw new RecipientDeletionNotAllowedException(recipientId);
        }
        recipientRepository.delete(recipient);
    }
}