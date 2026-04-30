# ── Stage 1: Build ────────────────────────────────────────────────────────────
# Use a plain JDK image and the project's own Gradle wrapper.
# Avoids the gradle:8.x standalone image whose native file-events library
# (libnative-platform-file-events.so) causes a SIGSEGV on linux/aarch64
# (Apple Silicon). -Dorg.gradle.native=false disables the native watcher entirely.
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy wrapper first so dependency-download layer is cached separately
COPY gradlew gradlew.bat ./
COPY gradle/ gradle/
RUN chmod +x gradlew

COPY build.gradle.kts settings.gradle.kts ./

# Pre-fetch dependencies (layer cache) — failure is non-fatal
RUN ./gradlew dependencies --no-daemon -Dorg.gradle.native=false || true

# Copy source and build
COPY src/ src/
RUN ./gradlew bootJar --no-daemon -x test -Dorg.gradle.native=false

# ── Stage 2: Runtime ──────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

# Non-root user for security
RUN addgroup -S smartship && adduser -S smartship -G smartship

# Copy artifact from build stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Set ownership
RUN chown smartship:smartship app.jar

USER smartship

EXPOSE 8080

# Container-aware JVM options
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:+UseContainerSupport"

HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
