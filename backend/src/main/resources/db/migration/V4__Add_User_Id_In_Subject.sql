ALTER TABLE subject
ADD COLUMN user_id BIGINT NOT NULL,
ADD CONSTRAINT fk_subject_user
    FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE CASCADE;

CREATE INDEX idx_subject_user_id ON subject(user_id);