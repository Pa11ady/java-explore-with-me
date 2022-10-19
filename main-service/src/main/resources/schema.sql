DROP TABLE IF EXISTS requests;
DROP TABLE IF EXISTS compilation_event;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS compilations;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS categories (
    category_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT uc_category_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS compilations (
    compilation_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title  VARCHAR(120) NOT NULL,
    pinned BOOLEAN,
    CONSTRAINT uc_compilation_title UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    CONSTRAINT uc_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS events (
    event_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    annotation VARCHAR(2000) NOT NULL,
    category_id BIGINT,
    description VARCHAR(7000) NOT NULL,
    title VARCHAR(120) NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE,
    lat REAL,
    lon REAL,
    initiator_id BIGINT,
    created_on TIMESTAMP WITHOUT TIME ZONE,
    paid BOOLEAN,
    participant_limit INT NOT NULL,
    published_on TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN,
    state VARCHAR(20) NOT NULL,
    CONSTRAINT fk_event_category FOREIGN KEY (category_id) REFERENCES categories (category_id),
    CONSTRAINT fk_event_user FOREIGN KEY (initiator_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS compilation_event (
    compilation_id BIGINT REFERENCES compilations (compilation_id),
    event_id BIGINT REFERENCES events (event_id),
    PRIMARY KEY (compilation_id, event_id),
);

CREATE TABLE IF NOT EXISTS requests (
    request_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    created TIMESTAMP WITHOUT TIME ZONE,
    event_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_event_request FOREIGN KEY (event_id) REFERENCES events (event_id),
    CONSTRAINT fk_user_request FOREIGN KEY (requester_id) REFERENCES users (user_id)
);