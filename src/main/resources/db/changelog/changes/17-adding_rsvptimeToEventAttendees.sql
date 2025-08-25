-- liquibase formatted sql

-- changeset malak:create-user-events-table
DROP TABLE IF EXISTS user_events;
ALTER TABLE event_attendees
    ADD COLUMN rsvp_date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
