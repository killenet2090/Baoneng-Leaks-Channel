use bnmotor_icv_vehicle_fota;

-- ������ڣ�2020-12-01
-- ���˵����
-- 1���ϴ��ļ���¼fileKey���ɹ���仯�������·���ֶγ��ȱ��
-- ����ˣ���С��


-- �޸�tb_fota_firmware_pkg����
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware_pkg` 
MODIFY COLUMN `upload_file_id` bigint(16) DEFAULT NULL COMMENT 'ԭʼ�ϴ������ļ�Id' AFTER `pkg_type`,
MODIFY COLUMN `original_pkg_file_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'ԭʼ���洢����·���������ļ�������·��ԭʼ����Ҫ�洢�ڷֲ�ʽ�洢�豸��' AFTER `encrypt_pkg_status`,
MODIFY COLUMN `original_report_file_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '���Ա���洢·��' AFTER `original_pkg_size`,
MODIFY COLUMN `original_dif_script_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '���ˢд�ű�url' AFTER `original_report_file_path`,
MODIFY COLUMN `release_pkg_file_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '�������洢����·���������ļ�������·��ԭʼ����Ҫ�洢�ڷֲ�ʽ�洢�豸��' AFTER `build_pkg_time`,
MODIFY COLUMN release_pkg_sign varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '������ǩ��ȫ����������ǩ����Ϣ�����÷����˽Կǩ������';