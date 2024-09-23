-- 所有货币表
CREATE TABLE currency_reference (
    code CHAR(3) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    symbol VARCHAR(10) NOT NULL,
    decimal_places INT DEFAULT 2 NOT NULL,
    gmt_create TIMESTAMP NOT NULL,
    gmt_modified TIMESTAMP NOT NULL
)

