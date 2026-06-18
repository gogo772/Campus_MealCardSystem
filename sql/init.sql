-- 校园饭卡管理系统 - 数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS campus_card_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE campus_card_system;

-- 持卡者信息表
CREATE TABLE IF NOT EXISTS card_holder (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    card_number VARCHAR(50) NOT NULL UNIQUE COMMENT '饭卡号',
    name VARCHAR(100) NOT NULL COMMENT '姓名',
    type INT NOT NULL COMMENT '类型（1-学生，2-教职工）',
    balance DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '余额',
    status INT NOT NULL DEFAULT 1 COMMENT '状态（1-正常，2-挂失，3-注销）',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_card_number (card_number),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='持卡者信息表';

-- 交易记录表
CREATE TABLE IF NOT EXISTS transaction_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    card_number VARCHAR(50) NOT NULL COMMENT '饭卡号',
    transaction_type INT NOT NULL COMMENT '交易类型（1-充值，2-消费）',
    amount DECIMAL(10, 2) NOT NULL COMMENT '交易金额',
    before_balance DECIMAL(10, 2) NOT NULL COMMENT '交易前余额',
    after_balance DECIMAL(10, 2) NOT NULL COMMENT '交易后余额',
    transaction_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '交易时间',
    remark VARCHAR(255) COMMENT '备注',
    INDEX idx_card_number (card_number),
    INDEX idx_transaction_type (transaction_type),
    INDEX idx_transaction_time (transaction_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易记录表';

-- 系统用户表（增强版：增加 real_name 字段，添加外键约束）
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '登录账号',
    password VARCHAR(255) NOT NULL COMMENT 'BCrypt加密密码',
    role VARCHAR(20) NOT NULL DEFAULT 'STUDENT' COMMENT '角色：ADMIN/STUDENT/TEACHER',
    real_name VARCHAR(100) COMMENT '用户真实姓名',
    card_number VARCHAR(50) COMMENT '绑定饭卡号',
    enabled INT NOT NULL DEFAULT 1 COMMENT '账号状态 1-启用 0-禁用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_card_number (card_number),
    -- 外键约束：sys_user.card_number 引用 card_holder.card_number，保护数据完整性
    CONSTRAINT fk_user_card FOREIGN KEY (card_number) REFERENCES card_holder(card_number) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- 插入测试数据
INSERT INTO card_holder (card_number, name, type, balance, status) VALUES
('20240001', '张三', 1, 100.00, 1),
('20240002', '李四', 1, 50.00, 1),
('20240003', '王五', 2, 200.00, 1),
('20240004', '赵六', 1, 0.00, 2);

INSERT INTO transaction_record (card_number, transaction_type, amount, before_balance, after_balance, remark) VALUES
('20240001', 1, 100.00, 0.00, 100.00, '初始充值'),
('20240002', 1, 50.00, 0.00, 50.00, '初始充值'),
('20240003', 1, 200.00, 0.00, 200.00, '初始充值');

-- 插入系统用户
-- 重要：Spring Security BCryptPasswordEncoder 只匹配 $2a$ 格式，$2b$ 格式无法登录！
-- 管理员密码: 123456    普通用户密码: 1234
INSERT INTO sys_user (username, password, role, real_name, card_number, enabled) VALUES
('admin',   '$2a$10$YdXKoEZX7mJ4VnJf2ae4fOyDd5u1q4zfDS9GxTaAACYrcSpbcrGs2', 'ADMIN',   '系统管理员', NULL,      1),
('zhangsan','$2a$10$RozUy2rJSqlV4NyLd.lkw.7c2.zkCP/GmgI4lSIOYafgodoCc3Oka','STUDENT', '张三',     '20240001',1),
('lisi',    '$2a$10$RozUy2rJSqlV4NyLd.lkw.7c2.zkCP/GmgI4lSIOYafgodoCc3Oka','STUDENT', '李四',     '20240002',1),
('wangwu',  '$2a$10$RozUy2rJSqlV4NyLd.lkw.7c2.zkCP/GmgI4lSIOYafgodoCc3Oka','TEACHER', '王五',     '20240003',1);
