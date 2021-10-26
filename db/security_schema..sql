CREATE TABLE authorities(
    id SERIAL PRIMARY KEY,
    authority VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE users(
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    enabled BOOLEAN DEFAULT true,
    authority_id INT NOT NULL REFERENCES authorities(id)
);

INSERT INTO authorities (authority) VALUES ('ROLE_USER');
INSERT INTO authorities (authority) VALUES ('ROLE_ADMIN');

INSERT INTO users (username, password, enabled, authority_id)
VALUES ('root', '$2a$10$EpQ2u0U5iTTmOmk4KALwXuGHnA9bV87QK9gvQ.bE1TwkiGF40ToYW', true,
        (SELECT id FROM authorities WHERE authority = 'ROLE_ADMIN'));