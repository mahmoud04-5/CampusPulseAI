-- liquibase formatted sql

-- changeset abdelrahman:create-event-table
CREATE TABLE IF NOT EXISTS event (
    id BIGSERIAL PRIMARY KEY,
    club_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    start_date TIMESTAMP NOT NULL,
    location VARCHAR(255) NOT NULL,
    capacity INT NOT NULL,
    category VARCHAR(50) NOT NULL,
    image_url TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_club FOREIGN KEY (club_id) REFERENCES club(id) ON DELETE CASCADE
);