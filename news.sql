DROP TABLE IF EXISTS news;
CREATE TABLE news
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    category   INT UNIQUE   NOT NULL,
    title      VARCHAR(255) NOT NULL,
    link       TEXT         NOT NULL,
    date       VARCHAR(50),
    summary    TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);