# Database Design (ERD)

This document describes the current PostgreSQL schema used by SmartShip.

## Entity Relationship Diagram

```mermaid
erDiagram
    USERS {
        UUID id PK
        VARCHAR username
        VARCHAR password
        VARCHAR role
        TIMESTAMPTZ created_at
    }

    RECIPIENTS {
        UUID id PK
        VARCHAR name
        VARCHAR email
        VARCHAR phone
        VARCHAR address
        VARCHAR document_number
        TIMESTAMPTZ created_at
        TIMESTAMPTZ updated_at
    }

    PACKAGES {
        UUID id PK
        VARCHAR tracking_id
        DOUBLE weight
        VARCHAR dimensions
        VARCHAR status
        TIMESTAMPTZ created_at
        TIMESTAMPTZ updated_at
        UUID recipient_id FK
    }

    RECIPIENTS ||--o{ PACKAGES : receives
```

## Main Tables

- `users`: authentication and authorization users (`ADMIN`, `DRIVER`)
- `recipients`: recipient master data
- `packages`: package lifecycle and tracking data

## Key Constraints

- Primary keys: UUID on all tables
- Foreign key: `packages.recipient_id -> recipients.id`
- Tracking ID should remain unique at database level
- Package lifecycle status is controlled by application state transitions