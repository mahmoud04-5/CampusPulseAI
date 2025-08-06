-- liquibase formatted sql

-- changeset abdelrahman:create-clubs-table
CREATE TABLE IF NOT EXISTS club (
    id BIGSERIAL PRIMARY KEY,
    ownerId BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    logoUrl TEXT,
    isActive BOOLEAN DEFAULT TRUE,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_owner FOREIGN KEY (ownerId) REFERENCES users(id) ON DELETE Cascade
    );