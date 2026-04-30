# SmartShip — Logistics Tracking Backend

Production-ready MVP REST API for managing package lifecycle from creation to delivery.
Built with Java 21, Spring Boot 3.x, PostgreSQL, Docker, and JWT Authentication.

---

## Table of Contents

1. [Architecture](#architecture)
2. [Setup & Running](#setup--running)
3. [Environment Variables](#environment-variables)
4. [API Documentation](#api-documentation)
5. [Database Justification (PostgreSQL vs NoSQL)](#database-justification)
6. [AI Skill Log](#ai-skill-log)

---

## Architecture

### Layer Structure

```
src/main/java/com/vcsoft/logistic_tracker_back/
├── controller/            # HTTP adapters (REST controllers)
├── application/
│   ├── port/
│   │   ├── input/         # Use-case interfaces (inbound ports)
│   │   └── output/        # Repository interfaces (outbound ports)
│   └── usecase/           # Application services (use-case implementations)
├── domain/
│   ├── model/             # Aggregate roots and enums (framework-free)
│   ├── state/             # State Pattern: PackageState, concrete states, factory
│   └── exception/         # Domain exceptions (no Spring dependency)
├── infrastructure/
│   └── persistence/
│       ├── entity/        # JPA entities
│       ├── repository/    # Spring Data JPA repositories
│       ├── mapper/        # Entity ↔ Domain mappers
│       └── adapter/       # Output port implementations
├── security/
│   ├── config/            # SecurityFilterChain, AuthenticationProvider
│   ├── filter/            # JwtAuthenticationFilter
│   ├── service/           # UserDetailsService
│   └── util/              # JwtUtil (sign / validate)
├── dto/
│   ├── request/           # Inbound request DTOs with JSR-303 validation
│   └── response/          # Outbound response DTOs + mappers
└── exception/             # GlobalExceptionHandler (@RestControllerAdvice)
```

### Design Principles

| Principle | Application |
|-----------|-------------|
| **SRP** | Each class has one reason to change: controllers map HTTP, use-cases orchestrate, domain enforces rules, adapters translate data |
| **OCP** | New states (e.g. `RETURNED`) can be added by creating a new `PackageState` implementation without touching existing code |
| **LSP** | All `PackageState` implementations are substitutable — each correctly implements the `transition()` contract |
| **ISP** | `PackageUseCase` (input port) and `PackageRepository` (output port) expose only what each consumer needs |
| **DIP** | Use-cases depend on `PackageRepository` interface; the adapter in infrastructure implements it |

### State Pattern

The state machine is implemented without `if-else` chains:

```
PackageState (interface)
├── ReceivedState   → allows: IN_TRANSIT
├── InTransitState  → allows: DELIVERED
└── DeliveredState  → terminal: no transitions
```

Adding a new state (`RETURNED`) requires:
1. Create `ReturnedState implements PackageState`
2. Add `RETURNED` to `PackageStatus` enum
3. Register in `PackageStateFactory`
4. Add Flyway migration — zero changes to existing business logic.

---

## Setup & Running

### Prerequisites

- Docker Desktop 24+
- Docker Compose v2

### 1. Start all services (PostgreSQL + pgAdmin + Backend)

```bash
docker compose up -d
```

This starts:
- **PostgreSQL** on `localhost:5432`
- **pgAdmin** on `http://localhost:5050`
- **Backend API** on `http://localhost:8080`

Flyway migrations run automatically on startup. Default seed users are created.

### 2. Run only the database stack (for local development)

```bash
docker compose up -d postgres pgadmin
```

Then run the backend locally:

```bash
./gradlew bootRun
```

### 3. Connecting pgAdmin to PostgreSQL

1. Open `http://localhost:5050`
2. Login: `admin@smartship.io` / `admin123`
3. The **SmartShip PostgreSQL** server is pre-configured via `pgadmin/servers.json`
4. Enter the database password when prompted: `smartship123`

If the server is not auto-loaded:
- Right-click **Servers** → **Register** → **Server**
- **Connection** tab:
  - Host: `postgres`
  - Port: `5432`
  - Database: `smartship`
  - Username: `smartship`
  - Password: `smartship123`

### 4. Run Tests

```bash
./gradlew test jacocoTestReport
```

HTML coverage report: `build/reports/jacoco/test/html/index.html`

---

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_URL` | `jdbc:postgresql://localhost:5432/smartship` | PostgreSQL JDBC URL |
| `DB_USER` | `smartship` | Database username |
| `DB_PASSWORD` | `smartship123` | Database password |
| `JWT_SECRET` | *(base64 string)* | HMAC-SHA256 signing secret |
| `JWT_EXPIRATION_MS` | `86400000` | Token lifetime in ms (24h) |
| `SERVER_PORT` | `8080` | Backend HTTP port |

> **Security note:** Override `JWT_SECRET` with a cryptographically random 256-bit value in production.

---

## API Documentation

### Authentication

#### POST /api/v1/auth/login

```json
// Request
{ "username": "admin", "password": "admin123" }

// Response 200
{ "token": "<JWT>", "role": "ADMIN" }
```

Seed credentials:

| Username | Password | Role |
|----------|----------|------|
| `admin` | `admin123` | `ADMIN` |
| `driver` | `driver123` | `DRIVER` |

---

### Packages

All package endpoints require `Authorization: Bearer <token>`.

#### POST /api/v1/packages *(ADMIN only)*

Creates a new package in `RECEIVED` status.

```json
// Request
{
  "trackingId": "TRK-2024-001",
  "weight": 2.5,
  "dimensions": "30x20x10cm",
  "recipientName": "John Doe"
}

// Response 201
{
  "id": "uuid",
  "trackingId": "TRK-2024-001",
  "weight": 2.5,
  "dimensions": "30x20x10cm",
  "recipientName": "John Doe",
  "status": "RECEIVED",
  "createdAt": "...",
  "updatedAt": "..."
}
```

**Validation rules:**
- `trackingId`: required, max 100 chars, must be unique
- `weight`: must be > 0
- `dimensions`: required
- `recipientName`: required

---

#### GET /api/v1/packages *(ADMIN + DRIVER)*

List all packages. Optionally filter by status.

```
GET /api/v1/packages
GET /api/v1/packages?status=IN_TRANSIT
```

Valid `status` values: `RECEIVED`, `IN_TRANSIT`, `DELIVERED`

---

#### GET /api/v1/packages/{trackingId} *(ADMIN + DRIVER)*

Fetch a single package by tracking ID.

```
Response 200 → PackageResponse
Response 404 → { "status": 404, "error": "Not Found", ... }
```

---

#### PATCH /api/v1/packages/{trackingId}/status *(DRIVER only)*

Transitions a package to a new status.

```json
// Request
{ "status": "IN_TRANSIT" }

// Response 200 → PackageResponse with updated status
// Response 409 → when transition is forbidden
// Response 404 → when tracking ID not found
```

**Allowed transitions:**

```
RECEIVED → IN_TRANSIT → DELIVERED
```

**Forbidden:**
- `RECEIVED → DELIVERED`
- Any reverse transition
- Any transition from `DELIVERED`

---

### Error Response Format

```json
{
  "status": 409,
  "error": "Conflict",
  "message": "Transition from RECEIVED to DELIVERED is not allowed.",
  "timestamp": "2024-01-15T10:30:00Z",
  "fieldErrors": null
}
```

---

## Database Justification

### Why PostgreSQL (SQL) over NoSQL?

**1. ACID Compliance**

Package state transitions are critical business operations. PostgreSQL guarantees Atomicity, Consistency, Isolation, and Durability. A status update either fully commits or fully rolls back — there is no partial state. NoSQL databases (MongoDB, DynamoDB) offer eventual consistency by default, which is unacceptable when a driver marks a package as `IN_TRANSIT` simultaneously with another process.

**2. Transaction Consistency for State Transitions**

The state machine enforces `RECEIVED → IN_TRANSIT → DELIVERED`. This constraint is enforced at the domain layer, but persistence must guarantee that concurrent writes cannot corrupt the state. PostgreSQL's row-level locking and serializable isolation prevents race conditions. A document store would require application-level distributed locks for the same guarantee.

**3. Relational Integrity**

The data model has clear relational semantics: packages belong to recipients, users have roles, tracking IDs are globally unique. Foreign keys, unique constraints, and check constraints (`weight > 0`) are enforced at the database level — a second line of defense beyond application validation. NoSQL databases rely entirely on application-level enforcement.

**4. Indexing for Logistics Queries**

Logistics boards filter packages by `status` heavily. PostgreSQL's partial and composite indexes provide sub-millisecond query performance at scale. The `idx_packages_status` index is created via Flyway migration (`V1`). Equivalent performance in document stores requires careful manual sharding or denormalization.

**5. Suitability for Logistics Systems**

Logistics tracking is inherently relational: packages → routes → drivers → recipients. The domain grows naturally into joins and aggregate queries (e.g., "how many packages per driver are IN_TRANSIT today?"). PostgreSQL handles this natively with full SQL. A NoSQL store would require pre-computed aggregations or complex application-level joins.

**6. Flyway Schema Evolution**

Migrations are versioned, reproducible, and auditable. Each schema change (e.g., adding `RETURNED` state) is a new SQL migration file — reviewable in pull requests, reversible, and testable. NoSQL schema evolution is implicit and harder to govern in teams.

**Engineering Decision:** PostgreSQL was chosen for its battle-tested reliability in financial and logistics systems, strong consistency guarantees, and alignment with the domain's relational nature.

---

## AI Skill Log

### Skills Loaded

| Skill | Purpose |
|-------|---------|
| `springboot-clean-architecture` | Enforced layered structure with inward dependency rule; domain is framework-free |
| `springboot-security-jwt` | Stateless JWT filter, externalized secrets, BCrypt passwords, role-based access |
| `springboot-dto-mapping` | Strict DTO ↔ Domain ↔ Entity separation; dedicated mapper classes |
| `springboot-exception-handling` | Centralized `@RestControllerAdvice`; no stack traces in responses; HTTP codes per error type |
| `springboot-testing` | Pure unit tests for domain/use-cases; `@WebMvcTest` for controllers; multiple failure scenarios |
| `docker-springboot-best-practices` | Multi-stage Dockerfile; non-root user; container-aware JVM; HEALTHCHECK |

---

### Generated Code — Refactored or Rejected

#### 1. State Transitions: `if-else` → State Pattern

**Initial approach considered:** Using an `if-else` chain inside `PackageUseCaseImpl` to validate transitions:

```java
// REJECTED — violates OCP and State Pattern requirement
if (current == RECEIVED && next == IN_TRANSIT) { ... }
else if (current == IN_TRANSIT && next == DELIVERED) { ... }
else throw new InvalidStateTransitionException(...);
```

**Why rejected:**
- Violates Open/Closed Principle — adding a new state requires modifying this method
- Business logic lives in the application service, not the domain
- Brittle: every new state multiplies if-else branches

**Refactored to:** Full State Pattern (`ReceivedState`, `InTransitState`, `DeliveredState` each implementing `PackageState.transition()`). New states require zero changes to existing classes.

---

#### 2. Entity Exposure: Direct Entity Returns → DTO Pattern

**Initial approach considered:** Returning `PackageEntity` directly from controllers (common shortcut).

**Why rejected:**
- Leaks infrastructure concerns (JPA annotations, lazy-load proxies) to API consumers
- Couples API contract to DB schema — any column rename breaks clients
- Violates the `springboot-dto-mapping` skill and Clean Architecture boundary rules

**Refactored to:** `PackageResponse` record as the API contract, `PackageResponseMapper` for transformation, `PackagePersistenceMapper` for entity ↔ domain mapping.

---

#### 3. Security: Hardcoded Secret → Externalized Configuration

**Initial approach considered:** Embedding the JWT secret directly in the source code as a constant.

**Why rejected:**
- Critical security vulnerability — secrets in source control
- Violates `springboot-security-jwt` skill rule: "never hardcode secrets in code"
- Prevents secret rotation without a code deployment

**Refactored to:** `@Value("${jwt.secret}")` backed by `application.properties` property which reads from the `JWT_SECRET` environment variable. Docker Compose injects the env variable; production systems should use a secrets manager (Vault, AWS Secrets Manager).

---

#### 4. Session Management: Stateful → Stateless

**Initial approach considered:** Default Spring Security session-based authentication.

**Why rejected:**
- Incompatible with horizontal scaling (sessions are node-local)
- Contradicts the stateless REST API requirement
- `springboot-security-jwt` skill mandates stateless configuration

**Refactored to:** `SessionCreationPolicy.STATELESS` + JWT filter chain. Authentication state lives entirely in the signed token.

---

#### 5. Exception Handling: Controller-level try/catch → Global Handler

**Initial approach considered:** Wrapping use-case calls in try/catch inside each controller.

**Why rejected:**
- Duplicates error-mapping logic across N controllers
- Inconsistent error response formats
- Violates `springboot-exception-handling` skill: centralize all handling

**Refactored to:** `GlobalExceptionHandler` with `@RestControllerAdvice` handling `PackageNotFoundException`, `InvalidStateTransitionException`, `MethodArgumentNotValidException`, and generic `Exception` with appropriate HTTP status codes.

---

*SmartShip Backend — Built following Clean Architecture, SOLID principles, and production security practices.*
