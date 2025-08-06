-- liquibase formatted sql

-- changeset abdelrahman:create-EventAttendees-table
CREATE TABLE IF NOT EXISTS event_attendees (
    id BIGSERIAL PRIMARY KEY,
    userId BIGINT NOT NULL,
    eventId BIGINT NOT NULL,
    CONSTRAINT fk_attendee_user FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_attendee_event FOREIGN KEY (eventId) REFERENCES event(id) ON DELETE CASCADE
    );