-- liquibase formatted sql

-- changeset abdelrahman:create-group_roles-table
CREATE TABLE IF NOT EXISTS group_roles (
    group_id BIGINT NOT NULL REFERENCES groups(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (group_id, role_id)
    );