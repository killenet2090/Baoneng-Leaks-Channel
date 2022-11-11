CREATE TABLE `tb_device_firmware_list` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` varchar(50) DEFAULT NULL COMMENT '项目Id',
  `tree_node_id` bigint(16) DEFAULT NULL COMMENT '关联的树节点ID',
  `device_list_id` bigint(16) DEFAULT NULL COMMENT '关联的设备零件列表ID',
  `firmware_id` bigint(16) DEFAULT NULL COMMENT '软件ID',
  `version` int(11) DEFAULT '0' COMMENT '数据并发控制字段',
  `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志: 0 - 正常,1 - 删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='OTA升级硬件与固件关联清单';


-- 修改tb_fota_firmwared表中tree_node_id的值

UPDATE tb_fota_firmware t1
LEFT JOIN  tb_device_tree_node t2
ON t1.tree_node_id = t2.id
set t1.tree_node_id = t2.parent_id

