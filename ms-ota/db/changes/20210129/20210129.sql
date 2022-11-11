use bnmotor_icv_vehicle_fota;

-- ������ڣ�2021-01-29
-- ����ˣ���С��



-- �ļ��ϴ���¼������Ƿ�ͨ���ֿ��ϴ����
ALTER TABLE `tb_fota_file_upload_record` 
ADD COLUMN `upload_slice` smallint(2) COMMENT '�Ƿ��Ƭ�ϴ�' AFTER `fast`;

-- �ļ���������Ӳ��Ա����ļ�Id
ALTER TABLE `tb_fota_firmware_pkg` 
ADD COLUMN `report_upload_file_id` bigint(16) COMMENT '���Ա����ļ�Id' AFTER `encrypt_upload_file_id`;


-- �Ƴ��ǿ��ֶ����ã�bnlink_1.3�汾�������ֶ��Ѿ�����
-- ������ڣ�2021-02-03
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_plan` 
MODIFY COLUMN `plan_desc` varchar(200) DEFAULT '' COMMENT '�ƻ�����' AFTER `batch_size`,
MODIFY COLUMN `download_tips` text COMMENT '������ʾ��' AFTER `new_version_tips`,
MODIFY COLUMN `task_tips` text COMMENT '������ʾ' AFTER `disclaimer`;


-- �޸Ĳ���״̬ע��
-- ������ڣ�2021-02-04
ALTER TABLE `tb_fota_strategy` 
MODIFY COLUMN `status` smallint(2) DEFAULT 0 COMMENT '״̬:0=�½�,1=������,2=���ͨ����3=�����ܾ�' AFTER `estimate_upgrade_time`,
ADD COLUMN `is_enable` smallint(2) NOT NULL DEFAULT 0 COMMENT '�Ƿ���:0=��������1=����' AFTER `status`;



update tb_fota_strategy set is_enable = 0 where is_enable is null;


-- �������������״̬��
-- �������: 2021-02-05
ALTER TABLE `tb_fota_firmware_pkg` 
ADD COLUMN `build_pkg_code` smallint(8) COMMENT '������״̬�룬�ο�CodeEnum����' AFTER `build_pkg_time`;


-- ������ǩ����ӱ�ǩ����Ϣ
-- �������: 2021-02-05
ALTER TABLE `tb_fota_object_label` 
ADD COLUMN `label_group_id` bigint(16) COMMENT '��ǩ��Id' AFTER `vin`,
ADD COLUMN `label_group_name` varchar(128) COMMENT '��ǩ��' AFTER `label_group_id`;

COMMIT;