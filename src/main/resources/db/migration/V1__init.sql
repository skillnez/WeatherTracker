CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    login VARCHAR(128) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);
CREATE TABLE locations (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    latitude DECIMAL(8, 5) NOT NULL,
    longitude DECIMAL(8, 5) NOT NULL
);
CREATE TABLE sessions (
    id UUID PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    expires_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_locations_user_id ON locations(user_id);