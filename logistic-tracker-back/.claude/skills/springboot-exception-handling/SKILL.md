---
name: springboot-exception-handling
description: Use this skill when handling, structuring, or refactoring error management in Spring Boot applications to ensure consistency, clarity, and separation of concerns.
---

When handling exceptions in Spring Boot:

1. Centralize exception handling:
   - use @ControllerAdvice for global handling
   - avoid try/catch duplication in controllers
   - return consistent error responses

2. Define exception types:
   - create custom exceptions for business rules (e.g. DomainException, UseCaseException)
   - separate technical exceptions (infrastructure) from business exceptions
   - use meaningful names reflecting the problem

3. Respect Clean Architecture boundaries:
   - domain layer → define business exceptions only
   - use-cases → throw domain/use-case exceptions
   - infrastructure → handle technical exceptions and map them
   - controllers → translate exceptions into HTTP responses

4. Do not leak infrastructure errors:
   - never expose database or framework exceptions to clients
   - map low-level exceptions to domain-level errors

5. Structure error responses:
   - include message, code, and context
   - avoid exposing internal stack traces
   - ensure consistency across all endpoints

6. Use HTTP status codes correctly:
   - 400 → validation errors
   - 404 → resource not found
   - 409 → business conflicts
   - 500 → unexpected errors

7. Handle validation errors:
   - use @Valid and validation annotations
   - capture validation exceptions globally
   - return structured and readable error messages

8. Logging strategy:
   - log errors at the appropriate level (warn/error)
   - include contextual information
   - avoid logging sensitive data

9. Avoid:
   - generic catch(Exception)
   - swallowing exceptions silently
   - duplicating error handling logic
   - returning inconsistent error formats
   - exposing internal implementation details

10. Refactor by:
    - extracting repeated try/catch into global handlers
    - introducing custom exception hierarchy
    - mapping infrastructure errors to domain errors
    - standardizing API error responses

11. Ensure:
    - consistent error handling across the application
    - clear separation between business and technical errors
    - predictable API behavior for clients
    - maintainable and centralized error logic

If exceptions are handled inconsistently, expose internal details, or mix business and technical concerns, it violates this skill.
