CREATE TABLE IF NOT EXISTS users (
    id            CHAR(36) PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password_hash VARCHAR(60)  NOT NULL,          -- BCrypt hash (60 chars)
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- One row per winning time, so a user's full history can be listed or ranked.
CREATE TABLE IF NOT EXISTS user_times (
    id            BIGINT       AUTO_INCREMENT PRIMARY KEY,
    user_id       CHAR(36)     NOT NULL,
    difficulty    VARCHAR(20)  NOT NULL,
    time_sec      INT          NOT NULL,
    achieved_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_times_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_times_user_diff (user_id, difficulty, time_sec)
) ENGINE=InnoDB;
