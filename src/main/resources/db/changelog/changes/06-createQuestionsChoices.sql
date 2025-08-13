-- liquibase formatted sql

-- changeset abdelrahman:create-questions_choices-table
CREATE TABLE IF NOT EXISTS questions_choices (
    id BIGSERIAL PRIMARY KEY,
    question_id BIGINT NOT NULL,
    choice TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_question FOREIGN KEY (question_id) REFERENCES survey_questions(id) ON DELETE CASCADE
    );