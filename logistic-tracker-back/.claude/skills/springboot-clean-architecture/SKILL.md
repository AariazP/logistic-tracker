---
name: springboot-clean-architecture
description: Use this skill when designing, structuring, or refactoring Spring Boot applications using Clean Architecture principles.
---

When applying Clean Architecture in Spring Boot:

1. Structure the application into layers (from inner to outer):
   - domain → core business models and rules
   - use-cases (application) → business use cases
   - interface adapters → controllers, presenters, mappers
   - infrastructure → database, frameworks, external services

2. Enforce dependency rule:
   - dependencies must always point inward
   - domain must not depend on any other layer
   - use-cases depend only on domain
   - outer layers depend on inner layers

3. Define domain layer:
   - contains entities and value objects
   - contains core business rules
   - must be framework-independent
   - must not use Spring annotations

4. Define use-cases layer:
   - encapsulates application-specific business logic
   - orchestrates domain entities
   - uses interfaces (ports) for external dependencies
   - must not depend on infrastructure

5. Use ports and adapters:
   - define interfaces (ports) in use-cases layer
   - implement them in infrastructure layer
   - inject implementations via dependency injection

6. Controllers (interface adapters):
   - handle HTTP requests/responses
   - map DTOs to use-case inputs
   - must not contain business logic
   - call use-cases, not services

7. Infrastructure layer:
   - implements repositories, external APIs, frameworks
   - contains Spring Data, REST clients, etc.
   - must depend on abstractions defined in inner layers

8. Data handling:
   - use DTOs for external communication
   - use mappers/adapters to transform data
   - never expose domain entities directly

9. Dependency injection:
   - wire implementations in configuration layer
   - depend on interfaces, not concrete classes

10. Transactions:
    - handled at use-case level
    - avoid placing transaction logic in controllers or infrastructure

11. Avoid:
    - controller → service → repository structure
    - business logic in controllers or infrastructure
    - domain depending on Spring or database
    - direct use of repositories inside controllers

12. Refactor by:
    - extracting business logic into use-cases
    - moving domain logic into domain layer
    - introducing ports for external dependencies
    - isolating infrastructure implementations

13. Ensure:
    - clear separation between business and framework
    - independent and testable domain logic
    - replaceable infrastructure without affecting core logic
    - strict adherence to dependency inversion

If business logic depends on frameworks, databases, or controllers, it violates this skill.
