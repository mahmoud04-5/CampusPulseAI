-- liquibase formatted sql

-- changeset abdelrahman:create-survey_user_answers-table
CREATE TABLE IF NOT EXISTS survey_user_answers (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    questions_answers JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_answer_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );