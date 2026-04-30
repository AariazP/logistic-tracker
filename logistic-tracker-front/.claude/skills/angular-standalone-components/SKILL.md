---
name: angular-standalone-components
description: Use this skill when designing or refactoring Angular applications to use standalone components instead of NgModules.
---

When using Angular standalone components:

1. Prefer standalone components over NgModules:
   - use `standalone: true` in all new components
   - avoid creating new NgModules unless strictly necessary

2. Define dependencies explicitly:
   - use `imports` array inside components
   - import only required Angular modules (CommonModule, RouterModule, etc.)
   - avoid implicit or global dependencies

3. Structure application:
   - use standalone components for pages, layouts, and UI elements
   - group related components by feature
   - avoid module-based structure

4. Routing:
   - use standalone components directly in routes
   - prefer lazy loading via `loadComponent`
   - avoid module-based lazy loading

5. Reusability:
   - keep components small and focused
   - design components to be easily reusable
   - avoid tight coupling between components

6. Dependency injection:
   - use `providers` at component level when needed
   - avoid unnecessary global providers
   - scope dependencies appropriately

7. Interoperability:
   - support integration with existing NgModule-based code when required
   - migrate gradually from modules to standalone components

8. Performance:
   - leverage lazy loading for standalone components
   - minimize bundle size by importing only required dependencies

9. Testing:
   - use standalone components directly in TestBed
   - import required dependencies explicitly in tests
   - avoid unnecessary module setup

10. Avoid:
    - creating unnecessary NgModules
    - importing entire modules when only a subset is needed
    - hidden dependencies
    - overly large standalone components

11. Refactor by:
    - converting NgModule components to standalone
    - removing unused modules
    - simplifying dependency graphs
    - isolating reusable components

12. Ensure:
    - explicit dependency management
    - improved modularity and readability
    - easier lazy loading and code splitting
    - alignment with modern Angular architecture

If components rely on NgModules unnecessarily or hide dependencies, it violates this skill.
