DROP TABLE IF EXISTS accidents_rules;
DROP TABLE IF EXISTS rules;
DROP TABLE IF EXISTS accidents;
DROP TABLE IF EXISTS accident_types;

CREATE TABLE IF NOT EXISTS accident_types(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS accidents(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    text TEXT NOT NULL,
    address VARCHAR(255) NOT NULL,
    accident_type_id INT REFERENCES accident_types(id) NOT NULL
);

CREATE TABLE IF NOT EXISTS rules(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS accidents_rules(
    accident_id INT REFERENCES accidents(id) NOT NULL,
    rule_id INT REFERENCES rules(id) NOT NULL
);

INSERT INTO accident_types(name)
VALUES ('Две машины'), ('Машина и человек'), ('Машина и велосипед');

INSERT INTO rules(name)
VALUES ('Статья. 1'), ('Статья. 2'), ('Статья. 3');