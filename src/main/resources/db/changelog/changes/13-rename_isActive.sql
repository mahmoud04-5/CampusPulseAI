-- liquibase formatted sql

-- changeset abdelrahman:rename-isActive-to-is_active
ALTER TABLE event
    RENAME COLUMN isActive TO is_active;

ALTER TABLE survey_questions
    RENAME COLUMN isActive TO is_active;

