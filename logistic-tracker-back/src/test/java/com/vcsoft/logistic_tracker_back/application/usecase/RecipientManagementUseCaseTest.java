package com.vcsoft.logistic_tracker_back.application.usecase;

import com.vcsoft.logistic_tracker_back.application.port.output.PackageRepository;
import com.vcsoft.logistic_tracker_back.application.port.output.RecipientRepository;
import com.vcsoft.logistic_tracker_back.domain.exception.RecipientDeletionNotAllowedException;
import com.vcsoft.logistic_tracker_back.domain.exception.RecipientNotFoundException;
import com.vcsoft.logistic_tracker_back.domain.model.Recipient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Recipient management use case tests")
class RecipientManagementUseCaseTest {

    @Mock
    private RecipientRepository recipientRepository;

    @Mock
    private PackageRepository packageRepository;

    @InjectMocks
    private CreateRecipientUseCaseImpl createRecipientUseCase;

    @InjectMocks
    private UpdateRecipientUseCaseImpl updateRecipientUseCase;

    @InjectMocks
    private DeleteRecipientUseCaseImpl deleteRecipientUseCase;

    private Recipient recipient;

    @BeforeEach
    void setUp() {
        recipient = Recipient.create("John Doe", "john@example.com", "555", "Street 9", "DOC-2");
    }

    @Test
    @DisplayName("updateRecipient: updates existing recipient info")
    void updateRecipient_success() {
        when(recipientRepository.findById(recipient.getId())).thenReturn(Optional.of(recipient));
        when(recipientRepository.save(recipient)).thenReturn(recipient);

        Recipient result = updateRecipientUseCase.updateRecipient(
                recipient.getId(),
                "Jane Doe",
                "jane@example.com",
                "999",
                "Street 10",
                "DOC-3"
        );

        assertThat(result.getName()).isEqualTo("Jane Doe");
        assertThat(result.getEmail()).isEqualTo("jane@example.com");
    }

    @Test
    @DisplayName("deleteRecipient: blocks deletion when active packages exist")
    void deleteRecipient_withActivePackages_throws() {
        when(recipientRepository.findById(recipient.getId())).thenReturn(Optional.of(recipient));
        when(packageRepository.existsActiveByRecipientId(recipient.getId())).thenReturn(true);

        assertThatThrownBy(() -> deleteRecipientUseCase.deleteRecipient(recipient.getId()))
                .isInstanceOf(RecipientDeletionNotAllowedException.class);

        verify(recipientRepository, never()).delete(recipient);
    }

    @Test
    @DisplayName("deleteRecipient: throws when recipient does not exist")
    void deleteRecipient_notFound() {
        when(recipientRepository.findById(recipient.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteRecipientUseCase.deleteRecipient(recipient.getId()))
                .isInstanceOf(RecipientNotFoundException.class);
    }

    // ── createRecipient ───────────────────────────────────────────────────────

    @Test
    @DisplayName("createRecipient: saves and returns new recipient")
    void createRecipient_success() {
        when(recipientRepository.save(any(Recipient.class))).thenAnswer(inv -> inv.getArgument(0));

        Recipient result = createRecipientUseCase.createRecipient(
                "Alice", "alice@example.com", "111", "Elm Street", "DOC-10");

        assertThat(result.getName()).isEqualTo("Alice");
        assertThat(result.getEmail()).isEqualTo("alice@example.com");
        assertThat(result.getDocumentNumber()).isEqualTo("DOC-10");
        verify(recipientRepository).save(any(Recipient.class));
    }

    @Test
    @DisplayName("listRecipients: returns all recipients from repository")
    void listRecipients_returnsAll() {
        when(recipientRepository.findAll()).thenReturn(List.of(recipient));

        List<Recipient> results = new ListRecipientsUseCaseImpl(recipientRepository).listRecipients();

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("John Doe");
    }
}