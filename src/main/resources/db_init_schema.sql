-- =====================================================
-- Portfolio Backend - Complete Database Schema
-- =====================================================
-- This script creates all MySQL tables for the portfolio application
-- Generated for: portfolio-backend
-- Date: June 7, 2026
-- =====================================================

-- =====================================================
-- 1. USERS TABLE (Main Entity)
-- =====================================================
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    title VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20),
    location VARCHAR(100),
    avatar_key VARCHAR(512) COMMENT 'S3 object key for profile picture',
    summary LONGTEXT COMMENT 'Professional summary',
    experience_years DECIMAL(3,1),
    years_as_senior DECIMAL(3,1),
    projects_delivered INT,
    cloud_infra_projects INT,
    teams_managed_infra INT,
    performance_optimization INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 2. EXPERIENCE TABLE (1:N with users)
-- =====================================================
CREATE TABLE IF NOT EXISTS experience (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    company_name VARCHAR(255),
    job_title VARCHAR(255),
    start_date DATETIME,
    end_date DATETIME,
    currently_working BOOLEAN DEFAULT FALSE,
    description LONGTEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_start_date (start_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 3. EXPERIENCE_HIGHLIGHT TABLE (1:N with users)
-- =====================================================
CREATE TABLE IF NOT EXISTS experience_highlight (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    highlight VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 4. EDUCATION TABLE (1:N with users)
-- =====================================================
CREATE TABLE IF NOT EXISTS education (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    institution_name VARCHAR(255),
    degree VARCHAR(255),
    field_of_study VARCHAR(255),
    start_date DATETIME,
    end_date DATETIME,
    currently_studying BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_institution (institution_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 5. EDUCATION_ACHIEVEMENT TABLE (1:N with users)
-- =====================================================
CREATE TABLE IF NOT EXISTS education_achievement (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    achievement_title VARCHAR(255),
    achievement_description LONGTEXT,
    achievement_date DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_achievement_date (achievement_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 6. CERTIFICATION TABLE (1:N with users)
-- =====================================================
CREATE TABLE IF NOT EXISTS certification (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    certification_name VARCHAR(255),
    issuing_organization VARCHAR(255),
    issue_date DATETIME,
    expiration_date DATETIME,
    credential_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_issuing_org (issuing_organization)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 7. AWARD TABLE (1:N with users)
-- =====================================================
CREATE TABLE IF NOT EXISTS award (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    award_title VARCHAR(255),
    awarding_organization VARCHAR(255),
    date_awarded DATETIME,
    description LONGTEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_date_awarded (date_awarded)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 8. TECHNICAL_EXPERTISE TABLE (1:N with users)
-- =====================================================
CREATE TABLE IF NOT EXISTS technical_expertise (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    skill_name VARCHAR(255),
    proficiency_level VARCHAR(50),
    years_of_experience INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_skill_name (skill_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 9. CORE_COMPETENCY TABLE (1:N with users)
-- =====================================================
CREATE TABLE IF NOT EXISTS core_competency (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    competency_name VARCHAR(255),
    description LONGTEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_competency_name (competency_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 10. PUBLICATION TABLE (1:N with users)
-- =====================================================
CREATE TABLE IF NOT EXISTS publication (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(500),
    publication_date DATETIME,
    publication_url VARCHAR(500),
    publisher VARCHAR(255),
    description LONGTEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_publisher (publisher)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- SUMMARY
-- =====================================================
-- Tables Created: 10
-- Relationships: All tables have foreign keys to users table
-- Charset: UTF8MB4 (supports all Unicode characters)
-- Engine: InnoDB (supports transactions and foreign keys)
-- Timestamps: Auto-populated created_at and updated_at
-- Indexes: Created for foreign keys and common query fields
-- =====================================================

-- Display all created tables
SHOW TABLES;

-- Display users table structure
DESCRIBE users;