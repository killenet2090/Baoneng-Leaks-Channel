/*
SQLyog Ultimate v12.2.6 (64 bit)
MySQL - 5.7.30-log : Database - bnmotor_icv_vehicle_fota
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`bnmotor_icv_vehicle_fota` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `bnmotor_icv_vehicle_fota`;

/*Table structure for table `tb_device_tree_node` */

DROP TABLE IF EXISTS `tb_device_tree_node`;

CREATE TABLE `tb_device_tree_node` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `project_id` varchar(50) NOT NULL DEFAULT 'bngrp' COMMENT '项目ID，用于定于设备树的归属',
  `node_name` varchar(100) NOT NULL DEFAULT '' COMMENT '业务代码名称',
  `node_code` varchar(128) NOT NULL DEFAULT '' COMMENT '节点对应的业务对象编码:如果为ECU设备零件，可以认为零件ID',
  `parent_id` bigint(16) DEFAULT NULL COMMENT '父节点ID， 如果为空，表示为根节点',
  `node_id_path` varchar(768) NOT NULL DEFAULT '' COMMENT '以节点ID作为路径元素， 以节点/开始到本节点的上一节点止的路径（不含/结尾），即当前节点所在的路径对于根节点其path 为/提供此属性的目的是便于树的查询当对某个节点进行移动时，此节点以下的所有节点的路径都需要修改',
  `node_name_path` varchar(768) NOT NULL DEFAULT '' COMMENT '以节点NAME作为路径元素，以节点/开始到本节点的上一节点止的路径（不含/结尾），即当前节点所在的路径对于根节点其path 为/提供此属性的目的是便于树的查询当对某个节点进行移动时，此节点以下的所有节点的路径都需要修改',
  `node_code_path` varchar(768) NOT NULL DEFAULT '' COMMENT '以节点CODE作为路径元素，以节点/开始到本节点的上一节点止的路径（不含/结尾），即当前节点所在的路径对于根节点其path 为/提供此属性的目的是便于树的查询当对某个节点进行移动时，此节点以下的所有节点的路径都需要修改',
  `tree_level` smallint(6) DEFAULT NULL COMMENT '根节点的层次为0,根节点的直接子节点层次为1,以此类推',
  `root_node_id` bigint(16) DEFAULT NULL COMMENT '每一颗树的根节点的id,如果自身是根节点,则为空',
  `status` smallint(6) NOT NULL DEFAULT '0' COMMENT '0-正常 ,1-停用',
  `order_num` int(11) DEFAULT '0' COMMENT '树节点同一层级排序号',
  `version` int(11) DEFAULT '0' COMMENT '数据版本，用于控制并发操作',
  `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志: 0-正常,1-删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `ik_project_parent` (`project_id`,`parent_id`) USING BTREE COMMENT '定义查询索引',
  KEY `uk_node_code_path` (`node_code_path`) USING BTREE COMMENT '定义业务查询索引'
) ENGINE=InnoDB AUTO_INCREMENT=1308580274329718786 DEFAULT CHARSET=utf8mb4 COMMENT='设备分类树\r\n该信息从车辆数据库中同步过来';


/*Table structure for table `tb_dictionary` */

DROP TABLE IF EXISTS `tb_dictionary`;

CREATE TABLE `tb_dictionary` (
  `id` bigint(255) unsigned NOT NULL AUTO_INCREMENT COMMENT '字典表id',
  `dic_key` varchar(50) NOT NULL COMMENT '字典键',
  `dic_value` varchar(50) NOT NULL COMMENT '字典值',
  `dic_type` varchar(50) NOT NULL COMMENT '字典类型',
  `is_active` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '是否有效，1有效；0无效',
  `version` int(11) DEFAULT NULL COMMENT '数据记录版本，用于控制并发',
  `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志:0=正常,1=删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COMMENT='字典表';

/*Data for the table `tb_dictionary` */

insert  into `tb_dictionary`(`id`,`dic_key`,`dic_value`,`dic_type`,`is_active`,`version`,`del_flag`,`create_by`,`create_time`,`update_by`,`update_time`) values 

(1,'firmware_url','http://10.210.100.17:9000/ota/f94f4a11079482bbaed3a71d32f72dc8','FLASH_SCRIPT',1,NULL,0,NULL,'2020-06-18 20:40:11',NULL,'2020-06-18 20:40:14'),

(2,'firmware_dif_url','http://10.210.100.17:9000/ota/f94f4a11079482bbaed3a71d32f72dc8','FLASH_SCRIPT',1,NULL,0,NULL,'2020-06-18 20:41:17',NULL,'2020-06-18 20:41:19'),

(3,'condition_type_speed','1','CONDITION_TYPE',1,NULL,0,NULL,'2020-06-18 20:40:11',NULL,'2020-06-18 20:40:14'),

(4,'condition_type_voltage','2','CONDITION_TYPE',1,NULL,0,NULL,'2020-06-18 20:40:11',NULL,'2020-06-18 20:40:14'),

(5,'condition_type_=','1','OPERATOR_ARITH',1,NULL,0,NULL,'2020-06-18 20:40:11',NULL,'2020-06-18 20:40:14'),

(6,'condition_type_!=','2','OPERATOR_ARITH',1,NULL,0,NULL,'2020-06-18 20:40:11',NULL,'2020-06-18 20:40:14'),

(7,'condition_type_>','3','OPERATOR_ARITH',1,NULL,0,NULL,'2020-06-18 20:40:11',NULL,'2020-06-18 20:40:14'),

(8,'condition_type_<','4','OPERATOR_ARITH',1,NULL,0,NULL,'2020-06-18 20:40:11',NULL,'2020-06-18 20:40:14'),

(9,'condition_type_>=','5','OPERATOR_ARITH',1,NULL,0,NULL,'2020-06-18 20:40:11',NULL,'2020-06-18 20:40:14'),

(10,'condition_type_<=','6','OPERATOR_ARITH',1,NULL,0,NULL,'2020-06-18 20:40:11',NULL,'2020-06-18 20:40:14');

/*Table structure for table `tb_fota_download_process` */

DROP TABLE IF EXISTS `tb_fota_download_process`;

CREATE TABLE `tb_fota_download_process` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `upgrade_record_id` bigint(16) NOT NULL COMMENT '升级记录ID',
  `downloaded_size` bigint(16) NOT NULL COMMENT '已下载字节数, byte',
  `total_size` bigint(16) NOT NULL COMMENT '总字节数',
  `upto_time` datetime NOT NULL COMMENT '截至时间',
  `version` int(11) DEFAULT NULL COMMENT '数据记录版本，用于控制并发',
  `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志:0-正常,1-删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `FK_Reference_16` (`upgrade_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OTA升级下载进度\r\n记录下载的当前进度信息。一个升级对象的一个软件的升级记录对应一条下载进度信息\r\n                                             -&#&';

/*Data for the table `tb_fota_download_process` */

/*Table structure for table `tb_fota_file_upload_record` */

DROP TABLE IF EXISTS `tb_fota_file_upload_record`;

