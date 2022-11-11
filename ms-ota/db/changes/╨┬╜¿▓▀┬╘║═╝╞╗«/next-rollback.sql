use bnmotor_icv_vehicle_fota;

-- 变更日期：2020-11-10
-- 变更说明：
-- 1、修改OTA数据库，删除一些无用字段
-- 2、固件表添加升级需要补充字段
-- 3、添加零件库和车型零件关系表
-- 变更人：许小常

-- 回滚升级任务表中添加的策略表和策略任务关联表
DROP TABLE bnmotor_icv_vehicle_fota.tb_fota_strategy;
DROP TABLE bnmotor_icv_vehicle_fota.tb_fota_strategy_firmware_list;
DROP TABLE bnmotor_icv_vehicle_fota.tb_device_firmware_list;
DROP TABLE bnmotor_icv_vehicle_fota.tb_fota_strategy_pre_condition;

-- 回滚升级任务表中添加的字段
ALTER TABLE `bnmotor_icv_vehicle_fota_dev`.`tb_fota_plan` DROP COLUMN `plan_mode`;
ALTER TABLE `bnmotor_icv_vehicle_fota_dev`.`tb_fota_plan` DROP COLUMN `ota_strategy_id`;


commit;

