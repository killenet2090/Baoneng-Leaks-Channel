use bnmotor_icv_vehicle_fota;

-- 变更日期：2021-03-05
-- 变更人：许小常



-- 修改安装过程状态说明
ALTER TABLE tb_fota_version_check_verify
MODIFY COLUMN `installed_complete_status` smallint(10) DEFAULT NULL COMMENT '安装结果：1=升级完成（成功），2=升级未完成，3=升级失败,4=升级终止（中断）';