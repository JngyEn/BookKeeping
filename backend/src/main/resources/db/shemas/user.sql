-- 用户注册信息表
CREATE TABLE user_account (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255)  NOT NULL,
    is_email_verified  TINYINT(1) DEFAULT 0,
    uuid VARCHAR(36) NOT NULL,
    user_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    password_updated_at DATETIME,
    gmt_create DATETIME,
    gmt_modified DATETIME
)

-- 用户配置表
CREATE TABLE user_config (
    user_uuid INT PRIMARY KEY, 
    gmt_create DATETIME,
    gmt_modified DATETIME,
    home_currency CHAR(3) NOT NULL
)

-- 验证码表
CREATE TABLE user_email_verify_code (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_uuid VARCHAR(36),
    email VARCHAR(255),
    code CHAR(6),
    expire_time DATETIME,
    is_used BOOLEAN DEFAULT FALSE,
    gmt_create DATETIME,
    gmt_modified DATETIME
)

-- 邮箱限制表
CREATE TABLE email_limit (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_uuid VARCHAR(36),
    count int,
    gmt_create DATETIME,
    gmt_modified DATETIME
)

-- 用户常用货币表
CREATE TABLE user_common_currency (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_uuid VARCHAR(36),
    code CHAR(3),
    name VARCHAR(50) NOT NULL,
    symbol VARCHAR(10) NOT NULL,
    decimal_places INT DEFAULT 2 NOT NULL,
    gmt_create TIMESTAMP NOT NULL,
    gmt_modified TIMESTAMP NOT NULL
)