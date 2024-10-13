-- 交易类型表
CREATE TABLE IF NOT EXISTS bill_deal_type (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_uuid VARCHAR(36),
    deal_type_color VARCHAR(9),
    deal_type VARCHAR(255) NOT NULL,
    gmt_create DATETIME,
    gmt_modified DATETIME
);

-- 交易渠道表
CREATE TABLE IF NOT EXISTS bill_deal_channal (
    id INT PRIMARY KEY AUTO_INCREMENT,
    deal_channal VARCHAR(255),
    deal_channal_color VARCHAR(9),
    user_uuid VARCHAR(36),
    gmt_create DATETIME,
    gmt_modified DATETIME
);

-- 额度表
CREATE TABLE IF NOT EXISTS bill_budget (
    id INT AUTO_INCREMENT PRIMARY KEY,        -- 预算的唯一标识
    budget_time_type ENUM('WEEKLY', 'MONTHLY', 'YEARLY') NOT NULL,  -- 预算类型：周、月或年
    user_uuid CHAR(36) NOT NULL,             -- 用户的UUID
    category_name VARCHAR(255) NOT NULL,      -- 交易类型、交易渠道或者总额度，通过复合索引加速匹配
    budget_amount DECIMAL(10, 2) NOT NULL,    -- 预算总金额
    home_currency CHAR(3) NOT NULL,            -- 预算的货币种类
    start_date DATE NOT NULL,                 -- 预算的开始日期
    end_date DATE NOT NULL,                   -- 预算的结束日期
    remaining_amount DECIMAL(10, 2) NOT NULL, -- 剩余预算金额
    gmt_create DATETIME,
    gmt_modified DATETIME
);

-- 账单表
CREATE TABLE IF NOT EXISTS bill_transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,          -- 账单的唯一标识
    user_uuid VARCHAR(36) NOT NULL,                -- 用户的UUID
    is_income TINYINT NOT NULL,            -- 交易类型标识，0表示支出，1表示收入
    foreign_amount DECIMAL(10, 2) NOT NULL,     -- 外币金额
    foreign_currency VARCHAR(3) NOT NULL,       -- 外币类型（如USD、EUR等）
    base_amount DECIMAL(10, 2) NOT NULL,       -- 本币金额
    base_currency VARCHAR(3) NOT NULL,         -- 本币类型（如CNY）
    exchange_rate DECIMAL(10, 6) NOT NULL,      -- 使用的汇率
    is_custom_rate TINYINT NOT NULL,          -- 是否使用自定义汇率，0表示否，1表示是
    deal_channal VARCHAR(255) NOT NULL,  -- 交易渠道（如信用卡、现金等）
    deal_type VARCHAR(255) NOT NULL,     -- 交易类型（如餐饮、购物等）
    remarks TEXT,                               -- 备注
    gmt_create DATETIME,
    gmt_modified DATETIME
);

-- 支出累计表
CREATE TABLE IF NOT EXISTS bill_expense_summary (
    id INT AUTO_INCREMENT PRIMARY KEY,       
    budget_time_type ENUM('DAY','WEEKLY', 'MONTHLY', 'YEARLY') NOT NULL,  -- 交易类型：天、周、月或年
    user_uuid CHAR(36) NOT NULL,             -- 用户的UUID
    category_name VARCHAR(255) NOT NULL,      -- 交易类型、交易渠道或者总额度，通过复合索引加速匹配
    summary_amount DECIMAL(10, 2) NOT NULL,    -- 交易总金额
    home_currency CHAR(3) NOT NULL,            -- 货币种类
    start_date DATE NOT NULL,                 -- 开始日期
    end_date DATE NOT NULL,                   -- 结束日期
    gmt_create DATETIME,
    gmt_modified DATETIME
);

-- 收入累计表
CREATE TABLE IF NOT EXISTS bill_income_summary (
    id INT AUTO_INCREMENT PRIMARY KEY,       
    budget_time_type ENUM('DAY','WEEKLY', 'MONTHLY', 'YEARLY') NOT NULL,  -- 交易类型：天、周、月或年
    user_uuid CHAR(36) NOT NULL,             -- 用户的UUID
    category_name VARCHAR(255) NOT NULL,      -- 收入类型、交易渠道或者总额度，通过复合索引加速匹配
    summary_amount DECIMAL(10, 2) NOT NULL,    -- 收入总金额
    home_currency CHAR(3) NOT NULL,            -- 货币种类
    start_date DATE NOT NULL,                 -- 开始日期
    end_date DATE NOT NULL,                   -- 结束日期
    gmt_create DATETIME,
    gmt_modified DATETIME
);