CREATE TABLE `tb_fota_file_upload_record` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `file_name` varchar(50) DEFAULT NULL COMMENT '文件原名称',
  `file_size` bigint(16) DEFAULT NULL COMMENT '文件大小，单位：字节',
  `file_md5` varchar(50) DEFAULT NULL COMMENT '文件md5值',
  `file_sha` varchar(128) DEFAULT NULL COMMENT '文件sha码',
  `chunk_sum` smallint(6) DEFAULT NULL COMMENT '总分片数',
  `upload_begin_dt` datetime DEFAULT NULL COMMENT '上传开始时间',
  `upload_end_dt` datetime DEFAULT NULL COMMENT '上传结束时间',
  `file_path` varchar(256) DEFAULT NULL COMMENT '文件上传路径',
  `file_address` varchar(256) DEFAULT NULL COMMENT '服务器磁盘位置',
  `file_key` varchar(64) DEFAULT NULL COMMENT 'OSS存储中的key值',
  `file_type` smallint(2) DEFAULT NULL COMMENT '文件用途类型：0=安装包文件，1=测试报告文件，2=脚本类型',
  `fast` tinyint(1) DEFAULT NULL COMMENT '是否秒传',
  `status` varchar(10) DEFAULT NULL COMMENT '状态',
  `version` int(11) DEFAULT NULL COMMENT '数据版本d，用于更新并发控制使用',
  `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志:0=正常,1=删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=479 DEFAULT CHARSET=utf8mb4 COMMENT='文件上传记录表';


/*Table structure for table `tb_fota_firmware` */

DROP TABLE IF EXISTS `tb_fota_firmware`;

CREATE TABLE `tb_fota_firmware` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` varchar(50) DEFAULT NULL COMMENT '项目ID多租户，不同的租户具有不同的项目ID',
  `parent_tree_node_id` bigint(16) DEFAULT NULL COMMENT '设备树父节点Id(配置层级的节点Id)',
  `tree_node_id` bigint(16) DEFAULT NULL COMMENT '设备树节点Id',
  `tree_node_code_path` varchar(768) DEFAULT NULL COMMENT '挂载的设备树零件节点业务路径',
  `component_code` varchar(50) NOT NULL COMMENT '零件代码区分不同零件的代号',
  `component_model` varchar(50) DEFAULT NULL COMMENT '零件型号',
  `component_name` varchar(50) DEFAULT NULL COMMENT '零件名称',
  `firmware_code` varchar(50) NOT NULL COMMENT '固件代码(固件代码由OTA平台同终端提前约定)',
  `firmware_name` varchar(50) NOT NULL COMMENT '固件名称',
  `diagnose` varchar(50) DEFAULT NULL COMMENT '诊断ID代表一个ECU刷新通讯时用到的ID',
  `ota_channel` varchar(20) DEFAULT NULL COMMENT '升级渠道定义当前软解借助什么渠道升级:０-tbox，1-车机......',
  `description` varchar(500) DEFAULT NULL COMMENT '描述软件的作用，目的等介绍说明',
  `update_model` smallint(1) DEFAULT NULL COMMENT '升级模式(区分于软件中的升级模式)：0-强制静默升级，1-非静默升级',
  `notified_to_mobile` smallint(1) DEFAULT NULL COMMENT '是否通知到手机：0-否，1-是',
  `package_model` smallint(2) unsigned zerofill DEFAULT NULL COMMENT '升级模式(按升级包的形式来看)软件的性质决定了该软件适合采用怎么样的升级模式0 - 全量升级，该模式下，只能通过下载全量包升级1－补丁升级，该模式下，可以通过全量升级和补丁升级2－差分升级，该模式下，可以通过全量升级和差分包升级',
  `encrypt_alg` varchar(20) DEFAULT NULL COMMENT '升级包加密算法采用对称加密，对全量包，补丁包或差分包进行加密的算法空代表无需加密，其他为具体加密方法目前支撑：AES-CBC-128',
  `package_alg` varchar(20) DEFAULT NULL COMMENT '打包算法指软件在发布为软件包时采用的打包算法后台再需要生成差分前，按此算法解包后，再生产差分空代表无需解包targzip',
  `apn_channel` smallint(1) DEFAULT NULL COMMENT 'APN通道期望该版本通过该通道升级：1-apn1，2-apn2',
  `ota_cond_express` varchar(256) DEFAULT NULL COMMENT '升级条件表达式采用ognl表达式表达，终端需要将变量替换为值传入表达式中计算ognl表达式值：true/false, 以判断终端是否满足条件',
  `flash_script_url` varchar(256) DEFAULT NULL COMMENT '刷写脚本URL',
  `status` smallint(2) DEFAULT NULL COMMENT '状态：0-新建，1-待审核，2-审核通过，3-审核不通过，4-已发布。新建状态允许物理删除',
  `version` int(11) DEFAULT '0' COMMENT '数据版本d，用于更新并发控制使用',
  `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志:0-正常1-删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `uk_tree_node_code_path` (`tree_node_code_path`) USING BTREE COMMENT '设备树节点Code路径，以该字段做业务关联',
  KEY `uk_project_firmwarecode` (`project_id`,`firmware_code`,`del_flag`) USING BTREE COMMENT '固件代码查询',
  KEY `uk_project_tree` (`project_id`,`tree_node_id`,`del_flag`) USING BTREE COMMENT '节点树查询索引'
) ENGINE=InnoDB AUTO_INCREMENT=1308580274354884610 DEFAULT CHARSET=utf8mb4 COMMENT='OTA固件信息\r\n定义各个零部件需要支持的OTA升级软件\r\n\r\nOTA软升级需要用户的下载';


/*Table structure for table `tb_fota_firmware_list` */

DROP TABLE IF EXISTS `tb_fota_firmware_list`;

CREATE TABLE `tb_fota_firmware_list` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` varchar(50) DEFAULT NULL COMMENT '项目Id',
  `ota_object_id` bigint(16) DEFAULT NULL COMMENT '所属升级对象ID',
  `component_id` varchar(50) DEFAULT NULL COMMENT '零件号：对应设备的唯一识别号，比如pdsn，tbox id,iccid等（废弃，由设备树去维护零件信息）',
  `firmware_id` bigint(16) DEFAULT NULL COMMENT '软件ID\r\n            ',
  `batch_no` varchar(50) DEFAULT NULL COMMENT '零件批次号',
  `serial_no` varchar(50) DEFAULT NULL COMMENT '零件序列号',
  `hardware_version` varchar(50) DEFAULT NULL COMMENT '零件硬件版本',
  `made_date` date DEFAULT NULL COMMENT '生产日期',
  `init_firmware_version` varchar(256) DEFAULT NULL COMMENT '初始化固件版本号',
  `running_firmware_version` varchar(256) DEFAULT NULL COMMENT '当前运行软件版本',
  `need_upgrade_firmware_version` varchar(256) DEFAULT NULL COMMENT '需要升级的软件版本号： 双系统升级时，需要在此版本号基础上升级。单升级时，该字段即为需要升级的软件版本号',
  `upgrade_time` datetime DEFAULT NULL COMMENT '最后升级时间',
  `version` int(11) DEFAULT '0' COMMENT '数据并发控制字段',
  `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志\r\n            0 - 正常\r\n            1 - 删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `FK_Reference_23` (`firmware_id`),
  KEY `FK_Reference_27` (`ota_object_id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COMMENT='OTA升级固件清单\r\n';


