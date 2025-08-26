-- liquibase formatted sql

-- changeset abdelrahman:create-user_otp-table
CREATE TABLE IF NOT EXISTS user_otp (
    id BIGSERIAL primary key,
    email VARCHAR(255) NOT NULL UNIQUE,
    otp_code VARCHAR(6) NOT NULL,
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );