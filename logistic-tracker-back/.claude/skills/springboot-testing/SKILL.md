---
name: springboot-testing
description: Use this skill when writing, structuring, or refactoring tests in Spring Boot applications to ensure reliability, correctness, and architectural alignment.
---

When testing Spring Boot applications:

1. Follow testing levels:
   - unit tests → domain and use-cases (no Spring context)
   - integration tests → adapters, repositories, controllers
   - e2e tests → full application flow

2. Test use-cases (application layer):
   - isolate from framework and infrastructure
   - mock all external dependencies (ports)
   - validate business rules and orchestration
   - test multiple scenarios, not just success

3. Test domain logic:
   - use pure unit tests
   - validate business invariants
   - cover edge cases and invalid states

4. Test controllers:
   - use @WebMvcTest or similar
   - mock use-cases
   - validate request/response mapping
   - verify HTTP status codes and payloads

5. Test repositories:
   - use @DataJpaTest or integration tests
   - validate queries and persistence behavior
   - use in-memory or testcontainers databases

6. Structure tests as:
   - Arrange → setup inputs and mocks
   - Act → execute behavior
   - Assert → verify outputs and side effects

7. Never test only the happy path:
   - include failure scenarios
   - test invalid inputs
   - test edge cases and boundary conditions
   - validate exception handling and error propagation

8. Mock dependencies properly:
   - mock ports in use-cases
   - avoid mocking domain logic
   - use mocks to isolate behavior, not to hide complexity

9. Ensure isolation:
   - tests must not depend on external systems
   - avoid shared mutable state between tests
   - keep tests deterministic and repeatable

10. Validate error handling:
    - assert thrown exceptions
    - verify correct error mapping in controllers
    - test both business and technical failures

11. Avoid:
    - loading full Spring context for unit tests
    - testing implementation details instead of behavior
    - flaky tests dependent on timing or environment
    - duplicating test logic

12. Refactor by:
    - splitting large tests into focused cases
    - removing unnecessary framework dependencies
    - introducing mocks for external dependencies
    - improving test readability and intent

13. Ensure:
    - high coverage of business logic
    - confidence in refactoring
    - alignment with Clean Architecture boundaries
    - fast and reliable test execution

If tests only validate the happy path, depend on real infrastructure, or are tightly coupled to implementation details, they violate this skill.