/*Table structure for table `tb_fota_firmware_pkg` */

DROP TABLE IF EXISTS `tb_fota_firmware_pkg`;

CREATE TABLE `tb_fota_firmware_pkg` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pkg_type` smallint(1) DEFAULT NULL COMMENT '升级包类型:0-全量包,1-补丁包,2-差分包',
  `upload_file_id` bigint(100) DEFAULT NULL COMMENT '原始上传包的文件Id',
  `pkg_file_name` varchar(50) DEFAULT NULL COMMENT '升级包文件名,即升级包上传前自身的文件名，用于显示',
  `original_pkg_file_path` varchar(100) DEFAULT NULL COMMENT '原始包存储完整路径，包含文件名绝对路径原始包需要存储在分布式存储设备上',
  `original_package_alg` varchar(50) DEFAULT NULL COMMENT '原始包打包算法来源于软件表中的打包算法',
  `original_pkg_sha_code` varchar(255) DEFAULT NULL COMMENT '原始包SHA码',
  `original_pkg_size` bigint(16) DEFAULT NULL COMMENT '原始包大小单位字节',
  `original_report_file_path` varchar(100) DEFAULT NULL COMMENT '测试报告存储路径',
  `original_dif_script_url` varchar(100) DEFAULT NULL COMMENT '差分刷写脚本url',
  `build_pkg_status` smallint(2) DEFAULT '0' COMMENT '包制作状态：0=未开始，1=制作中，2=制作成功，3=制作失败',
  `build_pkg_time` datetime DEFAULT NULL COMMENT '包制作更新时间',
  `release_pkg_file_path` varchar(100) DEFAULT NULL COMMENT '发布包存储完整路径，包含文件名绝对路径原始包需要存储在分布式存储设备上',
  `release_pkg_file_download_url` varchar(200) DEFAULT NULL COMMENT '发布包下载URL',
  `release_pkg_sha_code` varchar(256) DEFAULT NULL COMMENT '发布包SHA码',
  `release_pkg_encrypt_alg` varchar(50) DEFAULT NULL COMMENT '发布包加密算法来源于软件表中的加密算法',
  `release_pkg_encrypt_secret` varchar(50) DEFAULT NULL COMMENT '发布包加密密钥升级包加密密钥，由系统随机生成，一个包一个密钥',
  `release_pkg_sign_alg` varchar(50) DEFAULT NULL COMMENT '发布包签名算法来源于软件表中的签名算法',
  `release_pkg_sign` varchar(500) DEFAULT NULL COMMENT '升级包签名全量升级包的签名信息，采用服务端私钥签名而成',
  `release_pkg_file_size` bigint(16) DEFAULT NULL COMMENT '发布包大小单位字节',
  `release_pkg_chip_info` varchar(200) DEFAULT NULL COMMENT '升级包碎片数据结构：[N][Index][L][data][L][data][L][data]…… N: 一个字节，代表有多少个碎片	Index: 表示数据在包中的开始位置	L:表示该数据的长度	Data:为具体数据，长度为L上述数据需要经过Base64编码转换为字符格式。',
  `release_pkg_status` varchar(1) DEFAULT NULL COMMENT '发布包状态:0表示无效1表示有效',
  `release_pkg_status_msg` varchar(500) DEFAULT NULL COMMENT '发布包状态描述',
  `release_pkg_cdn_download_url` varchar(500) DEFAULT NULL COMMENT '发布包CDN下载地址',
  `release_pkg_cdn_obj_id` varchar(200) DEFAULT NULL COMMENT '发布包CDN对象ID',
  `release_pkg_cdn_time` datetime DEFAULT NULL COMMENT '发布包CDN发布时间',
  `estimate_flash_time` bigint(16) DEFAULT NULL COMMENT '预估刷写时间',
  `version` int(11) DEFAULT '0' COMMENT '数据记录版本，用于控制并发',
  `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志:0-正常,1-删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=227 DEFAULT CHARSET=utf8mb4 COMMENT='升级包信息\r\n包括升级包原始信息以及升级包发布信息';


/*Table structure for table `tb_fota_firmware_version` */

DROP TABLE IF EXISTS `tb_fota_firmware_version`;

CREATE TABLE `tb_fota_firmware_version` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` varchar(50) NOT NULL COMMENT '项目ID',
  `firmware_id` bigint(16) DEFAULT NULL COMMENT '固件ID该版本对应的软件',
  `firmware_version_name` varchar(50) DEFAULT NULL COMMENT '固件版本名称',
  `firmware_version_no` varchar(128) DEFAULT NULL COMMENT '固件版本号',
  `version_digital_no` bigint(16) DEFAULT NULL COMMENT '版本号数字码用于版本大小比较暂时由页面输入',
  `parent_version_id` bigint(16) DEFAULT NULL COMMENT '父版本ID',
  `applied_firmware_version` varchar(255) DEFAULT NULL COMMENT '适用的固件版本集合(分隔符是分号)固件版本号;固件版本号',
  `applied_hardware_version` varchar(200) DEFAULT NULL COMMENT '适用的硬件版本集合(分隔符是分号)硬件版本号;硬件版本号',
  `release_notes` varchar(500) DEFAULT NULL COMMENT '发布说明',
  `full_pkg_id` bigint(16) DEFAULT NULL COMMENT '全量包ID',
  `status` smallint(2) DEFAULT NULL COMMENT '状态：0-新建，1-待审核，2-审核通过，3-审核不通过，4-已发布',
  `is_force_full_update` smallint(2) DEFAULT NULL COMMENT '是否强制全量升级：0-否，1-是。如果是的话，版本检查总是返回全量包, 必须采用全量升级',
  `release_user` varchar(50) DEFAULT NULL COMMENT '发布用户Id',
  `release_dt` datetime DEFAULT NULL COMMENT '发布日期',
  `approval_user` varchar(50) DEFAULT NULL COMMENT '审核用户Id',
  `approval_dt` datetime DEFAULT NULL COMMENT '审核日期',
  `approval_comment` varchar(256) DEFAULT NULL COMMENT '审核意见',
  `version` int(11) DEFAULT '0' COMMENT '数据记录版本，用于控制并发',
  `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志:0-正常,1-删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `package_model` smallint(5) DEFAULT NULL COMMENT '固件的升级模式：0=全量,1=补丁,2=差分',
  PRIMARY KEY (`id`),
  KEY `index_project_firmware_id` (`project_id`,`firmware_id`,`del_flag`) USING BTREE COMMENT '以固件ID为索引,查询该固件的所有版本'
) ENGINE=InnoDB AUTO_INCREMENT=1310172019820081154 DEFAULT CHARSET=utf8mb4 COMMENT='软件版本,即软件坂本树上的一个节点\r\n定义软件所生成的各个不同的版本';


/*Table structure for table `tb_fota_firmware_version_dependence` */

DROP TABLE IF EXISTS `tb_fota_firmware_version_dependence`;

