-- liquibase formatted sql

-- changeset abdelrahman:create-roles-table
CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
    );