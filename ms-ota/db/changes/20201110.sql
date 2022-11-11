use bnmotor_icv_vehicle_fota;

-- ������ڣ�2020-11-10
-- ���˵����
-- 1���޸�OTA���ݿ⣬ɾ��һЩ�����ֶ�
-- 2���̼������������Ҫ�����ֶ�
-- 3����������ͳ��������ϵ��
-- ����ˣ���С��

-- ������ݱر������ֶ�
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_upgrade_task_condition` ADD COLUMN `version` int(4) DEFAULT '0' COMMENT '���ݲ������ư汾';
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_upgrade_task_condition` ADD COLUMN `del_flag` smallint(6) DEFAULT '0' COMMENT 'ɾ����־��0 - ������1 - ɾ��';
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_upgrade_task_condition` ADD COLUMN  `create_by` varchar(50) DEFAULT NULL COMMENT '������';
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_upgrade_task_condition` ADD COLUMN `create_time` datetime DEFAULT NULL COMMENT '����ʱ��';
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_upgrade_task_condition` ADD COLUMN `update_by` varchar(50) DEFAULT NULL COMMENT '������';
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_upgrade_task_condition` ADD COLUMN `update_time` datetime DEFAULT NULL COMMENT '����ʱ��';


-- ��ӹ̼����������ֶ� 
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` ADD COLUMN `info_coll_ctrl_obj` varchar(100) DEFAULT NULL COMMENT '��Ϣ�ɼ��ܿض���' after `flash_script_url`;
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` ADD COLUMN `dl_ctrl_obj` varchar(100) DEFAULT NULL COMMENT '�����ܿض���' after `info_coll_ctrl_obj`;
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` ADD COLUMN `inst_tx_ctrl_obj` varchar(100) DEFAULT NULL COMMENT '��װ�����ܿض���' after `dl_ctrl_obj`;
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` ADD COLUMN `inst_ctrl_obj` varchar(100) DEFAULT NULL COMMENT '��װ�ܿض���' after `inst_tx_ctrl_obj`;
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` ADD COLUMN `inst_condition` tinyint(2) DEFAULT '0' COMMENT '��װԼ���������Ҫ���ڵ�ѹ���ѹ�����²ſ��԰�װ����0 ��ѹ 1 ��ѹ Ĭ�ϣ�0 ��ѹ' after `inst_ctrl_obj`;
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` ADD COLUMN `bus_type` tinyint(2) DEFAULT NULL COMMENT '�������� 1 can�ڵ� 2 ��̫���ڵ�' after `inst_condition`;
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` ADD COLUMN `info_coll_script_type` tinyint(2) DEFAULT NULL COMMENT '��Ϣ�ɼ��ű�����' after `bus_type`;
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` ADD COLUMN `inst_algorithm_type` tinyint(2) DEFAULT NULL COMMENT '��װ�㷨����(SA)�㷨����' after `info_coll_script_type`;
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` ADD COLUMN `inst_script_url` varchar(100) DEFAULT NULL COMMENT '��װ�ű���ַ' after `inst_algorithm_type`;


-- �Ƴ�������ֶ� 
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_upgrade_task_condition` 
DROP COLUMN `task_id`;

-- �Ƴ�������ֶ� 
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_upgrade_strategy` 
DROP COLUMN `task_id`;

-- �Ƴ�������ֶ� 
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_task_terminate` 
DROP COLUMN `task_id`;

-- �Ƴ�������ֶ� 
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_plan_obj_list` 
DROP COLUMN `object_id`,
DROP COLUMN `task_id`;

-- �Ƴ�������ֶ� `task_id`
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_plan_firmware_list` 
DROP COLUMN `task_id`;


-- ���OTA����Ӳ���豸��Ϣ��ϵ��
CREATE TABLE `tb_fota_component_list` (
  `id` bigint(16) NOT NULL COMMENT 'ID',
  `project_id` varchar(50) DEFAULT NULL COMMENT '��ĿID���⻧����ͬ���⻧���в�ͬ����ĿID',
  `device_component_id` bigint(16) NOT NULL COMMENT '���Id',
  `tree_node_id` bigint(16) NOT NULL COMMENT '����ͺ�',
  `tree_node_code_path` varchar(768) DEFAULT NULL COMMENT '�豸���ڵ�ҵ��Code·��',
  `version` int(11) DEFAULT '0' COMMENT '���ݰ汾d�����ڸ��²�������ʹ��',
  `del_flag` smallint(6) DEFAULT '0' COMMENT 'ɾ����־:0-����1-ɾ��',
  `create_by` varchar(50) DEFAULT NULL COMMENT '������',
  `create_time` datetime DEFAULT NULL COMMENT '����ʱ��',
  `update_by` varchar(50) DEFAULT NULL COMMENT '������',
  `update_time` datetime DEFAULT NULL COMMENT '����ʱ��',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='OTA����Ӳ���豸��Ϣ��ϵ��';


-- ���OTA����Ӳ���豸��Ϣ��
CREATE TABLE `tb_device_component` (
  `id` bigint(16) NOT NULL COMMENT 'ID',
  `project_id` varchar(50) DEFAULT NULL COMMENT '��ĿID���⻧����ͬ���⻧���в�ͬ����ĿID',
  `component_code` varchar(50) NOT NULL COMMENT '����������ֲ�ͬ����Ĵ���',
  `component_model` varchar(50) DEFAULT NULL COMMENT '����ͺ�',
  `component_name` varchar(50) DEFAULT NULL COMMENT '�������',
  `version` int(11) DEFAULT '0' COMMENT '���ݰ汾d�����ڸ��²�������ʹ��',
  `del_flag` smallint(6) DEFAULT '0' COMMENT 'ɾ����־:0-����1-ɾ��',
  `create_by` varchar(50) DEFAULT NULL COMMENT '������',
  `create_time` datetime DEFAULT NULL COMMENT '����ʱ��',
  `update_by` varchar(50) DEFAULT NULL COMMENT '������',
  `update_time` datetime DEFAULT NULL COMMENT '����ʱ��',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='OTA����Ӳ���豸��Ϣ��';

ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_device_component` 
ADD UNIQUE INDEX `uk_code_model`(`component_code`, `component_model`) USING BTREE COMMENT '���������ͺ���������Ψһ��ʶ';


commit;