CREATE TABLE `tb_fota_firmware_version_dependence` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` varchar(32) DEFAULT NULL COMMENT '项目ID',
  `firmware_id` bigint(16) DEFAULT NULL COMMENT '固件ID',
  `firmware_version_id` bigint(16) DEFAULT NULL COMMENT '固件版本ID',
  `depend_firmware_id` bigint(16) DEFAULT NULL COMMENT '依赖固件ID',
  `depend_firmware_version_id` bigint(16) DEFAULT NULL COMMENT '依赖固件版本ID',
  `upgrade_sequence` smallint(6) DEFAULT NULL COMMENT '升级顺序1,2,...从小到大依次升级',
  `depend_error_hand_mechanism` varchar(50) DEFAULT NULL COMMENT '依赖错误处理机制当该依赖升级出错后该怎么处理:0-停止后续升级，已升级的保持;1-忽略当前依赖错误，继续下一个依赖，但是终止当前升级,2-终止后续升级，并回滚所有已发送的升级',
  `version` int(11) DEFAULT '0' COMMENT '数据处理版本，用于防止并发',
  `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志:0-正常,1-删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `index_project_firmware_version_id` (`project_id`,`firmware_id`,`firmware_version_id`,`del_flag`) USING BTREE COMMENT '查询某一版本的所有依赖版本列表',
  KEY `uk_version_target` (`project_id`,`firmware_version_id`,`depend_firmware_version_id`,`del_flag`) USING BTREE COMMENT '唯一键（）'
) ENGINE=InnoDB AUTO_INCREMENT=1278950933756153858 DEFAULT CHARSET=utf8mb4 COMMENT='软件版本依赖';


/*Table structure for table `tb_fota_firmware_version_path` */

DROP TABLE IF EXISTS `tb_fota_firmware_version_path`;

CREATE TABLE `tb_fota_firmware_version_path` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` varchar(32) DEFAULT NULL COMMENT '项目ID',
  `start_firmware_ver_id` bigint(16) DEFAULT NULL COMMENT '起始固件版本ID',
  `target_firmware_ver_id` bigint(16) DEFAULT NULL COMMENT '目标固件版本ID',
  `upgrade_path_type` smallint(1) DEFAULT NULL COMMENT '升级路径类型:0-全量升级路径，1-补丁升级路径，2-差分升级路径',
  `firmware_pkg_id` bigint(16) DEFAULT NULL COMMENT '固件包ID:如果是差分升级路径，则存放差分包id如果是补丁升级路径，则存放补丁包id如果是全量包升级，则该字段为空',
  `firmware_version_id_path` varchar(1024) DEFAULT NULL COMMENT '以版本的ID组成的路径,便于后续的路径检索以/分格的完整的id路径, 到本路径指向的版本id为止对于根节点，path为/。比如当前路径指向的版本为10004,其id path如下：/100001/100002/10003/10004',
  `pkg_upload` smallint(2) DEFAULT NULL COMMENT '是否已经上传了升级包文件：0=未上传，1=已上传',
  `version` int(11) DEFAULT '0' COMMENT '数据记录版本，用于控制并发',
  `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志：0-正常，1-删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `index_project_target_version` (`project_id`,`target_firmware_ver_id`,`del_flag`) USING BTREE COMMENT '用于查询某一版本所有升级路径（需要控制查询顺序）',
  KEY `index_start_target_pathtype` (`start_firmware_ver_id`,`target_firmware_ver_id`,`upgrade_path_type`,`del_flag`) USING BTREE COMMENT '路径起始唯一确定'
) ENGINE=InnoDB AUTO_INCREMENT=1310172270345859074 DEFAULT CHARSET=utf8mb4 COMMENT='软件版本升级路径记录全量包、补丁包和差分包的升级条件信息记录从适应的版本到当前版本的升级路径';



/*Table structure for table `tb_fota_object` */

DROP TABLE IF EXISTS `tb_fota_object`;

