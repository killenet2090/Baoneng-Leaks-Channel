use bnmotor_icv_vehicle_fota;

-- 变更日期：2020-12-07
-- 变更说明：
-- 1、升级任务对象表添加升级通知主动通知相关字段
-- 变更人：许小常

-- 添加升级任务对象表添加升级通知主动通知相关字段
ALTER TABLE `tb_fota_plan_obj_list` 
ADD COLUMN `notify_status` smallint(2) DEFAULT 0 COMMENT '升级通知状态：0=未通知，1=已通知，2=通知已回复' AFTER `log_file_url`,
ADD COLUMN `notify_try_num` smallint(2) DEFAULT 0 COMMENT '通知尝试次数' AFTER `notify_status`,
ADD COLUMN `nofity_time` datetime(0) COMMENT '通知发送时间' AFTER `notify_try_num`,
ADD COLUMN `notify_callback_time` datetime(0) COMMENT '通知回复时间' AFTER `nofity_time`;


-- 定时任务添加升级推送相关状态数据
ALTER TABLE `tb_fota_plan` 
ADD COLUMN `upgrade_notify_status` smallint(2) COMMENT '升级通知状态：0=未通知，1=通知已完成' AFTER `is_enable`,
ADD COLUMN `upgrade_notify_start_time` datetime(0) COMMENT '定时任务通知开始的时间' AFTER `upgrade_notify_status`,
ADD COLUMN `upgrade_notify_end_time` datetime(0) COMMENT '定时任务通知完成的时间' AFTER `upgrade_notify_start_time`;


ALTER TABLE `tb_fota_plan` 
MODIFY COLUMN `upgrade_mode` int(10) DEFAULT NULL COMMENT '升级模式：1 - 静默升级（工厂模式：下载+升级都全自动）， 2 - 提示用户（交互模式），3=静默下载 (自动下载)';

ALTER TABLE `tb_fota_firmware_pkg` 
MODIFY COLUMN `pkg_file_name` varchar(256) DEFAULT NULL COMMENT '升级包文件名,即升级包上传前自身的文件名，用于显示' ;


-- 升级任务V2版本
ALTER TABLE tb_fota_plan ADD fota_strategy_id BIGINT(16) NULL COMMENT '策略表主键id';
ALTER TABLE tb_fota_plan_firmware_list MODIFY COLUMN `group` int(11) NULL COMMENT '升级组';
ALTER TABLE tb_fota_plan_firmware_list CHANGE `group` group_seq int(11) NULL COMMENT '升级组';

ALTER TABLE tb_fota_plan ADD approve_state TINYINT DEFAULT 2 NOT NULL COMMENT '审批状态： 1免审批，2待审批、3审批中、4已审批、5未通过';
ALTER TABLE tb_fota_plan ADD publish_state TINYINT DEFAULT 1 NOT NULL COMMENT '发布状态： 1待发布 2发布中 3已发布 4已失效 5延迟发布';
ALTER TABLE tb_fota_plan ADD plan_mode TINYINT DEFAULT 0 NOT NULL COMMENT '任务模式： 0测试任务 1正式任务';

-- 增加保存升级任务附加参数
ALTER TABLE tb_fota_plan ADD extra VARCHAR(500) NULL COMMENT '存储额外信息（json串）';
-- 增加重构后任务的标识
ALTER TABLE tb_fota_plan ADD rebuild_flag TINYINT(4) NULL COMMENT '是否是重构后的任务'; 

-- 车辆零件对象表添加标识零件身份Id
ALTER TABLE tb_fota_object_component_list
ADD COLUMN `sn` varchar(128) DEFAULT NULL COMMENT '零件序列号' AFTER `ota_object_id`;

ALTER TABLE tb_fota_version_check_verify
MODIFY COLUMN `download_spend_time` int(4) DEFAULT 0 COMMENT '下载实际花费时间:单位为秒' AFTER `download_percent_rate`,
ADD COLUMN `download_remain_time` int(4) DEFAULT 0 COMMENT '下载预估剩余时间:单位为秒' AFTER `download_spend_time`;

ALTER TABLE`tb_fota_strategy_firmware_list` 
CHANGE COLUMN `start_source_id` `start_version_id` bigint(16) DEFAULT NULL COMMENT '前置版本Id' AFTER `upgrade_mode`,
CHANGE COLUMN `target_source_id` `target_version_id` bigint(16) DEFAULT NULL COMMENT '目标版本Id' AFTER `start_version_id`;


ALTER TABLE `tb_fota_version_check_verify` 
MODIFY COLUMN `installed_spend_time` int(16) DEFAULT 0 COMMENT '安装花费时间:时间为s' AFTER `installed_current_index`,
ADD COLUMN `installed_remain_time` int(16) COMMENT '安装剩余时间:时间为s' AFTER `installed_spend_time`,
ADD COLUMN `installed_firmware_id` bigint(0) COMMENT '当前正在安装的固件代码Id' AFTER `installed_remain_time`;


commit;