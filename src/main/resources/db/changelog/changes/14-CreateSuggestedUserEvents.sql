-- liquibase formatted sql

-- changeset abdelrahman:create-suggested_user_events-table
CREATE TABLE IF NOT EXISTS suggested_user_events (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_owner FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE Cascade,
    CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE Cascade
    );