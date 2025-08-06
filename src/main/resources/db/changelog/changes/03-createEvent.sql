-- liquibase formatted sql

-- changeset abdelrahman:create-event-table
CREATE TABLE IF NOT EXISTS event (
    id BIGSERIAL PRIMARY KEY,
    clubId BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    start_date TIMESTAMP NOT NULL,
    location VARCHAR(255) NOT NULL,
    capacity INT NOT NULL,
    category VARCHAR(50) NOT NULL,
    imageUrl TEXT,
    isActive BOOLEAN DEFAULT TRUE,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_club FOREIGN KEY (clubId) REFERENCES club(id) ON DELETE CASCADE
);