-- liquibase formatted sql

-- changeset abdelrahman:create-survey_Questions-table
CREATE TABLE IF NOT EXISTS survey_questions (
    id BIGSERIAL PRIMARY KEY,
    question TEXT NOT NULL,
    allow_Mulltiple_Answers BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
