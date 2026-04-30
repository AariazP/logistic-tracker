---
name: docker-springboot-best-practices
description: Use this skill when building, optimizing, or deploying Docker images for Spring Boot applications.
---

When building Docker images for Spring Boot:

1. Use multi-stage builds:
   - build stage → compile application (Maven/Gradle)
   - runtime stage → run application with minimal JRE
   - do not include build tools in final image

2. Base images:
   - build stage → maven:3.9-eclipse-temurin-17 or gradle image
   - runtime stage → eclipse-temurin:17-jre-alpine or similar minimal image

3. Build process:
   - copy only required files (pom.xml/build.gradle first)
   - leverage Docker layer caching
   - run build with tests skipped only if handled in CI

4. Artifact handling:
   - copy generated JAR into runtime image
   - do not include source code in final image

5. Security:
   - run container as non-root user
   - minimize installed packages
   - avoid exposing unnecessary ports
   - never include secrets in image

6. Configuration:
   - use environment variables for configuration
   - externalize application properties
   - avoid hardcoding credentials or URLs

7. Ports:
   - expose application port explicitly (e.g. 8080)

8. JVM optimization:
   - configure memory settings (e.g. -Xms, -Xmx)
   - use container-aware JVM options

9. Health checks:
   - define HEALTHCHECK using actuator endpoints
   - ensure readiness and liveness probes are available

10. Logging:
    - log to stdout/stderr
    - do not write logs to local files inside container

11. Image tagging:
    - use semantic versioning (MAJOR.MINOR.PATCH)
    - optionally include commit hash or build metadata

12. Reproducibility:
    - use fixed base image versions
    - ensure deterministic builds
    - avoid latest tags in production

13. Avoid:
    - single-stage Dockerfiles
    - running as root
    - embedding secrets in image
    - large images with unnecessary dependencies
    - tight coupling between image and environment

14. Refactor by:
    - introducing multi-stage builds
    - reducing image size
    - externalizing configuration
    - improving caching strategy

15. Ensure:
    - minimal and secure runtime image
    - fast build and startup times
    - consistent behavior across environments
    - alignment with container best practices

If the image includes unnecessary dependencies, runs as root, or is not reproducible, it violates this skill.
