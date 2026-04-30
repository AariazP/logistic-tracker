---
name: angular-strict-typing-interceptors-best-practices
description: Use this skill when writing or refactoring Angular code to enforce strict typing, proper use of interceptors, and framework best practices.
---

When developing Angular applications:

1. Enforce strict typing:
   - always enable `strict` mode in tsconfig
   - avoid `any` and `unknown` unless strictly necessary
   - define explicit interfaces or types for all data structures
   - type all function inputs and outputs
   - use generics for reusable abstractions

2. Model data explicitly:
   - define domain models separate from API contracts
   - use adapters/mappers to transform API responses
   - avoid leaking backend models into UI

3. Handle nullability:
   - avoid unsafe access (use optional chaining or guards)
   - prefer explicit null/undefined handling
   - avoid non-null assertions (`!`) unless guaranteed

4. Use interceptors for cross-cutting concerns:
   - authentication (tokens, headers)
   - error handling
   - logging and tracing
   - request/response transformation

5. Interceptor rules:
   - keep them stateless and focused
   - do not include business logic
   - chain multiple interceptors with clear responsibility
   - handle errors centrally and consistently

6. HTTP best practices:
   - strongly type all HttpClient requests and responses
   - avoid raw `any` responses
   - centralize API communication in services
   - do not call HttpClient directly from components

7. Component best practices:
   - keep components focused on UI logic only
   - use @Input and @Output for communication
   - avoid complex logic inside components
   - use OnPush change detection when possible

8. Service best practices:
   - encapsulate business logic
   - return typed observables
   - avoid side effects unless explicit
   - keep services small and cohesive

9. Reactive patterns:
   - prefer Observables over Promises in Angular context
   - avoid manual subscriptions when possible (use async pipe)
   - manage subscriptions properly to prevent memory leaks
   - use RxJS operators for transformation and composition

10. Configuration and environment:
    - use environment files for configuration
    - avoid hardcoded URLs or secrets
    - inject configuration where needed

11. Error handling:
    - handle HTTP errors in interceptors or services
    - do not ignore errors silently
    - propagate meaningful error messages

12. Avoid:
    - use of `any` in production code
    - business logic inside interceptors
    - direct DOM manipulation
    - tight coupling between layers
    - duplicated logic across services/components

13. Refactor by:
    - replacing `any` with proper types
    - extracting shared logic into services
    - introducing interceptors for repeated concerns
    - isolating transformations into adapters

14. Ensure:
    - type safety across the application
    - consistent API interaction patterns
    - centralized cross-cutting concerns
    - maintainable and predictable codebase

If code uses weak typing, duplicates cross-cutting logic, or mixes responsibilities between components, services, and interceptors, it violates this skill.
