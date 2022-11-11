use bnmotor_icv_vehicle_fota;

-- ������ڣ�2021-01-29
-- ����ˣ���С��



-- �ļ��ϴ���¼������Ƿ�ͨ���ֿ��ϴ����
ALTER TABLE `tb_fota_file_upload_record` 
DROP COLUMN `upload_slice`;


-- �ļ���������Ӳ��Ա����ļ�Id
ALTER TABLE `tb_fota_firmware_pkg` 
DROP COLUMN `report_upload_file_id`;


-- �Ƴ��ǿ��ֶ����ã�bnlink_1.3�汾�������ֶ��Ѿ�����
-- ������ڣ�2021-02-03
ALTER TABLE `tb_fota_plan` 
MODIFY COLUMN `plan_desc` varchar(200) NOT NULL DEFAULT '' COMMENT '�ƻ�����' AFTER `batch_size`,
MODIFY COLUMN `download_tips` text NOT NULL COMMENT '������ʾ��' AFTER `new_version_tips`,
MODIFY COLUMN `task_tips` text NOT NULL COMMENT '������ʾ' AFTER `disclaimer`;


-- �޸Ĳ���״̬ע��
-- ������ڣ�2021-02-04
ALTER TABLE `tb_fota_strategy` 
MODIFY COLUMN `status` smallint(2) DEFAULT 0 COMMENT '״̬:0=�½�,1=������,2=���ͨ����3=�����ܾ���4=ʧЧ' AFTER `estimate_upgrade_time`,
DROP COLUMN `is_enable`;


-- �������������״̬��
-- �������
ALTER TABLE `tb_fota_firmware_pkg` 
DROP COLUMN `build_pkg_code`;


-- ������ǩ����ӱ�ǩ����Ϣ
-- �������: 2021-02-05
ALTER TABLE `tb_fota_object_label` 
DROP COLUMN `label_group_id`,
DROP COLUMN `label_group_name`;