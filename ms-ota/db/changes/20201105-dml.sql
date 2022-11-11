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

ALTER TABLE `bnmotor_icv_vehicle_fota_dev`.`tb_device_component` 
ADD UNIQUE INDEX `uk_code_model`(`component_code`, `component_model`) USING BTREE COMMENT '零件代码和型号组成零件的唯一标识';