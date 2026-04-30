-- Enforce valid package status transitions and general DB-level validations

-- 1) Core enum-like constraints
ALTER TABLE packages
    ADD CONSTRAINT chk_packages_status_allowed
        CHECK (status IN ('RECEIVED', 'IN_TRANSIT', 'DELIVERED'));

ALTER TABLE users
    ADD CONSTRAINT chk_users_role_allowed
        CHECK (role IN ('ADMIN', 'DRIVER'));

-- 2) Generic non-blank safeguards for text columns
ALTER TABLE packages
    ADD CONSTRAINT chk_packages_tracking_id_not_blank
        CHECK (btrim(tracking_id) <> ''),
    ADD CONSTRAINT chk_packages_dimensions_not_blank
        CHECK (btrim(dimensions) <> '');

ALTER TABLE users
    ADD CONSTRAINT chk_users_username_not_blank
        CHECK (btrim(username) <> ''),
    ADD CONSTRAINT chk_users_password_not_blank
        CHECK (btrim(password) <> '');

ALTER TABLE recipients
    ADD CONSTRAINT chk_recipients_name_not_blank
        CHECK (btrim(name) <> ''),
    ADD CONSTRAINT chk_recipients_email_not_blank
        CHECK (btrim(email) <> ''),
    ADD CONSTRAINT chk_recipients_phone_not_blank
        CHECK (btrim(phone) <> ''),
    ADD CONSTRAINT chk_recipients_address_not_blank
        CHECK (btrim(address) <> ''),
    ADD CONSTRAINT chk_recipients_document_number_not_blank
        CHECK (btrim(document_number) <> '');

-- 3) Optional temporal consistency constraints
ALTER TABLE packages
    ADD CONSTRAINT chk_packages_timestamps_order
        CHECK (updated_at >= created_at);

ALTER TABLE recipients
    ADD CONSTRAINT chk_recipients_timestamps_order
        CHECK (updated_at >= created_at);

-- 4) Trigger function to prevent forbidden package status transitions
CREATE OR REPLACE FUNCTION validate_package_status_transition()
RETURNS trigger AS
$$
BEGIN
    -- Ignore updates where status is unchanged
    IF NEW.status = OLD.status THEN
        RETURN NEW;
    END IF;

    -- Allowed transitions: RECEIVED -> IN_TRANSIT -> DELIVERED
    IF (OLD.status = 'RECEIVED' AND NEW.status = 'IN_TRANSIT')
       OR (OLD.status = 'IN_TRANSIT' AND NEW.status = 'DELIVERED') THEN
        RETURN NEW;
    END IF;

    RAISE EXCEPTION USING
        ERRCODE = '23514',
        MESSAGE = format(
            'Invalid package status transition from %s to %s for package id %s',
            OLD.status,
            NEW.status,
            OLD.id
        );
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_validate_package_status_transition ON packages;

CREATE TRIGGER trg_validate_package_status_transition
BEFORE UPDATE OF status ON packages
FOR EACH ROW
EXECUTE FUNCTION validate_package_status_transition();
