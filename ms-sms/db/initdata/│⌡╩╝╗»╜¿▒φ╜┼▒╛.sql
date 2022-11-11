-- 创建db_sms 的数据库
CREATE DATABASE IF NOT EXISTS `db_sms` DEFAULT CHARACTER SET utf8mb4;
USE `db_sms`;


-- 创建短信信息表
CREATE TABLE IF NOT EXISTS `tb_sms_info` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `project_id` VARCHAR(50) NULL DEFAULT NULL COMMENT '多租户id' COLLATE 'utf8mb4_general_ci',
  `business_id` VARCHAR(36) NOT NULL COMMENT '业务id',
  `from_type` tinyint(2) NOT NULL COMMENT '来源终端类型 0后台管理平台 1远控服务',
  `send_phone` VARCHAR(11) COMMENT '发送手机号',
  `send_time` datetime NOT NULL COMMENT '发送时间',
  `send_channel` tinyint(2) unsigned NOT NULL COMMENT '发送渠道 1极光 ',
  `send_status` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '发送状态，10发送中 20发送完成 30发送失败',
  `finish_time` datetime COMMENT '完成时间',
  `version` INT(10) NULL DEFAULT NULL COMMENT '版本号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime COMMENT '最后一次修改时间',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改人',
  `del_flag` tinyint(2) unsigned NOT NULL DEFAULT '1' COMMENT '删除标志位，0正常；1删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COMMENT='短信信息表';


-- 创建短信明细表
CREATE TABLE IF NOT EXISTS `tb_sms_info_detail` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `project_id` VARCHAR(50) NULL DEFAULT NULL COMMENT '多租户id' COLLATE 'utf8mb4_general_ci',
  `sms_info_id` VARCHAR(36) NOT NULL COMMENT '消息id',
  `sms_content` text NOT NULL COMMENT '内容',
  `extra_data` text COMMENT '附加内容',
  `version` INT(10) NULL DEFAULT NULL COMMENT '版本号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime COMMENT '最后一次修改时间',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改人',
  `del_flag` tinyint(2) unsigned NOT NULL DEFAULT '1' COMMENT '删除标志位，0正常；1删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COMMENT='短信明细表';



-- 创建短信记录表
CREATE TABLE IF NOT EXISTS `tb_sms_record` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`project_id` VARCHAR(50) NULL DEFAULT NULL COMMENT '多租户id' COLLATE 'utf8mb4_general_ci',
	`sms_info_id` BIGINT(20) UNSIGNED NOT NULL COMMENT '消息明细id',
	`send_channel` tinyint(2) unsigned NOT NULL COMMENT '发送渠道 1极光短信 ',
	`template_id` VARCHAR(16) NULL DEFAULT NULL COMMENT '模板id' COLLATE 'utf8mb4_general_ci',
	`callback_msg_id` VARCHAR(16) NULL DEFAULT NULL COMMENT '回调消息id' COLLATE 'utf8mb4_general_ci',
	`send_status` TINYINT(2) UNSIGNED NOT NULL DEFAULT '0' COMMENT '发送状态，1发送中 2发送完成 3发送失败',
	`send_time` DATETIME NOT NULL COMMENT '发送时间',
	`finish_time` DATETIME NULL DEFAULT NULL COMMENT '完成时间',
	`status_code` INT(6) UNSIGNED NULL DEFAULT NULL COMMENT '状态编码',
	`version` INT(10) NULL DEFAULT NULL COMMENT '版本号',
	`create_time` DATETIME NOT NULL COMMENT '创建时间',
	`update_time` DATETIME NULL DEFAULT NULL COMMENT '最后一次修改时间',
	`create_by` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人' COLLATE 'utf8mb4_general_ci',
	`update_by` VARCHAR(50) NULL DEFAULT NULL COMMENT '修改人' COLLATE 'utf8mb4_general_ci',
	`del_flag` TINYINT(2) UNSIGNED NOT NULL DEFAULT '1' COMMENT '删除标志位，0正常；1删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COMMENT='短信记录表';


-- 创建短信渠道规则表
CREATE TABLE IF NOT EXISTS `tb_channel_rule` (
   `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`project_id` VARCHAR(50) NULL DEFAULT NULL COMMENT '多租户id' COLLATE 'utf8mb4_general_ci',
	`sms_channel` tinyint(2) UNSIGNED NOT NULL COMMENT '渠道 1极光短信 ',
	`send_rate` SMALLINT(4) UNSIGNED COMMENT '发送频率 条数',
	`rate_unit` TINYINT(2) UNSIGNED COMMENT '频率单位 10:毫秒ms 15:秒s 20:分min 25:小时hour 30:天',
	`send_times` SMALLINT(4) UNSIGNED COMMENT '发送次数',
	`times_unit` TINYINT(2) UNSIGNED COMMENT '次数单位 10:毫秒ms 15:秒s 20:分min 25:小时hour 30:天day 35:月month',
	`intercept_type` TINYINT(2) UNSIGNED NOT NULL DEFAULT '1' COMMENT '拦截类型 1:默认放行全部 2：默认拦截全部',
	`enable_flag` TINYINT(2) UNSIGNED NOT NULL DEFAULT '0' COMMENT '启用状态，0未启用 1启用',
	`version` INT(10) NULL DEFAULT NULL COMMENT '版本号',
	`create_time` DATETIME NOT NULL COMMENT '创建时间',
	`update_time` DATETIME NULL DEFAULT NULL COMMENT '最后一次修改时间',
	`create_by` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人' COLLATE 'utf8mb4_general_ci',
	`update_by` VARCHAR(50) NULL DEFAULT NULL COMMENT '修改人' COLLATE 'utf8mb4_general_ci',
	`del_flag` TINYINT(2) UNSIGNED NOT NULL DEFAULT '1' COMMENT '删除标志位，0正常；1删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COMMENT='短信渠道规则表';



-- 创建短信渠道发送名单设置表
CREATE TABLE IF NOT EXISTS `tb_channel_roster` (
   `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`project_id` VARCHAR(50) NULL DEFAULT NULL COMMENT '多租户id' COLLATE 'utf8mb4_general_ci',
	`channel_rule_id` BIGINT(20) UNSIGNED NOT NULL COMMENT '渠道设置表id',
	`phone` VARCHAR(11) NOT NULL COMMENT '手机号',
	`roster_type` TINYINT(2) UNSIGNED NOT NULL COMMENT '类型：1白名单 2黑名单',
	`enable_flag` TINYINT(2) UNSIGNED NOT NULL DEFAULT '0' COMMENT '启用状态，0未启用 1启用',
	`version` INT(10) NULL DEFAULT NULL COMMENT '版本号',
	`create_time` DATETIME NOT NULL COMMENT '创建时间',
	`update_time` DATETIME NULL DEFAULT NULL COMMENT '最后一次修改时间',
	`create_by` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人' COLLATE 'utf8mb4_general_ci',
	`update_by` VARCHAR(50) NULL DEFAULT NULL COMMENT '修改人' COLLATE 'utf8mb4_general_ci',
	`del_flag` TINYINT(2) UNSIGNED NOT NULL DEFAULT '1' COMMENT '删除标志位，0正常；1删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COMMENT='短信渠道发送名单设置表';


