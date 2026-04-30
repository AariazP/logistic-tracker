---
name: springboot-dto-mapping
description: Use this skill when designing or refactoring data transfer and mapping in Spring Boot applications to ensure separation between API, domain, and infrastructure models.
---

When handling DTO mapping in Spring Boot:

1. Separate models clearly:
   - DTOs → API input/output contracts
   - domain models → business logic representation
   - entities → persistence models
   - never mix these responsibilities

2. Never expose domain or entities:
   - do not return entities in controllers
   - do not accept entities as request payloads
   - always use DTOs at boundaries

3. Map explicitly between layers:
   - controller ↔ DTO
   - use-case ↔ domain
   - repository ↔ entity
   - use mappers/adapters for transformations

4. Keep mapping logic isolated:
   - create dedicated mapper classes
   - avoid mapping inside controllers, services, or repositories
   - ensure mappers have a single responsibility

5. Control data exposure:
   - include only required fields in DTOs
   - avoid over-fetching or leaking internal data
   - tailor DTOs per use-case when necessary

6. Handle transformations:
   - convert types, formats, and structures explicitly
   - manage nullability and defaults safely
   - avoid implicit or hidden transformations

7. Use tools carefully:
   - use MapStruct or manual mapping for clarity and control
   - avoid reflection-based mappers in critical paths
   - ensure mappings are predictable and testable

8. Maintain consistency:
   - follow naming conventions across DTOs and domain models
   - ensure bidirectional mappings are correct if needed

9. Avoid:
   - using entities as DTOs
   - duplicating mapping logic across classes
   - placing mapping logic in controllers or services
   - exposing unnecessary fields
   - tight coupling between layers

10. Refactor by:
    - extracting mapping logic into dedicated mappers
    - introducing DTOs where entities are exposed
    - simplifying complex mappings into smaller functions
    - removing duplicated transformations

11. Test mappings:
    - validate correct transformation between layers
    - test edge cases and null values
    - ensure no data loss or incorrect mapping

12. Ensure:
    - clear separation between layers
    - controlled data flow across boundaries
    - maintainable and reusable mapping logic
    - alignment with Clean Architecture principles

If DTOs, domain models, and entities are mixed, or mapping logic is scattered across layers, it violates this skill.
