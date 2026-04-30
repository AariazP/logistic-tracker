---
name: dockerfile-angular-correct-usage
description: Use this skill when building Docker images for Angular applications to ensure efficient, secure, and production-ready deployments.
---

When building Docker images for Angular:

1. Use multi-stage builds:
   - build stage → compile Angular app (Node image)
   - runtime stage → serve static files (Nginx or similar)

2. Base images:
   - build stage: node:lts-alpine
   - runtime stage: nginx:alpine

3. Build rules:
   - run `npm ci` instead of `npm install`
   - use production build (`ng build --configuration production`)
   - output to `/dist`

4. Runtime rules:
   - serve static files via Nginx
   - configure fallback to `index.html` for SPA routing
   - remove default Nginx config and replace with custom one

5. Security:
   - avoid unnecessary packages in final image
   - use minimal base images (alpine)
   - do not include source code in runtime image
   - do not store secrets in the image

6. Ports:
   - expose port 80 (or configured Nginx port)

7. Configuration:
   - use environment variables for runtime config when needed
   - avoid hardcoding API URLs in build when flexibility is required

8. Caching optimization:
   - copy package.json/package-lock.json first
   - install dependencies before copying full source
   - leverage Docker layer caching

9. Image tagging:
   - use semantic versioning (MAJOR.MINOR.PATCH)
   - optionally include build metadata (e.g. commit SHA)

10. Reproducibility:
    - ensure consistent builds via lock files
    - avoid non-deterministic installs

11. Avoid:
    - single-stage Dockerfiles
    - using `npm install` in production builds
    - shipping development dependencies
    - exposing Node server for static Angular apps
    - embedding secrets or environment-specific configs

Docker images must be minimal, reproducible, and optimized for static delivery.
Always prioritize performance, caching efficiency, and security.
