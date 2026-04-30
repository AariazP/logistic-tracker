package com.vcsoft.logistic_tracker_back.application.usecase;

import com.vcsoft.logistic_tracker_back.application.port.input.DeleteRecipientUseCase;
import com.vcsoft.logistic_tracker_back.application.port.output.PackageRepository;
import com.vcsoft.logistic_tracker_back.application.port.output.RecipientRepository;
import com.vcsoft.logistic_tracker_back.domain.exception.RecipientDeletionNotAllowedException;
import com.vcsoft.logistic_tracker_back.domain.exception.RecipientNotFoundException;
import com.vcsoft.logistic_tracker_back.domain.model.Recipient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class DeleteRecipientUseCaseImpl implements DeleteRecipientUseCase {

    private final RecipientRepository recipientRepository;
    private final PackageRepository packageRepository;

    public DeleteRecipientUseCaseImpl(RecipientRepository recipientRepository,
                                      PackageRepository packageRepository) {
        this.recipientRepository = recipientRepository;
        this.packageRepository = packageRepository;
    }

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