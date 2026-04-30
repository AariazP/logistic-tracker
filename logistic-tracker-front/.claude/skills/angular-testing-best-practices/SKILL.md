---
name: angular-testing-best-practices
description: Use this skill when writing, structuring, or refactoring tests in Angular applications to ensure reliability, maintainability, and coverage.
---

When testing Angular applications:

1. Follow testing levels:
   - unit tests → components, services, pipes
   - integration tests → component + template interaction
   - e2e tests → full user flows

2. Test components by:
   - focusing on behavior, not implementation
   - using TestBed for setup
   - mocking dependencies via providers
   - testing Inputs/Outputs explicitly
   - avoiding real HTTP calls

3. Test services by:
   - isolating logic
   - mocking external dependencies (HttpClient, other services)
   - verifying inputs and outputs
   - covering edge cases and error handling

4. Use Angular testing utilities:
   - TestBed for dependency injection
   - ComponentFixture for DOM interaction
   - fakeAsync/tick for async control
   - waitForAsync for async setup

5. Mock dependencies using:
   - Jasmine spies or Jest mocks
   - dependency injection overrides
   - HttpTestingController for HTTP requests

6. Test data flow:
   - verify @Input changes update the view
   - verify @Output emits correct events
   - ensure no hidden state mutations

7. Structure tests as:
   - Arrange → setup inputs and mocks
   - Act → trigger behavior
   - Assert → verify results

8. Keep tests:
   - deterministic (no randomness)
   - isolated (no shared state)
   - fast (avoid unnecessary rendering)

9. Avoid:
   - testing internal implementation details
   - relying on real APIs or timers
   - overly complex test setups
   - duplicated test logic

10. Use test doubles appropriately:
    - mocks → verify interactions
    - stubs → provide controlled data
    - spies → track calls and arguments

11. Cover critical scenarios:
    - success paths
    - error handling
    - edge cases
    - invalid inputs
    - boundary conditions
    - unexpected states

12. Never test only the happy path:
    - always include failure scenarios
    - validate error handling behavior
    - simulate API failures and timeouts
    - test defensive logic and fallbacks

13. For HTTP testing:
    - use HttpTestingController
    - assert request method, URL, payload
    - flush mock responses (success and error)

14. For reactive code:
    - test observables with controlled emissions
    - verify subscriptions and side effects
    - test error emissions and completion
    - avoid unhandled async behavior

15. Maintain test quality:
    - keep tests readable and simple
    - use descriptive test names
    - refactor tests alongside code changes

16. Ensure:
    - high coverage on business logic
    - confidence in refactoring
    - alignment with application architecture

If a test only validates the happy path and ignores failures, edge cases, or invalid inputs, it violates this skill.
