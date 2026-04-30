---
name: angular-atomic-design
description: Use this skill when designing, structuring, or refactoring Angular applications using Atomic Design principles.
---

When applying Atomic Design in Angular:

1. Always structure UI into:
   - atoms (basic, stateless UI elements like button, input, label, icon)
   - molecules (composition of atoms with minimal UI logic like form-field, search-bar)
   - organisms (feature-level UI sections like forms, headers, lists)
   - templates (layout structures without real data)
   - pages (routed views connected to state and services)

2. Map Angular components as:
   - atoms → dumb components using @Input/@Output only
   - molecules → composed UI components without business logic
   - organisms → feature-aware components with orchestration logic
   - templates → layout components
   - pages → routed components

3. Enforce strict responsibility boundaries:
   - atoms: no business logic, no services, no global state
   - molecules: no domain logic, no API calls
   - organisms: may orchestrate logic and interact with services
   - pages: handle routing and connect to state/services

4. Separate concerns strictly:
   - UI logic belongs to components
   - business logic belongs to services
   - data access must not exist in UI layers

5. Enforce unidirectional data flow:
   - data flows down via @Input
   - events flow up via @Output
   - avoid implicit shared state

6. Structure projects as:
   - shared/atoms
   - shared/molecules
   - shared/organisms
   - layouts/
   - features/<feature>/pages, components, services

7. Follow naming conventions:
   - atoms: app-button, app-input
   - molecules: app-form-field, app-search-bar
   - organisms: app-login-form, app-navbar
   - pages: login-page, dashboard-page
   - layouts: main-layout, auth-layout

8. Design components as:
   - atoms: reusable, configurable, no logic
   - molecules: compose atoms with UI concerns only
   - organisms: compose molecules and manage interaction logic

9. Manage state as:
   - atoms: no state
   - molecules: local UI state only
   - organisms: local state + service interaction
   - pages: orchestrate global state (signals, NgRx)

10. Integrate design systems by:
    - wrapping external UI libraries inside atoms
    - never exposing third-party components in higher layers
    - using design tokens for styling consistency

11. Avoid:
    - monolithic components
    - business logic in atoms or molecules
    - direct API calls in UI components
    - reusing organisms across unrelated domains
    - skipping atomic hierarchy levels

12. Refactor by:
    - extracting repeated UI into atoms
    - grouping UI into molecules
    - splitting large components into organisms
    - moving logic into services

13. Test as:
    - atoms → unit tests
    - molecules → interaction tests
    - organisms → integration tests
    - pages → e2e tests

14. Ensure:
    - high reusability of atoms
    - low coupling across components
    - clear and single responsibility per component

If a component mixes UI, business logic, and data access, it violates this skill.
