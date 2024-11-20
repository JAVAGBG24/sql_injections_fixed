CREATE TABLE users (
id SERIAL PRIMARY KEY,
username VARCHAR(50) NOT NULL UNIQUE,
password VARCHAR(50) NOT NULL
);

INSERT INTO users (username, password) VALUES ('admin', 'password');
INSERT INTO users (username, password) VALUES ('user', 'userpass');

SELECT * FROM users;



# Hasha & Salta lösenord
CREATE EXTENSION pgcrypto;

CREATE TABLE users (
id SERIAL PRIMARY KEY,
username TEXT NOT NULL UNIQUE,
password TEXT NOT NULL
);

INSERT INTO users (username, password) VALUES
('nisse', crypt('pw12345', gen_salt('bf')));

SELECT * FROM users;

SELECT * FROM users
WHERE username = 'nisse'
AND password = crypt('pw12345', password);

SELECT * FROM users
WHERE username = 'nisse'
AND password = crypt('wrongpassword', password);