use bnmotor_icv_vehicle_fota;

-- 变更日期：2020-11-10
-- 变更说明：
-- 1、修改OTA数据库，删除一些无用字段
-- 2、固件表添加升级需要补充字段
-- 3、添加零件库和车型零件关系表
-- 变更人：许小常

-- 添加数据必备属性字段
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_upgrade_task_condition` ADD COLUMN `version` int(4) DEFAULT '0' COMMENT '数据并发控制版本';
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_upgrade_task_condition` ADD COLUMN `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志：0 - 正常，1 - 删除';
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_upgrade_task_condition` ADD COLUMN  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人';
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_upgrade_task_condition` ADD COLUMN `create_time` datetime DEFAULT NULL COMMENT '创建时间';
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_upgrade_task_condition` ADD COLUMN `update_by` varchar(50) DEFAULT NULL COMMENT '更新人';
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_upgrade_task_condition` ADD COLUMN `update_time` datetime DEFAULT NULL COMMENT '更新时间';


-- 添加固件新增属性字段 
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` ADD COLUMN `info_coll_ctrl_obj` varchar(100) DEFAULT NULL COMMENT '信息采集受控对象' after `flash_script_url`;
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` ADD COLUMN `dl_ctrl_obj` varchar(100) DEFAULT NULL COMMENT '下载受控对象' after `info_coll_ctrl_obj`;
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` ADD COLUMN `inst_tx_ctrl_obj` varchar(100) DEFAULT NULL COMMENT '安装传输受控对象' after `dl_ctrl_obj`;
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` ADD COLUMN `inst_ctrl_obj` varchar(100) DEFAULT NULL COMMENT '安装受控对象' after `inst_tx_ctrl_obj`;
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` ADD COLUMN `inst_condition` tinyint(2) DEFAULT '0' COMMENT '安装约束（零件需要处于低压或高压条件下才可以安装）：0 低压 1 高压 默认：0 低压' after `inst_ctrl_obj`;
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` ADD COLUMN `bus_type` tinyint(2) DEFAULT NULL COMMENT '总线类型 1 can节点 2 以太网节点' after `inst_condition`;
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` ADD COLUMN `info_coll_script_type` tinyint(2) DEFAULT NULL COMMENT '信息采集脚本类型' after `bus_type`;
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` ADD COLUMN `inst_algorithm_type` tinyint(2) DEFAULT NULL COMMENT '安装算法类型(SA)算法类型' after `info_coll_script_type`;
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` ADD COLUMN `inst_script_url` varchar(100) DEFAULT NULL COMMENT '安装脚本地址' after `inst_algorithm_type`;


-- 移除多余的字段 
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_upgrade_task_condition` 
DROP COLUMN `task_id`;

-- 移除多余的字段 
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_upgrade_strategy` 
DROP COLUMN `task_id`;

-- 移除多余的字段 
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_task_terminate` 
DROP COLUMN `task_id`;

-- 移除多余的字段 
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_plan_obj_list` 
DROP COLUMN `object_id`,
DROP COLUMN `task_id`;

-- 移除多余的字段 `task_id`
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_plan_firmware_list` 
DROP COLUMN `task_id`;


-- 添加OTA升级硬件设备信息关系表
CREATE TABLE `tb_fota_component_list` (
  `id` bigint(16) NOT NULL COMMENT 'ID',
  `project_id` varchar(50) DEFAULT NULL COMMENT '项目ID多租户，不同的租户具有不同的项目ID',
  `device_component_id` bigint(16) NOT NULL COMMENT '零件Id',
  `tree_node_id` bigint(16) NOT NULL COMMENT '零件型号',
  `tree_node_code_path` varchar(768) DEFAULT NULL COMMENT '设备树节点业务Code路径',
  `version` int(11) DEFAULT '0' COMMENT '数据版本d，用于更新并发控制使用',
  `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志:0-正常1-删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='OTA升级硬件设备信息关系表';


-- 添加OTA升级硬件设备信息库
CREATE TABLE `tb_device_component` (
  `id` bigint(16) NOT NULL COMMENT 'ID',
  `project_id` varchar(50) DEFAULT NULL COMMENT '项目ID多租户，不同的租户具有不同的项目ID',
  `component_code` varchar(50) NOT NULL COMMENT '零件代码区分不同零件的代号',
  `component_model` varchar(50) DEFAULT NULL COMMENT '零件型号',
  `component_name` varchar(50) DEFAULT NULL COMMENT '零件名称',
  `version` int(11) DEFAULT '0' COMMENT '数据版本d，用于更新并发控制使用',
  `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志:0-正常1-删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='OTA升级硬件设备信息库';

ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_device_component` 
ADD UNIQUE INDEX `uk_code_model`(`component_code`, `component_model`) USING BTREE COMMENT '零件代码和型号组成零件的唯一标识';


commit;