---
name: springboot-security-jwt
description: Use this skill when implementing authentication and authorization in Spring Boot applications using JWT in a secure and scalable way.
---

When implementing JWT security in Spring Boot:

1. Use stateless authentication:
   - do not use HTTP sessions
   - configure Spring Security as stateless
   - rely entirely on JWT for authentication

2. Structure according to Clean Architecture:
   - domain → authentication rules and user model
   - use-cases → login, token generation, validation
   - adapters → controllers and request/response mapping
   - infrastructure → JWT provider, security filters

3. Token design:
   - include only necessary claims (user id, roles)
   - avoid sensitive data in payload
   - keep tokens small and minimal

4. Token lifecycle:
   - define expiration time (short-lived access tokens)
   - implement refresh token strategy if needed
   - reject expired or malformed tokens

5. Signing and validation:
   - use strong secret keys or asymmetric keys (RSA)
   - never hardcode secrets in code
   - validate signature, expiration, and claims on every request

6. Security filters:
   - implement JWT authentication filter
   - extract token from Authorization header (Bearer)
   - validate token before setting authentication context

7. Password handling:
   - always hash passwords (e.g. BCrypt)
   - never store plain text passwords
   - validate credentials securely

8. Authorization:
   - use roles/authorities from JWT claims
   - enforce access control at endpoint or method level
   - avoid hardcoding authorization logic in controllers

9. Exception handling:
   - handle authentication and authorization errors centrally
   - return proper HTTP status codes (401, 403)
   - do not expose internal security details

10. Configuration:
    - externalize secrets (env variables, vaults)
    - configure CORS properly
    - disable unused security features

11. Avoid:
    - storing JWT in local storage without precautions
    - long-lived tokens without rotation
    - embedding sensitive information in tokens
    - mixing authentication logic with business logic
    - bypassing validation in filters

12. Refactor by:
    - isolating JWT logic into dedicated components
    - introducing clear authentication use-cases
    - separating security configuration from business code
    - removing stateful authentication mechanisms

13. Ensure:
    - stateless and scalable authentication
    - secure token generation and validation
    - clear separation between security and business logic
    - compliance with security best practices

If tokens are not validated properly, secrets are exposed, or authentication logic is mixed with business logic, it violates this skill.
