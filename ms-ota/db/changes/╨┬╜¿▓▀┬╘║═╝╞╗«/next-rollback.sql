use bnmotor_icv_vehicle_fota;

-- ������ڣ�2020-11-10
-- ���˵����
-- 1���޸�OTA���ݿ⣬ɾ��һЩ�����ֶ�
-- 2���̼������������Ҫ�����ֶ�
-- 3����������ͳ��������ϵ��
-- ����ˣ���С��

-- �ع��������������ӵĲ��Ա�Ͳ������������
DROP TABLE bnmotor_icv_vehicle_fota.tb_fota_strategy;
DROP TABLE bnmotor_icv_vehicle_fota.tb_fota_strategy_firmware_list;
DROP TABLE bnmotor_icv_vehicle_fota.tb_device_firmware_list;
DROP TABLE bnmotor_icv_vehicle_fota.tb_fota_strategy_pre_condition;

-- �ع��������������ӵ��ֶ�
ALTER TABLE `bnmotor_icv_vehicle_fota_dev`.`tb_fota_plan` DROP COLUMN `plan_mode`;
ALTER TABLE `bnmotor_icv_vehicle_fota_dev`.`tb_fota_plan` DROP COLUMN `ota_strategy_id`;


commit;

