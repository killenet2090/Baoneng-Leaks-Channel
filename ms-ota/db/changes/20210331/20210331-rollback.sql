use bnmotor_icv_vehicle_fota;

-- ������ڣ�2021-03-31
-- ����ˣ�E.YanLonG



-- ɾ����tb_fota_approval_record
drop table `bnmotor_icv_vehicle_fota`.`tb_fota_approval_record`;


-- �ع����Ա�tb_fota_strategy
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_strategy drop column file_record_id;
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_strategy drop column remark;



-- �ļ��ϴ����޸�fileKey����
ALTER TABLE tb_fota_file_upload_record
MODIFY COLUMN `file_key` varchar(128) DEFAULT NULL COMMENT 'OSS�洢�е�keyֵ';