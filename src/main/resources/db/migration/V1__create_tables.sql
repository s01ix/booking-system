-- ========================================
-- Flyway Migration: Create core tables
-- ========================================

-- Użytkownicy
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    phone VARCHAR(15),
    enabled BOOLEAN NOT NULL
);

-- Role użytkowników
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, role)
);

-- Adresy
CREATE TABLE addresses (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    country VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    street VARCHAR(255),
    postal_code VARCHAR(50),
    building_number VARCHAR(50)
);

-- Usługi
CREATE TABLE services (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(500),
    price NUMERIC(10,2) NOT NULL,
    duration_minutes INT NOT NULL,
    active BOOLEAN NOT NULL
);

-- Specjaliści
CREATE TABLE specialists (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    specialization VARCHAR(255) NOT NULL,
    address_id BIGINT REFERENCES addresses(id) ON DELETE SET NULL,
    accepting_appointments BOOLEAN NOT NULL
);

-- Relacja N:M Specjalista ↔ Usługa
CREATE TABLE specialist_services (
    specialist_id BIGINT NOT NULL REFERENCES specialists(id) ON DELETE CASCADE,
    service_id BIGINT NOT NULL REFERENCES services(id) ON DELETE CASCADE,
    PRIMARY KEY (specialist_id, service_id)
);

-- Sloty czasowe
CREATE TABLE available_slots (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    specialist_id BIGINT NOT NULL REFERENCES specialists(id) ON DELETE CASCADE,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    booked BOOLEAN NOT NULL,
    version BIGINT,
    CONSTRAINT unique_specialist_slot UNIQUE (specialist_id, start_time)
);

-- Wizyty
CREATE TABLE appointments (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    slot_id BIGINT NOT NULL UNIQUE REFERENCES available_slots(id) ON DELETE CASCADE,
    service_id BIGINT NOT NULL REFERENCES services(id) ON DELETE CASCADE,
    status VARCHAR(50) NOT NULL,
    notes TEXT
);
