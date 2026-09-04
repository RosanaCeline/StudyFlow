CREATE TABLE task (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(60) NOT NULL,
    description VARCHAR(100),
    deadline TIMESTAMP WITH TIME ZONE,
    priority VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    creation_date TIMESTAMP WITH TIME ZONE NOT NULL,
    completion_date TIMESTAMP WITH TIME ZONE,
    subject_id BIGINT NOT NULL,
    CONSTRAINT fk_task_subject FOREIGN KEY (subject_id) REFERENCES subject(id) ON DELETE CASCADE
);

CREATE INDEX idx_task_subject_id ON task(subject_id);
CREATE INDEX idx_task_status ON task(status);
CREATE INDEX idx_task_priority ON task(priority);