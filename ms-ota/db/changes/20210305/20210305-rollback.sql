use bnmotor_icv_vehicle_fota;

-- ������ڣ�2021-03-05
-- ����ˣ���С��



-- ����ˢдʱ�䳤�Ȳ����޸�
ALTER TABLE tb_fota_strategy
MODIFY COLUMN `estimate_upgrade_time` smallint(10) DEFAULT 0 COMMENT 'Ԥ������ʱ��' AFTER `rollback_mode`;