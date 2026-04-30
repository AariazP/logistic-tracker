---
name: springboot-solid-principles
description: Use this skill when designing or refactoring Spring Boot applications to enforce SOLID principles within a Clean Architecture context.
---

When applying SOLID principles in Spring Boot:

1. Enforce Single Responsibility Principle (SRP):
   - each class must have one reason to change
   - controllers handle HTTP only
   - use-cases handle business logic
   - repositories handle persistence only
   - mappers handle data transformation
   - avoid mixing responsibilities across layers

2. Enforce Open/Closed Principle (OCP):
   - extend behavior without modifying existing code
   - use interfaces (ports) to allow new implementations
   - prefer composition over inheritance
   - add new features via new classes, not modifying core logic

3. Enforce Liskov Substitution Principle (LSP):
   - implementations must fully respect their contracts
   - repository implementations must behave consistently
   - do not break expected behavior in derived classes
   - avoid partial or unsupported implementations

4. Enforce Interface Segregation Principle (ISP):
   - define small, focused interfaces (ports)
   - avoid large, generic service interfaces
   - split responsibilities into multiple interfaces
   - ensure clients depend only on required methods

5. Enforce Dependency Inversion Principle (DIP):
   - depend on abstractions, not implementations
   - define ports in inner layers (use-cases/domain)
   - implement them in outer layers (infrastructure)
   - use Spring DI only for wiring, not for defining architecture

6. Apply SOLID across layers:
   - domain → pure business logic, no framework dependencies
   - use-cases → orchestrate logic via interfaces
   - adapters → translate between external and internal models
   - infrastructure → implements external concerns

7. Controllers:
   - depend only on use-cases
   - must not contain business logic
   - must not depend on repositories or infrastructure

8. Use-cases:
   - must be independent of frameworks
   - must depend on interfaces (ports)
   - must encapsulate a single business use case

9. Repositories:
   - must implement interfaces defined in inner layers
   - must not contain business rules
   - must be replaceable without affecting use-cases

10. Avoid:
    - fat classes with multiple responsibilities
    - direct dependency on concrete implementations
    - mixing business logic with framework code
    - large interfaces with unrelated methods
    - tight coupling between layers

11. Refactor by:
    - splitting classes with multiple responsibilities
    - introducing interfaces for dependencies
    - moving business logic into use-cases
    - extracting infrastructure concerns from core logic

12. Ensure:
    - high cohesion within classes
    - low coupling between layers
    - replaceable infrastructure
    - testable business logic independent of Spring

If a class mixes responsibilities, depends on concrete implementations, or cannot be replaced without breaking behavior, it violates this skill.
