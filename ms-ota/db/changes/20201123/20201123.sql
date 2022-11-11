use bnmotor_icv_vehicle_fota;

-- 变更日期：2020-11-23


-- 变更说明：PKI加密
-- 变更人：E.YanLonG
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_firmware_pkg ADD COLUMN build_upload_file_id bigint(16) NULL COMMENT '构建包完成上传包的文件Id'  AFTER upload_file_id;
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_firmware_pkg ADD encrypt_pkg_status tinyint(2) default 0 COMMENT '升级包加密状态 0未加密 1加密中 2加密成功 3加密失败';
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_firmware_pkg ADD encrypt_upload_file_id bigint(16) null COMMENT '加密后升级包文件上传id';
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_firmware_pkg MODIFY COLUMN release_pkg_sign varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '升级包签名全量升级包的签名信息，采用服务端私钥签名而成';



-- 更新升级包表记录字段长度
-- 变更人：xuxiaochang1
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_firmware_pkg 
MODIFY COLUMN original_pkg_file_path varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '原始包存储完整路径，包含文件名绝对路径原始包需要存储在分布式存储设备上' AFTER encrypt_pkg_status,
MODIFY COLUMN original_report_file_path varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '测试报告存储路径' AFTER original_pkg_size,
MODIFY COLUMN original_dif_script_url varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '差分刷写脚本url' AFTER original_report_file_path;

-- 文件上传记录字段长度修改
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_file_upload_record 
MODIFY COLUMN file_key varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'OSS存储中的key值' AFTER file_address;



-- 修改tb_fota_firmwared表中tree_node_id值为设备树level=5的父节点Id
-- 变更人：xuxiaochang1
UPDATE tb_fota_firmware t1
LEFT JOIN  tb_device_tree_node t2
ON t1.tree_node_id = t2.id
set t1.tree_node_id = t2.parent_id  where t2.tree_level = 5


-- 变更数据前先备份数据
drop table if exists tb_fota_firmware_bak;
create table tb_fota_firmware_bak like tb_fota_firmware;
insert into tb_fota_firmware_bak select * from tb_fota_firmware;


commit;
