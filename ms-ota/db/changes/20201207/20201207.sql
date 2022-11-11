use bnmotor_icv_vehicle_fota;

-- 变更日期：2020-12-07
-- 变更说明：
-- 1、添加任务策略表和任务策略关联表
-- 2、固件表添加升级需要补充字段
-- 3、添加零件库和车型零件关系表
-- 变更人：许小常


-- 添加OTA升级对象零件表

CREATE TABLE `tb_fota_object_component_list` (
  `id` bigint(16) NOT NULL COMMENT 'ID',
  `project_id` varchar(50) DEFAULT NULL COMMENT '项目Id',
  `ota_object_id` bigint(16) DEFAULT NULL COMMENT '所属升级对象ID',
  `component_type` varchar(50) DEFAULT NULL COMMENT '零件类型,同设备库元数据component_code定义',
  `component_type_name` varchar(50) DEFAULT NULL COMMENT '零件类型名称',
  `component_model` varchar(50) DEFAULT NULL COMMENT '零件型号',
  `component_name` varchar(50) DEFAULT NULL COMMENT '零件名称',
  `version` int(11) DEFAULT '0' COMMENT '数据并发控制字段',
  `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志：0=正常, 1=删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OTA升级对象零件表,用于接收车辆零件信息同步数据';



-- 修改固件安装相关控制项数据类型
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` 
MODIFY COLUMN `info_coll_ctrl_obj` tinyint(2) DEFAULT NULL COMMENT '信息采集受控对象:0=Master, 1=Slave-HMI' AFTER `flash_script_url`,
MODIFY COLUMN `dl_ctrl_obj` tinyint(2) DEFAULT NULL COMMENT '下载受控对象:0=Master' AFTER `info_coll_ctrl_obj`,
MODIFY COLUMN `inst_tx_ctrl_obj` tinyint(2) DEFAULT NULL COMMENT '安装传输受控对象:0=Master, 1=Slave-HMI' AFTER `dl_ctrl_obj`,
MODIFY COLUMN `inst_ctrl_obj` tinyint(2) DEFAULT NULL COMMENT '安装受控对象:0=Master, 1=Slave-HMI' AFTER `inst_tx_ctrl_obj`,
MODIFY COLUMN `info_coll_script_type` tinyint(2) DEFAULT NULL COMMENT '业务流程脚本，指升级对象进行OTA升级涉及的业务功能流程所需要用到的脚本类型，如A或B或C' AFTER `inst_ctrl_obj`,
MODIFY COLUMN `inst_algorithm_type` tinyint(2) DEFAULT NULL COMMENT '安装算法类型(SA)算法类型，0=Default；默认=0' AFTER `info_coll_script_type`,
MODIFY COLUMN `bus_type` tinyint(2) DEFAULT NULL COMMENT '总线类型:0=ETH, 1=CAN, 2=ETH/CAN' AFTER `inst_algorithm_type`,
MODIFY COLUMN `inst_condition` tinyint(2) DEFAULT 0 COMMENT '安装约束（零件需要处于低压或高压条件下才可以安装）:0=低压 1=高压' AFTER `bus_type`;


-- 添加ota升级策略表
CREATE TABLE `tb_fota_strategy` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT, 
  `project_id` varchar(50) DEFAULT NULL COMMENT '项目ID',
  `tree_node_id` bigint(16) DEFAULT NULL COMMENT '设备树节点ID',
  `tree_node_code_path` varchar(768) DEFAULT NULL COMMENT '设备树节点业务Code路径',
  `brand` varchar(128) NOT NULL DEFAULT '' COMMENT '品牌名称',
  `brand_code` varchar(64) NOT NULL DEFAULT '' COMMENT '品牌代码',
  `series` varchar(128) NOT NULL DEFAULT '' COMMENT '系列名称',
  `series_code` varchar(64) NOT NULL DEFAULT '' COMMENT '系列代码',
  `model` varchar(128) NOT NULL DEFAULT '' COMMENT '车型名称',
  `model_code` varchar(64) NOT NULL DEFAULT '' COMMENT '车型代码',
  `year` varchar(128) NOT NULL DEFAULT '' COMMENT '年款名称',
  `year_code` varchar(64) NOT NULL DEFAULT '' COMMENT '年款代码',
  `conf` varchar(128) NOT NULL DEFAULT '' COMMENT '配置名称',
  `conf_code` varchar(64) NOT NULL DEFAULT '' COMMENT '配置代码',
  `name` varchar(128) NOT NULL DEFAULT '' COMMENT '策略名称',
  `entire_version_no` varchar(128) NOT NULL DEFAULT '' COMMENT '整车版本号',
  `rollback_mode` tinyint(4) DEFAULT NULL COMMENT '回滚模式； 1 - 激进策略， 0 - 保守策略',
  `estimate_upgrade_time` smallint(10) COMMENT '预估升级时长',
  `status` smallint(2) DEFAULT NULL COMMENT '状态:0=新建,1=审批中,2=审核通过，3=审批拒绝，4=失效',
  `version` int(4) DEFAULT '0' COMMENT '数据并发控制版本',
  `del_flag` smallint(2) DEFAULT '0' COMMENT '删除标志：0 - 正常，1 - 删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='OTA升级策略-新表';


-- 添加ota升级任务策略关联表

CREATE TABLE `tb_fota_strategy_firmware_list` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(50) DEFAULT NULL COMMENT '项目ID',
  `ota_strategy_id` bigint(16) DEFAULT NULL COMMENT '策略ID',
  `component_list_id` bigint(16) DEFAULT NULL COMMENT '关联的设备零件列表ID',
  `firmware_id` bigint(16) DEFAULT NULL COMMENT '软件ID',
  `upgrade_mode` tinyint(4) DEFAULT NULL COMMENT '升级包模式； 2=差分升级包， 1=补丁升级包模式(保留)，0=全量升级包',
  `start_source_id` bigint(16) DEFAULT NULL COMMENT '前置版本Id',
  `target_source_id` bigint(16) DEFAULT NULL COMMENT '目标版本Id',
  `order_num` smallint(2) DEFAULT '0' COMMENT '升级顺序',
  `group_seq` smallint(2) DEFAULT '0' COMMENT '分组信息',
  `version` int(4) DEFAULT '0' COMMENT '数据并发控制版本',
  `del_flag` smallint(2) DEFAULT '0' COMMENT '删除标志：0 - 正常，1 - 删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='OTA升级策略-升级ecu固件列表';

-- 添加ota升级策略前置条件表

CREATE TABLE `tb_fota_strategy_pre_condition` (
  `id` bigint(16) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` varchar(50) DEFAULT NULL COMMENT '项目ID,多租户，不同的租户具有不同的项目ID',
  `ota_strategy_id` bigint(16) DEFAULT NULL COMMENT '策略ID',
  `cond_code` varchar(50) NOT NULL COMMENT '终端升级条件代码,比如：1=电源，2=档位,3=....',
  `cond_name` varchar(100) DEFAULT NULL COMMENT '条件名称',
  `value` varchar(255) NOT NULL COMMENT '条件值',
  `value_type` smallint(6) DEFAULT '1' COMMENT '值类型:1=数值型, 2=字符型',
  `operator_type` smallint(2) DEFAULT '1' COMMENT '运算符类型:1=关系运算符(> < == != >= <=),2=逻辑运算符',
  `operator_value` smallint(2) DEFAULT NULL COMMENT '运算符',
  `version` int(11) DEFAULT '0' COMMENT '数据版本字段',
  `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志：0=正常，1=删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='升级策略前置条件表';


-- 添加车型零件固件关联表

CREATE TABLE `tb_device_firmware_list` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` varchar(50) DEFAULT NULL COMMENT '项目Id',
  `tree_node_id` bigint(16) DEFAULT NULL COMMENT '关联的树节点ID',
  `component_list_id` bigint(16) DEFAULT NULL COMMENT '关联的设备零件列表ID',
  `firmware_id` bigint(16) DEFAULT NULL COMMENT '软件ID',
  `version` int(11) DEFAULT '0' COMMENT '数据并发控制字段',
  `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志: 0 - 正常,1 - 删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='OTA升级硬件与固件关联清单';



-- 任务表添加任务模式+任务选择策略Id

ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_plan` 
ADD COLUMN `plan_mode` smallint(1) COMMENT '任务模式: 0=测试任务,1=正式任务' AFTER `plan_type`,
ADD COLUMN `ota_strategy_id` bigint(16) COMMENT '任务策略Id' AFTER `upgrade_mode`;

commit;