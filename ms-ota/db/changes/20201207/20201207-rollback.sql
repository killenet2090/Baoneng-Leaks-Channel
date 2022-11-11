use bnmotor_icv_vehicle_fota;

-- ������ڣ�2020-12-07
-- ���˵����
-- 1�����������Ա��������Թ�����
-- 2���̼������������Ҫ�����ֶ�
-- 3����������ͳ��������ϵ��
-- ����ˣ���С��


-- ���OTA������������� -->�ع�
DROP TABLE bnmotor_icv_vehicle_fota.tb_fota_object_component_list;


-- �޸Ĺ̼���װ��ؿ������������� -->�ع�

ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` 
MODIFY COLUMN `info_coll_ctrl_obj` varchar(100) DEFAULT NULL COMMENT '��Ϣ�ɼ��ܿض���' AFTER `flash_script_url`,
MODIFY COLUMN `dl_ctrl_obj` varchar(100) DEFAULT NULL COMMENT '�����ܿض���' AFTER `info_coll_ctrl_obj`,
MODIFY COLUMN `inst_tx_ctrl_obj` varchar(100) DEFAULT NULL COMMENT '��װ�����ܿض���' AFTER `dl_ctrl_obj`,
MODIFY COLUMN `inst_ctrl_obj` varchar(100) DEFAULT NULL COMMENT '��װ�ܿض���' AFTER `inst_tx_ctrl_obj`;


-- �ع��������������ӵĲ��Ա�Ͳ������������
DROP TABLE bnmotor_icv_vehicle_fota.tb_fota_strategy;
DROP TABLE bnmotor_icv_vehicle_fota.tb_fota_strategy_firmware_list;
DROP TABLE bnmotor_icv_vehicle_fota.tb_device_firmware_list;
DROP TABLE bnmotor_icv_vehicle_fota.tb_fota_strategy_pre_condition;

-- �ع��������������ӵ��ֶ�
ALTER TABLE `bnmotor_icv_vehicle_fota_dev`.`tb_fota_plan` DROP COLUMN `plan_mode`;
ALTER TABLE `bnmotor_icv_vehicle_fota_dev`.`tb_fota_plan` DROP COLUMN `ota_strategy_id`;

commit;