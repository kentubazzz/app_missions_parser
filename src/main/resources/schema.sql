-- =====================================================
-- H2 Database Schema для архива миссий
-- =====================================================

-- Таблица миссий
CREATE TABLE IF NOT EXISTS missions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mission_id VARCHAR(50) NOT NULL UNIQUE,
    date DATE NOT NULL,
    location VARCHAR(255) NOT NULL,
    outcome VARCHAR(20) NOT NULL,
    damage_cost BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица проклятий
CREATE TABLE IF NOT EXISTS curses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mission_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    threat_level VARCHAR(50) NOT NULL,
    FOREIGN KEY (mission_id) REFERENCES missions(id) ON DELETE CASCADE,
    CONSTRAINT uk_curses_mission_id UNIQUE (mission_id)
);

-- Таблица магов
CREATE TABLE IF NOT EXISTS sorcerers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mission_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    rank VARCHAR(50) NOT NULL,
    FOREIGN KEY (mission_id) REFERENCES missions(id) ON DELETE CASCADE
);

-- Таблица техник
CREATE TABLE IF NOT EXISTS techniques (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mission_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    owner VARCHAR(255) NOT NULL,
    damage BIGINT,
    FOREIGN KEY (mission_id) REFERENCES missions(id) ON DELETE CASCADE
);

-- Таблица экономических оценок
CREATE TABLE IF NOT EXISTS economic_assessments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mission_id BIGINT NOT NULL,
    total_damage_cost BIGINT,
    infrastructure_damage BIGINT,
    commercial_damage BIGINT,
    transport_damage BIGINT,
    recovery_estimate_days INT,
    insurance_covered BOOLEAN,
    FOREIGN KEY (mission_id) REFERENCES missions(id) ON DELETE CASCADE,
    CONSTRAINT uk_economic_mission_id UNIQUE (mission_id)
);

-- Индексы
CREATE INDEX IF NOT EXISTS idx_mission_id ON missions(mission_id);
CREATE INDEX IF NOT EXISTS idx_sorcerers_name ON sorcerers(name);
CREATE INDEX IF NOT EXISTS idx_techniques_owner ON techniques(owner);