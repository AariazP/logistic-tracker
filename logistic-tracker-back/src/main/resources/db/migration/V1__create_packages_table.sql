-- V1__create_packages_table.sql
CREATE TABLE packages (
    id             UUID         NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    tracking_id    VARCHAR(100) NOT NULL UNIQUE,
    weight         DOUBLE PRECISION NOT NULL CHECK (weight > 0),
    dimensions     VARCHAR(255) NOT NULL,
    recipient_name VARCHAR(255) NOT NULL,
    status         VARCHAR(20)  NOT NULL DEFAULT 'RECEIVED',
    created_at     TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at     TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX idx_packages_status      ON packages (status);
CREATE UNIQUE INDEX idx_packages_tracking_id ON packages (tracking_id);
