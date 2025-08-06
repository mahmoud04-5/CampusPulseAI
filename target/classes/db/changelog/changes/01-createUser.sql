-- liquibase formatted sql

-- changeset abdelrahman:create-users-table
CREATE TABLE IF NOT EXISTS users (
     id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
     first_name VARCHAR(50) NOT NULL,
     last_name VARCHAR(50) NOT NULL,
     email VARCHAR(100) NOT NULL UNIQUE,
     password VARCHAR(255) NOT NULL,
     roles VARCHAR(255),
     isActive BOOLEAN NOT NULL DEFAULT TRUE,
     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);