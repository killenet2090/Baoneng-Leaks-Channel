use bnmotor_icv_vehicle_fota;

-- 变更日期：2020-11-10
-- 变更说明：
-- 1、修改OTA数据库，删除一些无用字段
-- 2、固件表添加升级需要补充字段
-- 3、添加零件库和车型零件关系表
-- 变更人：许小常


use bnmotor_icv_vehicle_fota;
-- 变更日期：2020-12-23
-- 变更说明：PKI加密回滚
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_firmware_pkg DROP COLUMN `build_upload_file_id`;
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_firmware_pkg DROP COLUMN encrypt_pkg_status;
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_firmware_pkg DROP COLUMN encrypt_upload_file_id;
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_firmware_pkg MODIFY COLUMN release_pkg_sign varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '升级包签名全量升级包的签名信息，采用服务端私钥签名而成';

commit;


-- 从备份的数据中恢复数据
UPDATE tb_fota_firmware t1
LEFT JOIN  tb_fota_firmware_bak t2
ON t1.id = t2.id
set t1.tree_node_id = t2.tree_node_id

commit;
