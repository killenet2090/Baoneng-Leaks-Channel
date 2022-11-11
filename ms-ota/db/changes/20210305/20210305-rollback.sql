use bnmotor_icv_vehicle_fota;

-- 变更日期：2021-03-05
-- 变更人：许小常



-- 策略刷写时间长度不够修改
ALTER TABLE tb_fota_strategy
MODIFY COLUMN `estimate_upgrade_time` smallint(10) DEFAULT 0 COMMENT '预估升级时长' AFTER `rollback_mode`;