-- liquibase formatted sql

-- changeset abdelrahman:create-suggested_organizer_events-table
CREATE TABLE IF NOT EXISTS suggested_organizer_events (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    category VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);