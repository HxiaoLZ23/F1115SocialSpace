-- =============================================
-- F1115 社交媒体平台数据库初始化脚本
-- 数据库版本: MySQL 8.0+
-- 创建日期: 2026-01-17
-- =============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `f1115_db` 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE `f1115_db`;

-- =============================================
-- 1. 用户信息表
-- =============================================
DROP TABLE IF EXISTS `user_profile`;
CREATE TABLE `user_profile` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名（唯一）',
  `password` VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
  `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `bio` VARCHAR(500) DEFAULT NULL COMMENT '个人简介',
  `status` TINYINT(1) DEFAULT 1 COMMENT '状态：0-禁用 1-正常',
  `role` TINYINT(1) DEFAULT 0 COMMENT '角色：0-普通用户 1-管理员 2-AI机器人',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_email` (`email`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表';

-- =============================================
-- 2. 关注关系表
-- =============================================
DROP TABLE IF EXISTS `follow_relation`;
CREATE TABLE `follow_relation` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `follower_id` BIGINT(20) NOT NULL COMMENT '关注者ID',
  `following_id` BIGINT(20) NOT NULL COMMENT '被关注者ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '关注时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_follow` (`follower_id`, `following_id`),
  KEY `idx_follower` (`follower_id`),
  KEY `idx_following` (`following_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='关注关系表';

-- =============================================
-- 3. 帖子表
-- =============================================
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '帖子ID',
  `user_id` BIGINT(20) NOT NULL COMMENT '发布者ID',
  `content` TEXT NOT NULL COMMENT '帖子内容',
  `images` JSON DEFAULT NULL COMMENT '图片列表（JSON数组）',
  `type` TINYINT(1) DEFAULT 0 COMMENT '类型：0-普通 1-置顶 2-AI生成',
  `status` TINYINT(1) DEFAULT 1 COMMENT '状态：0-删除 1-正常 2-审核中 3-违规',
  `like_count` INT(11) DEFAULT 0 COMMENT '点赞数',
  `comment_count` INT(11) DEFAULT 0 COMMENT '评论数',
  `view_count` INT(11) DEFAULT 0 COMMENT '浏览量',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_status` (`status`),
  KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='帖子表';

-- =============================================
-- 4. 评论表
-- =============================================
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `post_id` BIGINT(20) NOT NULL COMMENT '帖子ID',
  `user_id` BIGINT(20) NOT NULL COMMENT '评论者ID',
  `parent_id` BIGINT(20) DEFAULT NULL COMMENT '父评论ID（回复时使用）',
  `content` TEXT NOT NULL COMMENT '评论内容',
  `like_count` INT(11) DEFAULT 0 COMMENT '点赞数',
  `status` TINYINT(1) DEFAULT 1 COMMENT '状态：0-删除 1-正常 2-审核中 3-违规',
  `type` TINYINT(1) DEFAULT 0 COMMENT '类型：0-普通 1-AI生成',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_post_id` (`post_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

-- =============================================
-- 5. 帖子点赞表
-- =============================================
DROP TABLE IF EXISTS `post_like`;
CREATE TABLE `post_like` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `post_id` BIGINT(20) NOT NULL COMMENT '帖子ID',
  `user_id` BIGINT(20) NOT NULL COMMENT '点赞用户ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_post_user` (`post_id`, `user_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='帖子点赞表';

-- =============================================
-- 6. 评论点赞表
-- =============================================
DROP TABLE IF EXISTS `comment_like`;
CREATE TABLE `comment_like` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `comment_id` BIGINT(20) NOT NULL COMMENT '评论ID',
  `user_id` BIGINT(20) NOT NULL COMMENT '点赞用户ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_comment_user` (`comment_id`, `user_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论点赞表';

-- =============================================
-- 7. AI机器人配置表
-- =============================================
DROP TABLE IF EXISTS `ai_robot`;
CREATE TABLE `ai_robot` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '机器人ID',
  `user_id` BIGINT(20) NOT NULL COMMENT '关联用户ID',
  `name` VARCHAR(50) NOT NULL COMMENT '机器人名称',
  `api_key` VARCHAR(255) DEFAULT NULL COMMENT 'API密钥（加密存储）',
  `model` VARCHAR(50) DEFAULT 'qwen-plus' COMMENT '模型类型',
  `status` TINYINT(1) DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
  `config` JSON DEFAULT NULL COMMENT '配置信息（JSON）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI机器人配置表';

-- =============================================
-- 8. 内容审核记录表
-- =============================================
DROP TABLE IF EXISTS `content_audit`;
CREATE TABLE `content_audit` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `content_type` TINYINT(1) NOT NULL COMMENT '内容类型：1-帖子 2-评论',
  `content_id` BIGINT(20) NOT NULL COMMENT '内容ID',
  `audit_status` TINYINT(1) DEFAULT 2 COMMENT '审核状态：0-通过 1-拒绝 2-待审核',
  `audit_result` VARCHAR(500) DEFAULT NULL COMMENT '审核结果说明',
  `auditor_id` BIGINT(20) DEFAULT NULL COMMENT '审核者ID（AI或管理员）',
  `audit_type` TINYINT(1) DEFAULT 0 COMMENT '审核类型：0-自动 1-人工',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '审核时间',
  PRIMARY KEY (`id`),
  KEY `idx_content` (`content_type`, `content_id`),
  KEY `idx_audit_status` (`audit_status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='内容审核记录表';

-- =============================================
-- 初始化数据
-- =============================================

-- 注意：以下密码都是使用BCrypt加密的
-- admin123 -> $2a$10$X5wQ7GgZ7J9Z5Z5Z5Z5Z5uK5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Zu
-- 123456 -> $2a$10$wbTuky390AQyxRLLkbx2KepvnZN1hVVeO61ziniCsJot0YixJm6DO

-- 方案1：使用你注册用户时生成的真实BCrypt密码
-- 请先注册一个测试账号，然后从数据库中复制真实的BCrypt密码，替换下面的密码

-- 方案2：暂时使用明文密码（不推荐，仅用于测试）
-- 你可以先手动在数据库中插入，或者直接使用注册功能创建测试用户

-- 插入管理员账号（密码: admin123）
-- 注意：这个密码需要你先通过注册功能注册一个账号，然后复制真实的BCrypt哈希值
INSERT INTO `user_profile` (`username`, `password`, `nickname`, `avatar`, `email`, `bio`, `status`, `role`) 
VALUES 
('admin', '$2a$10$wbTuky390AQyxRLLkbx2KepvnZN1hVVeO61ziniCsJot0YixJm6DO', '系统管理员', NULL, 'admin@f1115.com', '系统管理员账号', 1, 1),
('ai_robot', '$2a$10$wbTuky390AQyxRLLkbx2KepvnZN1hVVeO61ziniCsJot0YixJm6DO', 'AI小助手', NULL, 'ai@f1115.com', '我是AI机器人，为大家提供有趣的内容', 1, 2);

-- 插入测试用户（密码: 123456）
INSERT INTO `user_profile` (`username`, `password`, `nickname`, `avatar`, `email`, `bio`, `status`, `role`) 
VALUES 
('testuser1', '$2a$10$wbTuky390AQyxRLLkbx2KepvnZN1hVVeO61ziniCsJot0YixJm6DO', '测试用户1', NULL, 'test1@f1115.com', '这是测试用户1', 1, 0),
('testuser2', '$2a$10$wbTuky390AQyxRLLkbx2KepvnZN1hVVeO61ziniCsJot0YixJm6DO', '测试用户2', NULL, 'test2@f1115.com', '这是测试用户2', 1, 0);

-- 插入测试帖子
INSERT INTO `post` (`user_id`, `content`, `type`, `status`, `like_count`, `comment_count`, `view_count`) 
VALUES 
(3, '欢迎来到F1115社交平台！这是第一条测试帖子。', 0, 1, 5, 2, 100),
(4, '今天天气真好，适合出去走走！', 0, 1, 3, 1, 50),
(2, 'AI生成的内容：大家好，我是AI小助手，很高兴认识大家！', 2, 1, 10, 5, 200);

-- 插入测试评论
INSERT INTO `comment` (`post_id`, `user_id`, `parent_id`, `content`, `type`, `status`, `like_count`) 
VALUES 
(1, 4, NULL, '欢迎欢迎！期待更多精彩内容！', 0, 1, 2),
(1, 3, 1, '谢谢支持！', 0, 1, 1),
(2, 3, NULL, '确实是个好天气！', 0, 1, 1);

-- 插入测试点赞
INSERT INTO `post_like` (`post_id`, `user_id`) 
VALUES 
(1, 3),
(1, 4),
(2, 3),
(3, 3),
(3, 4);

-- 插入测试关注关系
INSERT INTO `follow_relation` (`follower_id`, `following_id`) 
VALUES 
(3, 4),
(4, 3),
(3, 2),
(4, 2);

-- =============================================
-- 查询验证
-- =============================================
-- 查看所有表
SHOW TABLES;

-- 查看用户数量
SELECT COUNT(*) AS user_count FROM user_profile;

-- 查看帖子数量
SELECT COUNT(*) AS post_count FROM post;

-- 查看评论数量
SELECT COUNT(*) AS comment_count FROM comment;

-- =============================================
-- 脚本结束
-- =============================================
