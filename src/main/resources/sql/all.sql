DROP DATABASE IF EXISTS `zcl`;
CREATE DATABASE `zcl`;

USE `zcl`;

DROP TABLE IF EXISTS `t_sys_user`;
CREATE TABLE `t_sys_user` (
  id             BIGINT AUTO_INCREMENT PRIMARY KEY,
  create_time    DATETIME,
  modify_time    DATETIME,
  status         SMALLINT COMMENT '状态  -2：激活状态异常  -1：停用  0：试用  1：未激活',
  username       VARCHAR(18) UNIQUE
  COMMENT '用户名',
  password       CHAR(60) COMMENT '密码',
  invite_code    CHAR(6) UNIQUE
  COMMENT '激活时使用的邀请码',
  invite_user_id BIGINT,
  phone          CHAR(11) COMMENT '手机号码，必须是手机',
  activate_time  DATETIME COMMENT '激活时间'
)
  ENGINE = innodb
  DEFAULT CHARSET = utf8
  COMMENT '用户表';


DROP TABLE IF EXISTS `t_biz_invite_code`;
CREATE TABLE `t_biz_invite_code` (
  id               BIGINT AUTO_INCREMENT PRIMARY KEY,
  create_time      DATETIME,
  modify_time      DATETIME,
  status           SMALLINT COMMENT ' 状态： -1： 被人工废弃     0：可用     1：已被使用     2：已过期',
  invite_code      CHAR(6) UNIQUE
  COMMENT '邀请码',
  rebate_value     DECIMAL(14, 2) COMMENT '邀请码返利额',
  generate_user_id BIGINT COMMENT '生成该码的用户',
  timeout          BIGINT COMMENT '邀请码有效时限'
)
  ENGINE = innodb
  DEFAULT CHARSET = utf8
  COMMENT '邀请码表';


DROP TABLE IF EXISTS `t_biz_point`;
CREATE TABLE `t_biz_point` (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  create_time DATETIME,
  modify_time DATETIME,
  status      SMALLINT COMMENT '状态   -1：停用  0：试用  1：未激活',
  user_id     BIGINT UNIQUE
  COMMENT '关联的用户id',
  account     DECIMAL(14, 2) COMMENT '余额',
  version     BIGINT DEFAULT 0
  COMMENT '乐观锁版本号'
)
  ENGINE = innodb
  DEFAULT CHARSET = utf8
  COMMENT '积分表，也就是用户的钱包';


DROP TABLE IF EXISTS `t_sys_config`;
CREATE TABLE `t_sys_config` (
  id           BIGINT AUTO_INCREMENT PRIMARY KEY,
  create_time  DATETIME,
  modify_time  DATETIME,
  status       SMALLINT COMMENT '该字段不使用',
  config_name  VARCHAR(50),
  config_value VARCHAR(200)
)
  ENGINE = innodb
  DEFAULT CHARSET = utf8
  COMMENT '配置表';


DROP TABLE IF EXISTS `t_biz_point_log`;
CREATE TABLE `t_biz_point_log` (
  id              BIGINT AUTO_INCREMENT PRIMARY KEY,
  create_time     DATETIME,
  modify_time     DATETIME,
  status          SMALLINT COMMENT '该字段不使用',
  user_id         BIGINT COMMENT '关联的用户id',
  bill_id         BIGINT COMMENT '账单id，如果不存在则为null',
  point_before    DECIMAL(14, 2) COMMENT '交易前积分点',
  point_after     DECIMAL(14, 2) COMMENT '交易后积分点',
  trading_volume  DECIMAL(14, 2) COMMENT '交易总额',
  trading_type_id SMALLINT COMMENT '交易类型',
  pay_type_id     SMALLINT COMMENT '支付类型',
  trading_desc    VARCHAR(20) COMMENT '交易类型描述',
  pay_desc        VARCHAR(20) COMMENT '支付类型描述',
  operator_id     BIGINT COMMENT '操作人id',
  operator_type   SMALLINT COMMENT '操作人类型  0: 用户   1: 管理员'
)
  ENGINE = innodb
  DEFAULT CHARSET = utf8
  COMMENT '用户积分变更日志表';


DROP TABLE IF EXISTS `t_biz_invite_code_price`;
CREATE TABLE `t_biz_invite_code_price` (
  id           BIGINT AUTO_INCREMENT PRIMARY KEY,
  create_time  DATETIME,
  modify_time  DATETIME,
  status       SMALLINT COMMENT '-1:禁用  0:使用中',
  quantity     INTEGER COMMENT '数量',
  total_price  DECIMAL(14, 2) COMMENT '总价',
  rebate_value DECIMAL(14, 2) COMMENT '单条返利额',
  timeout      BIGINT COMMENT '过期时限',
  timeout_desc VARCHAR(20) COMMENT '过期时限描述，如"3天"'
)
  ENGINE = innodb
  DEFAULT CHARSET = utf8
  COMMENT '邀请码价目表';


DROP TABLE IF EXISTS `t_biz_invite_code_bill`;
CREATE TABLE `t_biz_invite_code_bill` (
  id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
  create_time          DATETIME,
  modify_time          DATETIME,
  status               SMALLINT,
  invite_code_price_id BIGINT COMMENT '资费标准id',
  quantity             INT COMMENT '单次购入数量',
  total                DECIMAL(14, 2) COMMENT '总价'
)
  ENGINE = innodb
  DEFAULT CHARSET = utf8
  COMMENT '邀请码购买账单';