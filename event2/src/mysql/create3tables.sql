-- 1. DROP (Delete) existing tables if they exist, to ensure a clean start.
--    (Optional, but recommended if you are restarting development)
DROP TABLE IF EXISTS registrations;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS users;


-- 2. CREATE the USERS table (Base table for User.java and Attendee.java)
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    user_type VARCHAR(50) NOT NULL DEFAULT 'Standard User'
);

-- 3. CREATE the EVENTS table (Main table for Event.java)
CREATE TABLE events (
    event_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    event_date DATE NOT NULL,
    location VARCHAR(255),
    capacity INT,
    fee DECIMAL(10, 2)
);

-- 4. CREATE the REGISTRATIONS table (The linking table for RegistrationDAO.java)
--    This table implements the Many-to-Many relationship between Users and Events.
CREATE TABLE registrations (
    user_id INT NOT NULL,
    event_id INT NOT NULL,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign keys link back to the primary tables
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (event_id) REFERENCES events(event_id),
    
    -- Prevents a user from registering for the same event more than once
    PRIMARY KEY (user_id, event_id) 
);

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100),
    email VARCHAR(100)
);

-- OPTIONAL: Insert Sample Data for Testing
INSERT INTO events (title, event_date, location, capacity, fee) 
VALUES 
('Introduction to JDBC & Spark', '2026-01-20', 'Room 303', 100, 10.00),
('OOP Design Workshop', '2026-02-15', 'Lecture Theatre A', 75, 0.00);

INSERT INTO users (name, email, user_type)
VALUES
('Alice Johnson', 'alice@example.com', 'Registered Attendee'),
('Bob Smith', 'bob@example.com', 'Standard User');