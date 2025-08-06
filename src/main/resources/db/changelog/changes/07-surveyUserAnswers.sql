-- liquibase formatted sql

-- changeset abdelrahman:create-survey_user_answers-table
CREATE TABLE IF NOT EXISTS survey_user_answers (
    id BIGSERIAL PRIMARY KEY,
    userId BIGINT NOT NULL,
    questions_answers JSONB,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_answer_user FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE
    );