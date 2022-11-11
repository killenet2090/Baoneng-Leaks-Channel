use bnmotor_icv_vehicle_fota;

-- 变更日期：2021-03-31
-- 变更人：E.YanLonG



-- 删除表tb_fota_approval_record
drop table `bnmotor_icv_vehicle_fota`.`tb_fota_approval_record`;


-- 回滚策略表tb_fota_strategy
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_strategy drop column file_record_id;
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_strategy drop column remark;



-- 文件上传表修改fileKey长度
ALTER TABLE tb_fota_file_upload_record
MODIFY COLUMN `file_key` varchar(128) DEFAULT NULL COMMENT 'OSS存储中的key值';