use bnmotor_icv_vehicle_fota;

-- 变更日期：2020-12-01
-- 变更说明：
-- 1、上传文件记录fileKey生成规则变化导致相关路径字段长度变更
-- 变更人：许小常


-- 修改tb_fota_firmware_pkg表长度
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware_pkg` 
MODIFY COLUMN `upload_file_id` bigint(16) DEFAULT NULL COMMENT '原始上传包的文件Id' AFTER `pkg_type`,
MODIFY COLUMN `original_pkg_file_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '原始包存储完整路径，包含文件名绝对路径原始包需要存储在分布式存储设备上' AFTER `encrypt_pkg_status`,
MODIFY COLUMN `original_report_file_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '测试报告存储路径' AFTER `original_pkg_size`,
MODIFY COLUMN `original_dif_script_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '差分刷写脚本url' AFTER `original_report_file_path`,
MODIFY COLUMN `release_pkg_file_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发布包存储完整路径，包含文件名绝对路径原始包需要存储在分布式存储设备上' AFTER `build_pkg_time`,
MODIFY COLUMN release_pkg_sign varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '升级包签名全量升级包的签名信息，采用服务端私钥签名而成';