CREATE TABLE `tb_fota_object` (
  `id` bigint(16) NOT NULL COMMENT '自增序列  即ID',
  `project_id` varchar(50) DEFAULT NULL COMMENT '项目ID',
  `tree_node_id` bigint(16) DEFAULT NULL COMMENT '设备树节点ID',
  `tree_node_code_path` varchar(768) DEFAULT NULL COMMENT '设备树节点业务Code路径',
  `object_id` varchar(50) DEFAULT NULL COMMENT '对象ID: 升级对象的唯一识别ID',
  `current_area` varchar(255) DEFAULT NULL COMMENT '当前区域',
  `sale_area` varchar(255) DEFAULT NULL COMMENT '销售区域',
  `licence_no` varchar(50) DEFAULT NULL COMMENT '车牌号',
  `production_date` date DEFAULT NULL COMMENT '生产日期',
  `prod_batch_no` varchar(50) DEFAULT NULL COMMENT '生产批次',
  `init_version` varchar(128) DEFAULT NULL COMMENT '原始出厂大版本号',
  `last_version` varchar(128) DEFAULT NULL COMMENT '上次运行大版本',
  `current_version` varchar(128) DEFAULT NULL COMMENT '当前运行大版本号',
  `conf_version` varchar(128) DEFAULT NULL COMMENT '固件清单配置版本信息，用于与TBOX固件列表同步；车辆固件清单有更新，则需要更新该字段',
  `version` int(11) DEFAULT '0' COMMENT '数据记录版本，用于控制并发',
  `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志\r\n            0 - 正常\r\n            1 - 删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_object_id` (`object_id`) USING BTREE COMMENT '升级对象唯一索引，一般为车Vin码',
  KEY `index_treenode` (`tree_node_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OTA升级对象指需要升级的一个完整对象，\r\n在车联网中指一辆车\r\n通常拿车的vin作为升级的ID\r\n                                   ';


/*Table structure for table `tb_fota_object_label` */

DROP TABLE IF EXISTS `tb_fota_object_label`;

CREATE TABLE `tb_fota_object_label` (
  `id` bigint(16) NOT NULL COMMENT '自增序列  即ID',
  `project_id` varchar(50) DEFAULT NULL COMMENT '项目ID',
  `object_id` bigint(16) DEFAULT NULL COMMENT '对象ID\r\n            升级对象的唯一识别ID',
  `label_key` varchar(50) DEFAULT NULL COMMENT '生产日期',
  `label_value` varchar(50) DEFAULT NULL COMMENT '标签值',
  `version` int(11) DEFAULT '0' COMMENT '数据记录版本，用于控制并发',
  `del_flag` smallint(2) DEFAULT '0' COMMENT '删除标志\r\n            0 - 正常\r\n            1 - 删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `FK_Reference_29` (`object_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OTA升级对象指需要升级的一个完整对象，\r\n在车联网中指一辆车\r\n通常拿车的vin作为升级的ID\r\n                                         ';

/*Data for the table `tb_fota_object_label` */

/*Table structure for table `tb_fota_plan` */

DROP TABLE IF EXISTS `tb_fota_plan`;

CREATE TABLE `tb_fota_plan` (
  `id` bigint(16) NOT NULL COMMENT '主键',
  `project_id` varchar(50) DEFAULT 'bngrp' COMMENT '项目Id',
  `object_parent_id` bigint(16) DEFAULT NULL COMMENT '该任务选择的设备树节点字段',
  `plan_name` varchar(200) NOT NULL COMMENT '计划名称',
  `plan_start_time` datetime DEFAULT NULL COMMENT '计划开始时间',
  `plan_end_time` datetime DEFAULT NULL COMMENT '计划结束时间',
  `plan_status` smallint(11) DEFAULT NULL COMMENT '计划状态:-1=草稿状态、0=创建完成待审核、1=审核中（保留状态），2=审核通过、3=已发布（保留）、4=已失效',
  `batch_size` int(11) DEFAULT NULL COMMENT '计划分批的批次大小',
  `plan_desc` varchar(200) DEFAULT NULL COMMENT '计划描述',
  `publish_by` varchar(50) DEFAULT NULL COMMENT '发布者',
  `publish_date` date DEFAULT NULL COMMENT '发布日期',
  `plan_type` smallint(1) DEFAULT NULL COMMENT '计划类型：0 - 软件,1 - 整车',
  `target_version` varchar(128) DEFAULT NULL COMMENT '目标版本ID：整车的目标版本',
  `plan_stop_rate` decimal(10,2) DEFAULT NULL COMMENT '计划终止的失败率',
  `retry_count` smallint(6) DEFAULT NULL COMMENT '重试次数',
  `remarks` varchar(50) DEFAULT NULL COMMENT '备注',
  `new_version_tips` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT '新版本提示语',
  `download_tips` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT '下载提示语',
  `disclaimer` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT '免责声明',
  `task_tips` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT '任务提示',
  `upgrade_mode` int(10) DEFAULT NULL COMMENT '升级模式； 1 - 静默升级， 2 - 提示用户',
  `is_enable` tinyint(2) DEFAULT '0' COMMENT '数据状态：0 - 禁用，1 - 启用.',
  `version` int(11) DEFAULT '0' COMMENT '数据并发版本控制',
  `del_flag` smallint(2) DEFAULT '0' COMMENT '删除标志：0 - 正常，1 - 删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OTA升级计划表';

/*Table structure for table `tb_fota_plan_firmware_list` */

DROP TABLE IF EXISTS `tb_fota_plan_firmware_list`;

CREATE TABLE `tb_fota_plan_firmware_list` (
  `id` bigint(16) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` varchar(50) DEFAULT NULL COMMENT '项目ID',
  `task_id` bigint(20) DEFAULT NULL COMMENT '关联任务ID',
  `plan_id` bigint(16) DEFAULT NULL COMMENT '升级计划ID',
  `firmware_id` bigint(16) DEFAULT NULL,
  `firmware_version_id` bigint(16) DEFAULT NULL COMMENT '目标版本ID\r\n            即期望升级到的目标版本，\r\n            如果不指定，则要求升级到最新版本',
  `upgrade_seq` smallint(6) DEFAULT NULL COMMENT '升级顺序\r\n            1,2,...\r\n            从小到大依次升级',
  `group` int(255) DEFAULT NULL COMMENT '升级组',
  `rollback_mode` int(11) DEFAULT NULL COMMENT '回滚标志 0=不回滚 1=回滚',
  `version` int(11) DEFAULT '0' COMMENT '数据记录版本，用于控制并发',
  `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志\r\n            0 - 正常\r\n            1 - 删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `FK_Reference_22` (`plan_id`),
  KEY `FK_Reference_25` (`firmware_version_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1295199417450434668 DEFAULT CHARSET=utf8mb4 COMMENT='该表用于定义一个OTA升级计划中需要升级哪几个软件\r\n包含：\r\n     1. 依赖的软件清单\r\n                                               -';

/*Table structure for table `tb_fota_plan_obj_list` */

DROP TABLE IF EXISTS `tb_fota_plan_obj_list`;

CREATE TABLE `tb_fota_plan_obj_list` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `ota_plan_id` bigint(16) DEFAULT NULL COMMENT '升级计划id',
  `ota_object_id` bigint(16) DEFAULT NULL COMMENT '升级对象Id',
  `object_id` bigint(16) DEFAULT NULL COMMENT '升级对象',
  `status` tinyint(2) DEFAULT '0' COMMENT '升级状态:0=新创建，1=TBOX检查到新版本，2=升级下载待确认（TBOX已经收到新版本下发），3=升级下载中，4=升级安装待确认（下载完成），5=升级安装中，6=升级成功，7=升级失败\r\n51=TBOX已收到下载指令（并同步到了OTA云端），待下载，52=TBOX执行下载结束（成功），53=TBOX执行下载结束（失败），54=TBOX已收到升级安装指令（并同步到了OTA云端），55=前置条件检查失败',
  `task_id` bigint(16) DEFAULT NULL COMMENT '关联任务ID',
  `vin` varchar(255) DEFAULT NULL COMMENT '关联车辆VIN',
  `current_area` varchar(255) DEFAULT NULL COMMENT '当前区域',
  `sale_area` varchar(255) DEFAULT NULL COMMENT '销售区域',
  `source_version` varchar(128) DEFAULT NULL COMMENT '升级前原版本号',
  `target_version` varchar(128) DEFAULT NULL COMMENT '升级目标版本号',
  `current_version` varchar(128) DEFAULT NULL COMMENT '当前运行版本号',
  `result` int(11) DEFAULT NULL COMMENT '升级结果 1=升级完成,2=升级未完成,3=升级失败',
  `log_file_id` bigint(16) DEFAULT NULL COMMENT '升级日志文件上传文件Id',
  `log_file_url` varchar(255) DEFAULT NULL COMMENT '升级日志文件下载地址url',
  `version` int(112) DEFAULT '0' COMMENT '数据版本字段',
  `del_flag` tinyint(23) DEFAULT '0' COMMENT '删除标志\r\n            0 - 正常\r\n            1 - 删除',
  `create_by` varchar(503) DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(503) DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime(3) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1309767447898787842 DEFAULT CHARSET=utf8mb4 COMMENT='OTA升级计划对象清单\r\n定义一次升级中包含哪些需要升级的车辆';


/*Table structure for table `tb_fota_plan_task_detail` */

DROP TABLE IF EXISTS `tb_fota_plan_task_detail`;

CREATE TABLE `tb_fota_plan_task_detail` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `project_id` varchar(50) DEFAULT NULL COMMENT '项目Id',
  `ota_plan_id` bigint(16) DEFAULT NULL COMMENT 'ota任务Id',
  `ota_plan_obj_id` bigint(16) DEFAULT NULL COMMENT '升级计划对象列表中某一个升级对象记录主键Id（tb_fota_plan_obj_list表数据主键Id）\r\n',
  `ota_plan_firmware_id` bigint(16) DEFAULT NULL COMMENT '升级计划清单列表中某一个升级固件记录主键Id（tb_fota_plan_firmware_list表数据主键Id）',
  `start_time` datetime DEFAULT NULL COMMENT '整个OTA开始时间\r\n            从版本检查更新开始',
  `finish_time` datetime DEFAULT NULL COMMENT '结束时间',
  `failed_time` datetime DEFAULT NULL COMMENT '失败时间',
  `failed_reason` varchar(256) DEFAULT NULL COMMENT '升级失败原因',
  `retry_count` smallint(6) DEFAULT NULL COMMENT '重试次数',
  `retry_time` datetime DEFAULT NULL COMMENT '重试时间',
  `task_status` smallint(2) DEFAULT '-1' COMMENT '任务状态：0=升级未开始，1=成功，2=失败',
  `remarks` varchar(50) DEFAULT NULL COMMENT '备注',
  `source_version` varchar(128) DEFAULT NULL COMMENT '原版本号：来自于升级对象固件清单列表\r\n',
  `current_version` varchar(128) DEFAULT NULL COMMENT '当前版本号：来自于升级计划中选定的固件版本号',
  `installed_version` varchar(128) DEFAULT NULL COMMENT '升级完成后版本号',
  `version` int(11) DEFAULT '0' COMMENT '数据版本控制字段',
  `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志\r\n            0 - 正常\r\n            1 - 删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `status` smallint(6) DEFAULT NULL COMMENT '升级任务状态:1=升级完成, 2=升级失败，3=升级未完成',
  `failed_code` varchar(50) DEFAULT NULL COMMENT '错误码',
  `file_path` varchar(255) DEFAULT NULL COMMENT '日志报文保存路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1291933169321345249 DEFAULT CHARSET=utf8mb4 COMMENT='OTA升级任务明细定义的是一辆车的一个零部件的一款软件所涉及的一个或多个软件的升级\r\n在创建升级计划时创建升级';


/*Table structure for table `tb_fota_upgrade_condition` */

DROP TABLE IF EXISTS `tb_fota_upgrade_condition`;

CREATE TABLE `tb_fota_upgrade_condition` (
  `id` bigint(16) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` varchar(50) DEFAULT NULL COMMENT '项目ID\r\n            多租户，不同的租户具有不同的项目ID',
  `cond_code` varchar(50) NOT NULL COMMENT '终端升级条件代码\r\n            比如：\r\n            1- 速度\r\n            2- 电压\r\n            3- ....\r\n            ',
  `cond_name` varchar(100) DEFAULT NULL COMMENT '条件名称',
  `value` varchar(255) DEFAULT NULL COMMENT '条件值',
  `value_source` smallint(6) DEFAULT NULL COMMENT '值来源方式\r\n            0-预定义枚举值\r\n            1-手工输入',
  `value_type` smallint(6) DEFAULT NULL COMMENT '值类型\r\n            0 - 数值型\r\n            1-字符型',
  `operator_type` smallint(2) DEFAULT NULL COMMENT '运算符类型:0=关系运算符(> < == != >= <=),1=逻辑运算符',
  `operator_value` smallint(2) DEFAULT NULL COMMENT '运算符',
  `version` int(11) DEFAULT '0' COMMENT '数据版本字段',
  `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志\r\n            0 - 正常\r\n            1 - 删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='终端升级条件项目定义';

/*Data for the table `tb_fota_upgrade_condition` */

insert  into `tb_fota_upgrade_condition`(`id`,`project_id`,`cond_code`,`cond_name`,`value`,`value_source`,`value_type`,`operator_type`,`operator_value`,`version`,`del_flag`,`create_by`,`create_time`,`update_by`,`update_time`) values 

(1,NULL,'001','车速小于20公里每小时','车速小于20公里每小时',1,1,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL),

(2,NULL,'002','挡位位于P档','挡位位于P档',1,1,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL),

(3,NULL,'003','电量高于70%','必须停车升级',1,1,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL),

(4,NULL,'004','可用存储空间大于4Gb','电量高于70%',1,1,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL),

(5,NULL,'005','手刹处于制动状态','手刹处于制动状态',1,1,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL),

(6,NULL,'006','充电中','充电中',1,1,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL),

(7,NULL,'007','放电中','放电中',1,1,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL),

(8,NULL,'008','车声esp稳定关闭','关闭时车正有危险！',2,2,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL),

(9,NULL,'009','刹车ECU模块条件','必须停车升级',1,1,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL);

/*Table structure for table `tb_fota_upgrade_error` */

DROP TABLE IF EXISTS `tb_fota_upgrade_error`;

CREATE TABLE `tb_fota_upgrade_error` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `upgrade_record_id` bigint(16) DEFAULT NULL COMMENT '升级记录ID',
  `version_info` varchar(50) DEFAULT NULL COMMENT '版本信息',
  `error_log` varchar(500) DEFAULT NULL COMMENT '错误日志',
  `version` int(11) DEFAULT '0' COMMENT '数据记录版本，用于控制并发',
  `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志\r\n            0 - 正常\r\n            1 - 删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `FK_Reference_18` (`upgrade_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OTA升级错误日志';

/*Data for the table `tb_fota_upgrade_error` */

/*Table structure for table `tb_fota_upgrade_process` */

DROP TABLE IF EXISTS `tb_fota_upgrade_process`;

CREATE TABLE `tb_fota_upgrade_process` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `upgrade_record_id` bigint(16) DEFAULT NULL COMMENT '升级记录ID',
  `process_id` varchar(20) DEFAULT NULL COMMENT '过程类型ID\r\n            OTA升级过程:\r\n            1:升级确认\r\n            2:升级包下载\r\n            3:升级包安装确认\r\n            4:升级包安装\r\n            5:软件回滚(软件回滚后汇报该过程)\r\n            　　',
  `process_name` varchar(50) DEFAULT NULL COMMENT '过程名称',
  `begin_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `status` varchar(2) DEFAULT NULL COMMENT '执行状态\r\n            S-成功\r\n            F-失败',
  `remark` varchar(200) DEFAULT NULL COMMENT '升级过程描述',
  `version` int(11) DEFAULT '0' COMMENT '数据记录版本，用于控制并发',
  `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志\r\n            0 - 正常\r\n            1 - 删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `FK_Reference_15` (`upgrade_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OTA升级由一系列的升级过程组成\r\nOTA升级过程:\r\n1:升级确认\r\n2:升级包下载\r\n                                            ';

/*Data for the table `tb_fota_upgrade_process` */

/*Table structure for table `tb_fota_upgrade_record` */

DROP TABLE IF EXISTS `tb_fota_upgrade_record`;

CREATE TABLE `tb_fota_upgrade_record` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `project_id` varchar(50) NOT NULL COMMENT '项目ID',
  `ota_task_detail_id` bigint(16) DEFAULT NULL COMMENT '升级任务明细ID\r\n            该字段，仅针对计划升级才需要保存任务id',
  `object_type` smallint(2) DEFAULT NULL COMMENT '升级对象类型\r\n            支持多类型升级对象的升级，目前只有车辆升级\r\n            0 - 车辆升级',
  `object_id` varchar(50) DEFAULT NULL COMMENT '对象ID\r\n            升级对象的唯一识别ID',
  `fota_object_firmware_id` bigint(16) DEFAULT NULL COMMENT '升级对象固件ID\r\n            指向升级对象固件清单',
  `firmware_id` bigint(16) NOT NULL COMMENT '升级固件ID',
  `upgrade_pkg_id` bigint(16) DEFAULT NULL COMMENT '升级包ID',
  `before_upgrade_version_id` bigint(16) NOT NULL COMMENT '升级前版本ID',
  `before_upgrade_version` varchar(128) NOT NULL COMMENT '升级前版本号',
  `after_upgrade_version_id` bigint(16) NOT NULL COMMENT '升级后版本ID',
  `upgraded_version` varchar(128) NOT NULL COMMENT '升级后版本号',
  `begin_time` datetime DEFAULT NULL COMMENT '整个OTA开始时间\r\n            从收到通知开始或从版本检查更新开始',
  `finish_time` datetime DEFAULT NULL COMMENT '结束时间',
  `status` smallint(2) DEFAULT NULL COMMENT '升级状态\r\n            0 - 失败\r\n            1 - 成功\r\n            2 - 升级中',
  `cur_upgrade_proc_code` varchar(50) DEFAULT NULL COMMENT '当前所处升级过程代码',
  `remarks` varchar(50) DEFAULT NULL COMMENT '备注',
  `version` int(11) DEFAULT '0' COMMENT '数据记录版本，用于控制并发',
  `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志\r\n            0 - 正常\r\n            1 - 删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `FK_Reference_28` (`upgrade_pkg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OTA升级记录指的是升级对象针对每一个升级软件的一次升级记录\r\n升级记录记录的是升级计划和任务的实际执行情况\r\n';

/*Data for the table `tb_fota_upgrade_record` */

/*Table structure for table `tb_fota_version_check` */

DROP TABLE IF EXISTS `tb_fota_version_check`;

CREATE TABLE `tb_fota_version_check` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `project_id` varchar(50) NOT NULL DEFAULT 'bngrp' COMMENT '项目id',
  `vehicle_id` varchar(50) NOT NULL COMMENT '汽车vin码',
  `business_id` bigint(16) NOT NULL COMMENT '服务端与客户端流程交互消息序列号',
  `req_time` datetime NOT NULL COMMENT '客户端发起请求时间',
  `check_result` smallint(2) DEFAULT '0' COMMENT '检查结果:0=没有新版本，1=存在新版本',
  `check_source_type` smallint(2) DEFAULT '2' COMMENT '版本检查请求来源：1=HU发起，2=TBOX主动发起检测，3=APP客户端发起，4、云端下发通知主动检测',
  `check_param` text NOT NULL COMMENT '版本检查请求参数',
  `check_response` text COMMENT '版本检查响应结果',
  `version` int(11) DEFAULT '0' COMMENT '数据并发控制字段',
  `del_flag` smallint(2) DEFAULT '0' COMMENT '删除标志:0-正常1-删除',
  `create_by` varchar(50) DEFAULT 'sys' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT 'sys' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1310181490881474563 DEFAULT CHARSET=utf8mb4;


/*Table structure for table `tb_fota_version_check_verify` */

DROP TABLE IF EXISTS `tb_fota_version_check_verify`;

CREATE TABLE `tb_fota_version_check_verify` (
  `id` bigint(16) NOT NULL COMMENT '主键Id',
  `project_id` varchar(50) DEFAULT 'bngrp' COMMENT '项目id',
  `object_id` bigint(16) unsigned zerofill NOT NULL COMMENT '升级对象Id',
  `ota_plan_object_id` bigint(16) NOT NULL COMMENT '升级任务对象Id',
  `ota_plan_id` bigint(16) NOT NULL COMMENT '升级任务Id',
  `check_req_id` bigint(16) NOT NULL COMMENT '客户端请求记录Id',
  `vin` varchar(50) NOT NULL COMMENT '车辆Id,默认为vin码，冗余字段',
  `source_type` smallint(2) NOT NULL DEFAULT '0' COMMENT '版本检查发起来源类型:0=客户端请求类型，保留，默认为0',
  `check_ack_verify_status` smallint(2) DEFAULT '0' COMMENT 'TBOX获取新版本检查与云端确认已异步获取结果状态：1=确认已经获收到版本检查结果:0=未收到版本检查结果',
  `check_ack_verify_time` datetime DEFAULT NULL COMMENT 'TBOX获取新版本检查与云端确认已异步获取结果状态：1=确认已经获收到版本检查结果时间',
  `upgrade_req_time` datetime DEFAULT NULL COMMENT 'TBOX升级确认请求时间(成功获取新版本检查结果，与云端确认已收到)',
  `upgrade_verify_status` smallint(2) DEFAULT NULL COMMENT '客户端升级确认(远程下载)消息状态:0=放弃升级，1=确认升级',
  `upgrade_verify_source` smallint(2) DEFAULT NULL COMMENT '客户端升级确认(远程下载)来源类型：0=车机，1=App',
  `upgrade_verify_immediate` smallint(6) DEFAULT NULL COMMENT '客户端升级确认（远程下载是否可以立即下载）',
  `upgrade_verify_time` datetime DEFAULT NULL COMMENT '客户端升级确认消息时间(远程下载)',
  `upgrade_verify_push` smallint(2) DEFAULT '0' COMMENT '升级确认消息（远程下载）下发状态:0=未下发，1=已下发（下发至客户端APP）保留',
  `download_start_time` datetime DEFAULT NULL COMMENT '下载开始时间',
  `download_end_time` datetime DEFAULT NULL COMMENT '下载结束时间',
  `download_full_size` bigint(11) DEFAULT NULL COMMENT '下载包总大小',
  `download_finished_size` bigint(11) DEFAULT NULL COMMENT '下载包已完成大小',
  `download_percent_rate` smallint(2) DEFAULT '0' COMMENT '下载完成百分比',
  `download_spend_time` smallint(4) DEFAULT '0' COMMENT '下载实际花费时间:时间为秒',
  `download_report_time` datetime DEFAULT NULL COMMENT '下载进度上报时间',
  `download_status` smallint(2) DEFAULT NULL COMMENT '下载完成状态：0=未开始，1=下载中，2=下载中止，3下载等待，4=下载完成，5=校验成功，6=校验失败',
  `installed_ack_verify_status` smallint(2) DEFAULT '0' COMMENT 'TBOX升级确认请求已收到状态：1=已收到，0=未收到',
  `installed_ack_verify_time` datetime DEFAULT NULL COMMENT 'TBOX升级确认请求已收到时间',
  `installed_req_time` datetime DEFAULT NULL COMMENT '升级确认请求时间',
  `installed_verify_status` smallint(2) DEFAULT NULL COMMENT '升级安装消息确认状态:1=现在升级,2=预约升级,3=放弃安装',
  `installed_verify_booked_time` datetime DEFAULT NULL COMMENT '预约升级的时间点：installed_verify_status=2时该值不为空',
  `installed_verify_source` smallint(2) DEFAULT NULL COMMENT '升级安装消息确认来源：0=车机，1=App',
  `installed_verify_time` datetime DEFAULT NULL COMMENT '升级安装消息确认时间',
  `installed_verify_push` smallint(2) DEFAULT '0' COMMENT '升级安装确认消息状态:0=未下发，1=已下发（保留）',
  `installed_percent_rate` smallint(2) DEFAULT '0' COMMENT '安装完成百分比（保留）',
  `installed_total_num` smallint(2) DEFAULT NULL COMMENT '安装升级包总数量',
  `installed_current_index` smallint(2) DEFAULT NULL COMMENT '当前安装升级包序号（第几个）',
  `installed_spend_time` bigint(16) DEFAULT '0' COMMENT '安装花费时间:时间为s',
  `installed_report_time` datetime DEFAULT NULL COMMENT '安装进度上报时间',
  `installed_status` smallint(2) DEFAULT '0' COMMENT '安装状态：0=未开始，1=前置条件检查失败，2=升级中，3=hu取消升级',
  `installed_complete_status` smallint(10) DEFAULT NULL COMMENT '安装结果：1=升级完成（成功），2=升级未完成，3=升级失败',
  `installed_complete_time` datetime DEFAULT NULL COMMENT '安装结果上报时间',
  `installed_start_time` datetime DEFAULT NULL COMMENT '安装开始时间',
  `installed_end_time` datetime DEFAULT NULL COMMENT '安装结束时间',
  `version` int(11) DEFAULT '0' COMMENT '数据并发控制字段',
  `del_flag` smallint(2) DEFAULT '0' COMMENT '删除标志:0-正常1-删除',
  `create_by` varchar(50) DEFAULT 'sys' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT 'sys' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OTA升级版本结果确认表';


/*Table structure for table `tb_req_from_app` */

DROP TABLE IF EXISTS `tb_req_from_app`;

CREATE TABLE `tb_req_from_app` (
  `id` bigint(16) NOT NULL COMMENT '自增序列  即ID',
  `project_id` varchar(50) DEFAULT NULL COMMENT '项目ID',
  `device_id` varchar(128) DEFAULT NULL COMMENT '设备id',
  `device_type` smallint(2) DEFAULT NULL COMMENT '设备类型（APP客户端设备类型）:1=安卓,2=ios',
  `vin` varchar(128) DEFAULT NULL COMMENT 'vin码',
  `req_type` smallint(2) DEFAULT NULL COMMENT '请求类型：1=版本检查，2=升级确认，3=安装确认',
  `trans_id` bigint(16) DEFAULT NULL COMMENT '升级事务Id',
  `task_id` bigint(16) DEFAULT NULL COMMENT '升级任务Id',
  `extra` text COMMENT '附加属性',
  `ack_status` smallint(2) DEFAULT NULL COMMENT 'OTA云端推送会APP状态：0=新建，1=TBOX已响应，2=已推送成功，3=推送异常',
  `version` int(11) DEFAULT '0' COMMENT '数据记录版本，用于控制并发',
  `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志\r\n            0 - 正常\r\n            1 - 删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `index_config_code_path` (`device_type`) USING BTREE COMMENT '升级对象唯一索引，一般为车Vin码',
  KEY `index_treenode` (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储来自APP的请求';

ALTER TABLE tb_req_from_app MODIFY COLUMN device_type smallint(2) NULL COMMENT '设备类型（APP客户端设备类型）:1=安卓,2=ios';


/*Table structure for table `tb_task_terminate` */

DROP TABLE IF EXISTS `tb_task_terminate`;

CREATE TABLE `tb_task_terminate` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `ota_plan_id` bigint(16) DEFAULT NULL COMMENT '升级计划ID',
  `task_id` bigint(20) DEFAULT NULL COMMENT '关联的任务ID',
  `max_download_failed` int(10) DEFAULT NULL COMMENT '最大下载失败次数',
  `max_install_failed` int(10) DEFAULT NULL COMMENT '最大安装失败次数',
  `max_verify_failed` int(10) DEFAULT NULL COMMENT '最大验证失败次数',
  `version` int(11) DEFAULT '0' COMMENT '数据记录版本，用于控制并发',
  `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志\r\n            0 - 正常\r\n            1 - 删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1288414935762661462 DEFAULT CHARSET=utf8mb4 COMMENT='任务终止条件';

/*Data for the table `tb_task_terminate` */

/*Table structure for table `tb_upgrade_strategy` */

DROP TABLE IF EXISTS `tb_upgrade_strategy`;

CREATE TABLE `tb_upgrade_strategy` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `ota_plan_id` bigint(16) DEFAULT NULL COMMENT '升级计划ID',
  `task_id` bigint(20) DEFAULT NULL COMMENT '关联的任务ID',
  `firmware_id` bigint(20) DEFAULT NULL COMMENT '关联固件ID',
  `firmware_version_id` bigint(20) DEFAULT NULL COMMENT '固件版本',
  `rollback_mode` tinyint(4) DEFAULT NULL COMMENT '回滚模式； 1 - 回滚， 0 - 不回滚',
  `max_concurrent` int(4) DEFAULT NULL COMMENT '最大并发数',
  `version` int(4) DEFAULT '0' COMMENT '数据并发控制版本',
  `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志：0 - 正常，1 - 删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=575 DEFAULT CHARSET=utf8mb4 COMMENT='升级策略';

/*Table structure for table `tb_upgrade_task_condition` */

DROP TABLE IF EXISTS `tb_upgrade_task_condition`;

CREATE TABLE `tb_upgrade_task_condition` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `project_id` varchar(50) DEFAULT NULL COMMENT '项目ID\r\n            多租户，不同的租户具有不同的项目ID',
  `task_id` bigint(16) NOT NULL COMMENT '关联任务ID（与ota_plan_id意义一致，后续可能会移除）',
  `ota_plan_id` bigint(16) DEFAULT NULL COMMENT '关联升级计划任务ID（与taskId意义一致，为了统一）',
  `condition_id` bigint(16) NOT NULL COMMENT '条件项ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=727 DEFAULT CHARSET=utf8mb4 COMMENT='终端升级条件枚举值';


-- OTA所需零件升级字段信息
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_firmware ADD info_coll_ctrl_obj varchar(100) NULL COMMENT '信息采集受控对象';
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_firmware ADD dl_ctrl_obj varchar(100) NULL COMMENT '下载受控对象';
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_firmware ADD inst_tx_ctrl_obj varchar(100) NULL COMMENT '安装传输受控对象';
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_firmware ADD inst_ctrl_obj varchar(100) NULL COMMENT '安装受控对象';
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_firmware ADD inst_condition tinyint(2) DEFAULT 0 COMMENT '安装约束（零件需要处于低压或高压条件下才可以安装）：0 低压 1 高压 默认：0 低压';
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_firmware ADD bus_type tinyint(2) NULL COMMENT '总线类型 1 can节点 2 以太网节点';
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_firmware ADD info_coll_script_type tinyint(2) NULL COMMENT '信息采集脚本类型';
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_firmware ADD inst_algorithm_type tinyint(2) NULL COMMENT '安装算法类型(SA)算法类型';
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_firmware ADD inst_script_url varchar(100) NULL COMMENT '安装脚本地址';

ALTER TABLE tb_fota_version_check_verify ADD upgrade_log varchar(500) NULL COMMENT 'tbox升级日志持久化信息';



-- PKI加密SQL变更
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_firmware_pkg ADD encrypt_pkg_status tinyint(2) default 0 COMMENT '升级包加密状态 0未加密 1加密中 2加密成功 3加密失败';
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_firmware_pkg ADD encrypt_upload_file_id bigint(16) null COMMENT '加密后升级包文件上传id';
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_firmware_pkg MODIFY COLUMN release_pkg_sign varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '升级包签名全量升级包的签名信息，采用服务端私钥签名而成';
