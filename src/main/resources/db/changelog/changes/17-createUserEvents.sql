-- liquibase formatted sql

-- changeset malak:create-user-events-table
CREATE TABLE IF NOT EXISTS user_events
(
    user_id        BIGINT      NOT NULL,
    event_id       BIGINT      NOT NULL,
    rsvp_date_time TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_user_events PRIMARY KEY (user_id, event_id),
    CONSTRAINT fk_user_events_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_events_event FOREIGN KEY (event_id) REFERENCES event (id)
    );
