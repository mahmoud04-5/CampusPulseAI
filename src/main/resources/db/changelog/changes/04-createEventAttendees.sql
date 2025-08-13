-- liquibase formatted sql

-- changeset abdelrahman:create-EventAttendees-table
CREATE TABLE IF NOT EXISTS event_attendees (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    CONSTRAINT fk_attendee_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_attendee_event FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE
    );