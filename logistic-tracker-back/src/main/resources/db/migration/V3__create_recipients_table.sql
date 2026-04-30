CREATE TABLE recipients (
    id              UUID         NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NOT NULL DEFAULT 'pending@example.com',
    phone           VARCHAR(50)  NOT NULL DEFAULT 'pending',
    address         VARCHAR(255) NOT NULL DEFAULT 'pending',
    document_number VARCHAR(50)  NOT NULL DEFAULT 'pending',
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ  NOT NULL DEFAULT now()
);

INSERT INTO recipients (name)
SELECT DISTINCT recipient_name
FROM packages;

ALTER TABLE packages
    ADD COLUMN recipient_id UUID;

UPDATE packages p
SET recipient_id = r.id
FROM recipients r
WHERE r.name = p.recipient_name;

ALTER TABLE packages
    ALTER COLUMN recipient_id SET NOT NULL;

ALTER TABLE packages
    ADD CONSTRAINT fk_packages_recipients
        FOREIGN KEY (recipient_id) REFERENCES recipients (id);

CREATE INDEX idx_packages_recipient_id ON packages (recipient_id);

ALTER TABLE packages
    DROP COLUMN recipient_name;