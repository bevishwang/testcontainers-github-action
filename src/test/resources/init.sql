-- Create a table for storing users
CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    name     TEXT NOT NULL,
    email    TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL
);

-- Create a table for storing products
CREATE TABLE products
(
    id          SERIAL PRIMARY KEY,
    name        TEXT           NOT NULL,
    price       DECIMAL(10, 2) NOT NULL,
    description TEXT           NOT NULL
);
