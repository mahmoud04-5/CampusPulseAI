ALTER TABLE users
    ADD CONSTRAINT fk_role_group
        FOREIGN KEY (role_group)
            REFERENCES groups (id)
            ON DELETE CASCADE;