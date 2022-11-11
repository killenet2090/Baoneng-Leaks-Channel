use bnmotor_icv_vehicle_fota;

-- ������ڣ�2021-03-31
-- ����ˣ�E.YanLonG

-- �����������̼�¼��`tb_fota_approval_record`
CREATE TABLE `tb_fota_approval_record` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `business_key` varchar(50) DEFAULT NULL COMMENT '����ҵ��Ψһ���� OTA_1234567890123',
  `form_title` varchar(50) NOT NULL default '' COMMENT '����ʾ����',
  `approval_type` tinyint(4) NOT NULL COMMENT '�������� 1�������� 2��������',
  `definition_key` varchar(50) not null default '' comment '���̶���key',
  `process_instance_id` varchar(100) NOT NULL default '' COMMENT '��ӦId',
  `ota_object_key` bigint(16) DEFAULT NULL COMMENT '���Ի������������',
  `ota_object_body` TEXT DEFAULT NULL COMMENT '���Ի��������json��',
  `vars` varchar(500) DEFAULT NULL COMMENT 'ҳ�����ӵ�ַ',
  `status` smallint(2) DEFAULT '0' COMMENT '����״̬:1������ 2������ 3������ 4������ 5�ܾ� 6���� 7����',
  `description` varchar(500) DEFAULT NULL COMMENT '����˵��',
  `version` int(11) DEFAULT '0' COMMENT '���ݰ汾�����ڸ��²�������ʹ��',
  `del_flag` smallint(6) DEFAULT '0' COMMENT 'ɾ����־:0-����1-ɾ��',
  `create_by` varchar(50) DEFAULT NULL COMMENT '������',
  `create_time` datetime DEFAULT NULL COMMENT '����ʱ��',
  `update_by` varchar(50) DEFAULT NULL COMMENT '������',
  `update_time` datetime DEFAULT NULL COMMENT '����ʱ��',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='OTA��������Ϣ';



-- ���Ա���������������ļ��ϴ�id
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_strategy ADD file_record_id BIGINT(16) NULL COMMENT '�ϴ������������ļ�id' AFTER estimate_upgrade_time;
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_strategy ADD remark varchar(500) NULL COMMENT '������˱�ע' AFTER file_record_id;


-- �ļ��ϴ����޸�fileKey����
ALTER TABLE tb_fota_file_upload_record
MODIFY COLUMN `file_key` varchar(256) DEFAULT NULL COMMENT 'OSS�洢�е�keyֵ';