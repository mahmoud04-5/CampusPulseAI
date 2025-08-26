-- liquibase formatted sql

-- changeset malak:create-user-suggested-clubs-table

CREATE TABLE user_suggested_clubs (
                                      id BIGSERIAL PRIMARY KEY,
                                      user_id BIGINT NOT NULL,
                                      club_id BIGINT NOT NULL,
                                      interest VARCHAR(255),

                                      CONSTRAINT fk_user_suggested_clubs_user
                                          FOREIGN KEY (user_id) REFERENCES users(id)
                                              ON DELETE CASCADE,

                                      CONSTRAINT fk_user_suggested_clubs_club
                                          FOREIGN KEY (club_id) REFERENCES club(id)
                                              ON DELETE CASCADE
);


