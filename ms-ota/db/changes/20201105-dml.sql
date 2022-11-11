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

ALTER TABLE `bnmotor_icv_vehicle_fota_dev`.`tb_device_component` 
ADD UNIQUE INDEX `uk_code_model`(`component_code`, `component_model`) USING BTREE COMMENT '���������ͺ���������Ψһ��ʶ';