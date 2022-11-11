use bnmotor_icv_vehicle_fota;

-- ������ڣ�2020-12-07
-- ���˵����
-- 1�����������Ա��������Թ�����
-- 2���̼������������Ҫ�����ֶ�
-- 3����������ͳ��������ϵ��
-- ����ˣ���С��


-- ���OTA�������������

CREATE TABLE `tb_fota_object_component_list` (
  `id` bigint(16) NOT NULL COMMENT 'ID',
  `project_id` varchar(50) DEFAULT NULL COMMENT '��ĿId',
  `ota_object_id` bigint(16) DEFAULT NULL COMMENT '������������ID',
  `component_type` varchar(50) DEFAULT NULL COMMENT '�������,ͬ�豸��Ԫ����component_code����',
  `component_type_name` varchar(50) DEFAULT NULL COMMENT '�����������',
  `component_model` varchar(50) DEFAULT NULL COMMENT '����ͺ�',
  `component_name` varchar(50) DEFAULT NULL COMMENT '�������',
  `version` int(11) DEFAULT '0' COMMENT '���ݲ��������ֶ�',
  `del_flag` smallint(6) DEFAULT '0' COMMENT 'ɾ����־��0=����, 1=ɾ��',
  `create_by` varchar(50) DEFAULT NULL COMMENT '������',
  `create_time` datetime DEFAULT NULL COMMENT '����ʱ��',
  `update_by` varchar(50) DEFAULT NULL COMMENT '������',
  `update_time` datetime DEFAULT NULL COMMENT '����ʱ��',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OTA�������������,���ڽ��ճ��������Ϣͬ������';



-- �޸Ĺ̼���װ��ؿ�������������
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` 
MODIFY COLUMN `info_coll_ctrl_obj` tinyint(2) DEFAULT NULL COMMENT '��Ϣ�ɼ��ܿض���:0=Master, 1=Slave-HMI' AFTER `flash_script_url`,
MODIFY COLUMN `dl_ctrl_obj` tinyint(2) DEFAULT NULL COMMENT '�����ܿض���:0=Master' AFTER `info_coll_ctrl_obj`,
MODIFY COLUMN `inst_tx_ctrl_obj` tinyint(2) DEFAULT NULL COMMENT '��װ�����ܿض���:0=Master, 1=Slave-HMI' AFTER `dl_ctrl_obj`,
MODIFY COLUMN `inst_ctrl_obj` tinyint(2) DEFAULT NULL COMMENT '��װ�ܿض���:0=Master, 1=Slave-HMI' AFTER `inst_tx_ctrl_obj`,
MODIFY COLUMN `info_coll_script_type` tinyint(2) DEFAULT NULL COMMENT 'ҵ�����̽ű���ָ�����������OTA�����漰��ҵ������������Ҫ�õ��Ľű����ͣ���A��B��C' AFTER `inst_ctrl_obj`,
MODIFY COLUMN `inst_algorithm_type` tinyint(2) DEFAULT NULL COMMENT '��װ�㷨����(SA)�㷨���ͣ�0=Default��Ĭ��=0' AFTER `info_coll_script_type`,
MODIFY COLUMN `bus_type` tinyint(2) DEFAULT NULL COMMENT '��������:0=ETH, 1=CAN, 2=ETH/CAN' AFTER `inst_algorithm_type`,
MODIFY COLUMN `inst_condition` tinyint(2) DEFAULT 0 COMMENT '��װԼ���������Ҫ���ڵ�ѹ���ѹ�����²ſ��԰�װ��:0=��ѹ 1=��ѹ' AFTER `bus_type`;


-- ���ota�������Ա�
CREATE TABLE `tb_fota_strategy` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT, 
  `project_id` varchar(50) DEFAULT NULL COMMENT '��ĿID',
  `tree_node_id` bigint(16) DEFAULT NULL COMMENT '�豸���ڵ�ID',
  `tree_node_code_path` varchar(768) DEFAULT NULL COMMENT '�豸���ڵ�ҵ��Code·��',
  `brand` varchar(128) NOT NULL DEFAULT '' COMMENT 'Ʒ������',
  `brand_code` varchar(64) NOT NULL DEFAULT '' COMMENT 'Ʒ�ƴ���',
  `series` varchar(128) NOT NULL DEFAULT '' COMMENT 'ϵ������',
  `series_code` varchar(64) NOT NULL DEFAULT '' COMMENT 'ϵ�д���',
  `model` varchar(128) NOT NULL DEFAULT '' COMMENT '��������',
  `model_code` varchar(64) NOT NULL DEFAULT '' COMMENT '���ʹ���',
  `year` varchar(128) NOT NULL DEFAULT '' COMMENT '�������',
  `year_code` varchar(64) NOT NULL DEFAULT '' COMMENT '������',
  `conf` varchar(128) NOT NULL DEFAULT '' COMMENT '��������',
  `conf_code` varchar(64) NOT NULL DEFAULT '' COMMENT '���ô���',
  `name` varchar(128) NOT NULL DEFAULT '' COMMENT '��������',
  `entire_version_no` varchar(128) NOT NULL DEFAULT '' COMMENT '�����汾��',
  `rollback_mode` tinyint(4) DEFAULT NULL COMMENT '�ع�ģʽ�� 1 - �������ԣ� 0 - ���ز���',
  `estimate_upgrade_time` smallint(10) COMMENT 'Ԥ������ʱ��',
  `status` smallint(2) DEFAULT NULL COMMENT '״̬:0=�½�,1=������,2=���ͨ����3=�����ܾ���4=ʧЧ',
  `version` int(4) DEFAULT '0' COMMENT '���ݲ������ư汾',
  `del_flag` smallint(2) DEFAULT '0' COMMENT 'ɾ����־��0 - ������1 - ɾ��',
  `create_by` varchar(50) DEFAULT NULL COMMENT '������',
  `create_time` datetime DEFAULT NULL COMMENT '����ʱ��',
  `update_by` varchar(50) DEFAULT NULL COMMENT '������',
  `update_time` datetime DEFAULT NULL COMMENT '����ʱ��',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='OTA��������-�±�';


-- ���ota����������Թ�����

CREATE TABLE `tb_fota_strategy_firmware_list` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(50) DEFAULT NULL COMMENT '��ĿID',
  `ota_strategy_id` bigint(16) DEFAULT NULL COMMENT '����ID',
  `component_list_id` bigint(16) DEFAULT NULL COMMENT '�������豸����б�ID',
  `firmware_id` bigint(16) DEFAULT NULL COMMENT '���ID',
  `upgrade_mode` tinyint(4) DEFAULT NULL COMMENT '������ģʽ�� 2=����������� 1=����������ģʽ(����)��0=ȫ��������',
  `start_source_id` bigint(16) DEFAULT NULL COMMENT 'ǰ�ð汾Id',
  `target_source_id` bigint(16) DEFAULT NULL COMMENT 'Ŀ��汾Id',
  `order_num` smallint(2) DEFAULT '0' COMMENT '����˳��',
  `group_seq` smallint(2) DEFAULT '0' COMMENT '������Ϣ',
  `version` int(4) DEFAULT '0' COMMENT '���ݲ������ư汾',
  `del_flag` smallint(2) DEFAULT '0' COMMENT 'ɾ����־��0 - ������1 - ɾ��',
  `create_by` varchar(50) DEFAULT NULL COMMENT '������',
  `create_time` datetime DEFAULT NULL COMMENT '����ʱ��',
  `update_by` varchar(50) DEFAULT NULL COMMENT '������',
  `update_time` datetime DEFAULT NULL COMMENT '����ʱ��',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='OTA��������-����ecu�̼��б�';

-- ���ota��������ǰ��������

CREATE TABLE `tb_fota_strategy_pre_condition` (
  `id` bigint(16) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` varchar(50) DEFAULT NULL COMMENT '��ĿID,���⻧����ͬ���⻧���в�ͬ����ĿID',
  `ota_strategy_id` bigint(16) DEFAULT NULL COMMENT '����ID',
  `cond_code` varchar(50) NOT NULL COMMENT '�ն�������������,���磺1=��Դ��2=��λ,3=....',
  `cond_name` varchar(100) DEFAULT NULL COMMENT '��������',
  `value` varchar(255) NOT NULL COMMENT '����ֵ',
  `value_type` smallint(6) DEFAULT '1' COMMENT 'ֵ����:1=��ֵ��, 2=�ַ���',
  `operator_type` smallint(2) DEFAULT '1' COMMENT '���������:1=��ϵ�����(> < == != >= <=),2=�߼������',
  `operator_value` smallint(2) DEFAULT NULL COMMENT '�����',
  `version` int(11) DEFAULT '0' COMMENT '���ݰ汾�ֶ�',
  `del_flag` smallint(6) DEFAULT '0' COMMENT 'ɾ����־��0=������1=ɾ��',
  `create_by` varchar(50) DEFAULT NULL COMMENT '������',
  `create_time` datetime DEFAULT NULL COMMENT '����ʱ��',
  `update_by` varchar(50) DEFAULT NULL COMMENT '������',
  `update_time` datetime DEFAULT NULL COMMENT '����ʱ��',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='��������ǰ��������';


-- ��ӳ�������̼�������

CREATE TABLE `tb_device_firmware_list` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` varchar(50) DEFAULT NULL COMMENT '��ĿId',
  `tree_node_id` bigint(16) DEFAULT NULL COMMENT '���������ڵ�ID',
  `component_list_id` bigint(16) DEFAULT NULL COMMENT '�������豸����б�ID',
  `firmware_id` bigint(16) DEFAULT NULL COMMENT '���ID',
  `version` int(11) DEFAULT '0' COMMENT '���ݲ��������ֶ�',
  `del_flag` smallint(6) DEFAULT '0' COMMENT 'ɾ����־: 0 - ����,1 - ɾ��',
  `create_by` varchar(50) DEFAULT NULL COMMENT '������',
  `create_time` datetime DEFAULT NULL COMMENT '����ʱ��',
  `update_by` varchar(50) DEFAULT NULL COMMENT '������',
  `update_time` datetime DEFAULT NULL COMMENT '����ʱ��',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='OTA����Ӳ����̼������嵥';



-- ������������ģʽ+����ѡ�����Id

ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_plan` 
ADD COLUMN `plan_mode` smallint(1) COMMENT '����ģʽ: 0=��������,1=��ʽ����' AFTER `plan_type`,
ADD COLUMN `ota_strategy_id` bigint(16) COMMENT '�������Id' AFTER `upgrade_mode`;

commit;