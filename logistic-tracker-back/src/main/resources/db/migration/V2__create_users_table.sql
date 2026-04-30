-- V2__create_users_table.sql
CREATE TABLE users (
    id           UUID         NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    username     VARCHAR(100) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    role         VARCHAR(20)  NOT NULL DEFAULT 'DRIVER',
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT now()
);

-- Seed default users (passwords are BCrypt of 'admin123' and 'driver123')
INSERT INTO users (username, password, role) VALUES
    ('admin',  '$2a$12$57qmD4ZkvtsrNQuHZdaUpOuxFFvItzMD/bKGhISCkp2g.QhWaT7ey', 'ADMIN'),
    ('driver', '$2a$12$8nOx/N2//4jTwIo.05xMLOcTY9JCHlpPosHjor/HxoBEfWz72SK5K', 'DRIVER');
