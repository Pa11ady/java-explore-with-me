DROP TABLE IF EXISTS hits;

CREATE TABLE IF NOT EXISTS hits (
    hit_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app VARCHAR(255) NOT NULL,
    uri VARCHAR(1000) NOT NULL,
    ip VARCHAR(45) NOT NULL,
    timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL
);