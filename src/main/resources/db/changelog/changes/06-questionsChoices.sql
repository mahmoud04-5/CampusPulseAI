-- liquibase formatted sql

-- changeset abdelrahman:create-questions_choices-table
CREATE TABLE IF NOT EXISTS questions_choices (
    id BIGSERIAL PRIMARY KEY,
    questionId BIGINT NOT NULL,
    choice TEXT NOT NULL,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_question FOREIGN KEY (questionId) REFERENCES survey_questions(id) ON DELETE CASCADE
    );