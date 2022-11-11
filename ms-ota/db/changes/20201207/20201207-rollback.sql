use bnmotor_icv_vehicle_fota;

-- 变更日期：2020-12-07
-- 变更说明：
-- 1、添加任务策略表和任务策略关联表
-- 2、固件表添加升级需要补充字段
-- 3、添加零件库和车型零件关系表
-- 变更人：许小常


-- 添加OTA升级对象零件表 -->回滚
DROP TABLE bnmotor_icv_vehicle_fota.tb_fota_object_component_list;


-- 修改固件安装相关控制项数据类型 -->回滚

ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` 
MODIFY COLUMN `info_coll_ctrl_obj` varchar(100) DEFAULT NULL COMMENT '信息采集受控对象' AFTER `flash_script_url`,
MODIFY COLUMN `dl_ctrl_obj` varchar(100) DEFAULT NULL COMMENT '下载受控对象' AFTER `info_coll_ctrl_obj`,
MODIFY COLUMN `inst_tx_ctrl_obj` varchar(100) DEFAULT NULL COMMENT '安装传输受控对象' AFTER `dl_ctrl_obj`,
MODIFY COLUMN `inst_ctrl_obj` varchar(100) DEFAULT NULL COMMENT '安装受控对象' AFTER `inst_tx_ctrl_obj`;


-- 回滚升级任务表中添加的策略表和策略任务关联表
DROP TABLE bnmotor_icv_vehicle_fota.tb_fota_strategy;
DROP TABLE bnmotor_icv_vehicle_fota.tb_fota_strategy_firmware_list;
DROP TABLE bnmotor_icv_vehicle_fota.tb_device_firmware_list;
DROP TABLE bnmotor_icv_vehicle_fota.tb_fota_strategy_pre_condition;

-- 回滚升级任务表中添加的字段
ALTER TABLE `bnmotor_icv_vehicle_fota_dev`.`tb_fota_plan` DROP COLUMN `plan_mode`;
ALTER TABLE `bnmotor_icv_vehicle_fota_dev`.`tb_fota_plan` DROP COLUMN `ota_strategy_id`;

commit;