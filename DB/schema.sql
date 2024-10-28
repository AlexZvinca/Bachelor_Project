
CREATE TABLE county (
    id VARCHAR(2) PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);


CREATE TABLE user_info (
    id VARCHAR(50) PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(15) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    date_of_birth DATE,
    county_id VARCHAR(2) REFERENCES county(id) ON DELETE SET NULL,
    city VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    cnp VARCHAR(20) UNIQUE NOT NULL,
    role VARCHAR(20) CHECK (role IN ('REQUESTOR', 'AUTHORITY', 'ADMIN')) NOT NULL
);


CREATE TABLE authorization_request (
    id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) REFERENCES user_info(id) ON DELETE CASCADE,
    county_id VARCHAR(2) REFERENCES county(id) ON DELETE SET NULL,
    id_copy TEXT NOT NULL,
    license_plate_number VARCHAR(15) NOT NULL,
    vehicle_identification TEXT NOT NULL,
    description TEXT,
    status VARCHAR(20) CHECK (status IN ('SENT', 'IN_ANALYSIS', 'GRANTED', 'NOT_GRANTED')) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);