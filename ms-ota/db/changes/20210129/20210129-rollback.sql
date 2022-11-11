use bnmotor_icv_vehicle_fota;

-- 变更日期：2021-01-29
-- 变更人：许小常



-- 文件上传记录中添加是否通过分块上传完成
ALTER TABLE `tb_fota_file_upload_record` 
DROP COLUMN `upload_slice`;


-- 文件升级包添加测试报告文件Id
ALTER TABLE `tb_fota_firmware_pkg` 
DROP COLUMN `report_upload_file_id`;


-- 移除非空字段设置，bnlink_1.3版本中以下字段已经废弃
-- 变更日期：2021-02-03
ALTER TABLE `tb_fota_plan` 
MODIFY COLUMN `plan_desc` varchar(200) NOT NULL DEFAULT '' COMMENT '计划描述' AFTER `batch_size`,
MODIFY COLUMN `download_tips` text NOT NULL COMMENT '下载提示语' AFTER `new_version_tips`,
MODIFY COLUMN `task_tips` text NOT NULL COMMENT '任务提示' AFTER `disclaimer`;


-- 修改策略状态注释
-- 变更日期：2021-02-04
ALTER TABLE `tb_fota_strategy` 
MODIFY COLUMN `status` smallint(2) DEFAULT 0 COMMENT '状态:0=新建,1=审批中,2=审核通过，3=审批拒绝，4=失效' AFTER `estimate_upgrade_time`,
DROP COLUMN `is_enable`;


-- 添加升级包制作状态码
-- 变更日期
ALTER TABLE `tb_fota_firmware_pkg` 
DROP COLUMN `build_pkg_code`;


-- 车辆标签表添加标签组信息
-- 变更日期: 2021-02-05
ALTER TABLE `tb_fota_object_label` 
DROP COLUMN `label_group_id`,
DROP COLUMN `label_group_name`;