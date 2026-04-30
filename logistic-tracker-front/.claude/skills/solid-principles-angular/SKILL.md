---
name: angular-solid-principles
description: Use this skill when designing, refactoring, or evaluating Angular code to enforce SOLID principles and improve maintainability, scalability, and testability.
---

When applying SOLID principles in Angular:

1. Enforce Single Responsibility Principle (SRP):
   - each component, service, or class must have one reason to change
   - components handle UI only
   - services handle business logic and API communication
   - data transformation must be delegated to mappers/adapters
   - never mix UI, logic, and data access in the same unit

2. Enforce Open/Closed Principle (OCP):
   - extend behavior without modifying existing code
   - prefer composition over inheritance
   - use Angular DI to inject alternative implementations
   - use strategy pattern or injection tokens for variability

3. Enforce Liskov Substitution Principle (LSP):
   - implementations must fully respect their contracts
   - interchangeable services must behave consistently
   - avoid partial implementations or unexpected overrides
   - do not introduce breaking behavior in derived classes

4. Enforce Interface Segregation Principle (ISP):
   - create small, focused interfaces
   - avoid fat or multipurpose services
   - split services by responsibility (e.g. auth, profile, permissions)
   - ensure consumers depend only on what they use

5. Enforce Dependency Inversion Principle (DIP):
   - depend on abstractions, not concrete implementations
   - use interfaces or abstract classes
   - use Angular Dependency Injection for wiring
   - avoid direct instantiation using `new`
   - use InjectionToken when interface-based injection is required

6. Structure Angular layers as:
   - components → UI only, no business logic, no API calls
   - services → business logic and API communication
   - adapters/mappers → data transformation
   - models → domain representation

7. Enforce component rules:
   - must not call APIs directly
   - must not contain business logic
   - must communicate via @Input and @Output
   - must delegate logic to services

8. Enforce service rules:
   - must encapsulate a single responsibility
   - must be reusable and testable
   - must not become "god services"
   - must follow interface segregation

9. Enforce data flow:
   - components → services → APIs
   - never invert dependencies
   - avoid circular dependencies

10. Structure projects as:
    - core/services
    - core/abstractions
    - core/tokens
    - features/<feature>/components, pages, services, models, adapters

11. Apply dependency injection correctly:
    - prefer constructor injection
    - use providedIn: 'root' for global services
    - use InjectionToken for abstractions
    - decouple implementations from consumers

12. Avoid:
    - fat components (UI + logic + API)
    - god services with multiple responsibilities
    - direct instantiation of dependencies
    - tight coupling between components and services
    - leaking backend models into UI
    - uncontrolled shared mutable state

13. Refactor by:
    - splitting large components and moving logic to services
    - splitting large services into smaller focused ones
    - introducing interfaces to decouple implementations
    - replacing conditionals with strategy pattern
    - extracting data transformations into adapters

14. Test as:
    - components → shallow tests
    - services → unit tests
    - adapters → pure function tests
    - use DI to mock dependencies

15. Ensure:
    - high cohesion within units
    - low coupling between layers
    - strong separation of concerns
    - improved testability through abstraction

If a component or service mixes responsibilities, depends on concrete implementations, or cannot be easily tested, it violates this skill.
