package com.vcsoft.logistic_tracker_back.infrastructure.config;

import com.vcsoft.logistic_tracker_back.application.usecase.auth.EnsureAdminExistsUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdminBootstrapRunner tests")
class AdminBootstrapRunnerTest {

    @Mock
    private EnsureAdminExistsUseCase ensureAdminExistsUseCase;

    @InjectMocks
    private AdminBootstrapRunner runner;

    @Test
    @DisplayName("run invokes use case with configured credentials")
    void run_invokesUseCase() throws Exception {
        ReflectionTestUtils.setField(runner, "adminUsername", "root");
        ReflectionTestUtils.setField(runner, "adminPassword", "strong-pass");

        runner.run(new DefaultApplicationArguments(new String[0]));

        verify(ensureAdminExistsUseCase).ensureAdminExists("root", "strong-pass");
    }

    @Test
    @DisplayName("run propagates errors from use case")
    void run_propagatesError() {
        ReflectionTestUtils.setField(runner, "adminUsername", "root");
        ReflectionTestUtils.setField(runner, "adminPassword", "strong-pass");
        doThrow(new IllegalStateException("boom")).when(ensureAdminExistsUseCase)
                .ensureAdminExists("root", "strong-pass");

        assertThatThrownBy(() -> runner.run(new DefaultApplicationArguments(new String[0])))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("boom");
    }
}
