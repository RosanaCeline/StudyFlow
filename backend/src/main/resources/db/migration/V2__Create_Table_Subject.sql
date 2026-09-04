CREATE TABLE subject (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(250),
    color VARCHAR(20) NOT NULL,
    creation_date TIMESTAMP WITH TIME ZONE NOT NULL,
    situation VARCHAR(30) NOT NULL
);