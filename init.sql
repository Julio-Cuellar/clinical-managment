-- Initialize databases for the clinical management system
CREATE DATABASE IF NOT EXISTS medical_auth;
CREATE DATABASE IF NOT EXISTS medical_users;

-- Create a dedicated user for the application (optional)
-- CREATE USER 'clinical_user'@'%' IDENTIFIED BY 'password123';
-- GRANT ALL PRIVILEGES ON medical_auth.* TO 'clinical_user'@'%';
-- GRANT ALL PRIVILEGES ON medical_users.* TO 'clinical_user'@'%';
-- FLUSH PRIVILEGES;