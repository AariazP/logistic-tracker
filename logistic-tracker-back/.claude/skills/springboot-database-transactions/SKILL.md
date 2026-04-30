---
name: springboot-database-transactions
description: Use this skill when managing database transactions in Spring Boot applications to ensure data consistency, integrity, and correct transactional boundaries.
---

When handling database transactions in Spring Boot:

1. Define transaction boundaries at use-case level:
   - transactions must be controlled in application/use-case layer
   - do not manage transactions in controllers or repositories
   - each use-case defines a clear transactional scope

2. Use @Transactional correctly:
   - apply at use-case/service level only
   - keep transactions as short as possible
   - avoid long-running transactions

3. Ensure atomicity:
   - all operations within a transaction must succeed or fail together
   - rollback on failures and exceptions
   - avoid partial updates

4. Handle propagation:
   - understand default propagation (REQUIRED)
   - use different propagation only when necessary
   - avoid unnecessary nested transactions

5. Manage isolation levels:
   - use default isolation unless specific issues arise
   - adjust for concurrency problems (dirty reads, phantom reads)
   - balance consistency and performance

6. Handle exceptions and rollback:
   - ensure exceptions trigger rollback
   - configure rollback rules for checked exceptions if needed
   - do not swallow exceptions inside transactions

7. Avoid side effects inside transactions:
   - do not call external services (APIs, messaging) inside transactions
   - avoid long blocking operations
   - keep transactions focused on database operations

8. Maintain consistency:
   - enforce invariants within transaction boundaries
   - validate business rules before committing

9. Avoid:
   - transactions in controllers
   - transactions in repository layer
   - long-lived or nested transactions without need
   - mixing transactional logic with external side effects
   - manual transaction management when not required

10. Refactor by:
    - moving transaction boundaries to use-cases
    - splitting large transactions into smaller ones if needed
    - isolating external calls outside transactional scope
    - simplifying transactional flows

11. Ensure:
    - data consistency and integrity
    - predictable transactional behavior
    - clear boundaries aligned with business use-cases
    - minimal performance overhead

If transactions are placed in incorrect layers, are too long, or include external side effects, it violates this skill.
