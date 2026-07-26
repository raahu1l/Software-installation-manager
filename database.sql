-- ============================================================
--  Software Management System — Database Setup
--  Run this in MySQL before starting the application
-- ============================================================

CREATE DATABASE IF NOT EXISTS software_manager;
USE software_manager;

-- Main software table
CREATE TABLE IF NOT EXISTS software (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    software_name VARCHAR(150) NOT NULL,
    version       VARCHAR(50)  NOT NULL,
    developer     VARCHAR(150),
    category      VARCHAR(100),
    install_date  DATE,
    system_name   VARCHAR(100)
);

-- Version update history (auto-populated when you update a version)
CREATE TABLE IF NOT EXISTS update_history (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    software_id   INT,
    software_name VARCHAR(150),
    old_version   VARCHAR(50),
    new_version   VARCHAR(50),
    update_date   DATE,
    FOREIGN KEY (software_id) REFERENCES software(id) ON DELETE SET NULL
);

-- Sample data (optional)
INSERT INTO software (software_name, version, developer, category, install_date, system_name) VALUES
('Visual Studio Code', '1.85.0', 'Microsoft',    'IDE',           '2024-01-10', 'DESKTOP-01'),
('Google Chrome',      '120.0',  'Google',        'Browser',       '2024-01-12', 'DESKTOP-01'),
('VLC Media Player',   '3.0.18', 'VideoLAN',      'Media Player',  '2024-01-15', 'LAPTOP-02'),
('MySQL Workbench',    '8.0.34', 'Oracle',        'Database Tool', '2024-01-20', 'DESKTOP-01');


CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    password VARCHAR(100)
);

CREATE TABLE activity_logs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    action_type VARCHAR(100),
    software_name VARCHAR(255),
    action_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);