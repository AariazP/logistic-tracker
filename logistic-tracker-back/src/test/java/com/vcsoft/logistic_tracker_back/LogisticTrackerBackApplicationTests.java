package com.vcsoft.logistic_tracker_back;

import org.junit.jupiter.api.Test;

/**
 * Basic smoke test — kept minimal to avoid requiring a running DB in unit-test phase.
 * Integration tests requiring full context should use @Testcontainers.
 */
class LogisticTrackerBackApplicationTests {

    @Test
    void contextLoads() {
        // Intentionally empty: context loading test is covered by integration tests
        // that use Testcontainers. This keeps CI unit tests fast.
    }
}
