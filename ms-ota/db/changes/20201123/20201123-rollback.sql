use bnmotor_icv_vehicle_fota;

-- ������ڣ�2020-11-10
-- ���˵����
-- 1���޸�OTA���ݿ⣬ɾ��һЩ�����ֶ�
-- 2���̼������������Ҫ�����ֶ�
-- 3����������ͳ��������ϵ��
-- ����ˣ���С��


use bnmotor_icv_vehicle_fota;
-- ������ڣ�2020-12-23
-- ���˵����PKI���ܻع�
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_firmware_pkg DROP COLUMN `build_upload_file_id`;
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_firmware_pkg DROP COLUMN encrypt_pkg_status;
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_firmware_pkg DROP COLUMN encrypt_upload_file_id;
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_firmware_pkg MODIFY COLUMN release_pkg_sign varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '������ǩ��ȫ����������ǩ����Ϣ�����÷����˽Կǩ������';

commit;


-- �ӱ��ݵ������лָ�����
UPDATE tb_fota_firmware t1
LEFT JOIN  tb_fota_firmware_bak t2
ON t1.id = t2.id
set t1.tree_node_id = t2.tree_node_id

commit;
