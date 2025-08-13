-- liquibase formatted sql

-- changeset abdelrahman:create-groups-table
CREATE TABLE IF NOT EXISTS groups (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
    );