

ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_upgrade_task_condition` add column `version` int(4) DEFAULT '0' COMMENT '数据并发控制版本';
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_upgrade_task_condition` add column `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志：0 - 正常，1 - 删除';
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_upgrade_task_condition` add column  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人';
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_upgrade_task_condition` add column `create_time` datetime DEFAULT NULL COMMENT '创建时间';
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_upgrade_task_condition` add column `update_by` varchar(50) DEFAULT NULL COMMENT '更新人';
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_upgrade_task_condition` add column `update_time` datetime DEFAULT NULL COMMENT '更新时间';



ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` ADD COLUMN `info_coll_ctrl_obj` varchar(100) DEFAULT NULL COMMENT '信息采集受控对象' after `flash_script_url`;
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` ADD COLUMN `dl_ctrl_obj` varchar(100) DEFAULT NULL COMMENT '下载受控对象' after `info_coll_ctrl_obj`;
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` ADD COLUMN `inst_tx_ctrl_obj` varchar(100) DEFAULT NULL COMMENT '安装传输受控对象' after `dl_ctrl_obj`;
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` ADD COLUMN `inst_ctrl_obj` varchar(100) DEFAULT NULL COMMENT '安装受控对象' after `inst_tx_ctrl_obj`;
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` ADD COLUMN `inst_condition` tinyint(2) DEFAULT '0' COMMENT '安装约束（零件需要处于低压或高压条件下才可以安装）：0 低压 1 高压 默认：0 低压' after `inst_ctrl_obj`;
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` ADD COLUMN `bus_type` tinyint(2) DEFAULT NULL COMMENT '总线类型 1 can节点 2 以太网节点' after `inst_condition`;
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` ADD COLUMN `info_coll_script_type` tinyint(2) DEFAULT NULL COMMENT '信息采集脚本类型' after `bus_type`;
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` ADD COLUMN `inst_algorithm_type` tinyint(2) DEFAULT NULL COMMENT '安装算法类型(SA)算法类型' after `info_coll_script_type`;
ALTER TABLE `bnmotor_icv_vehicle_fota`.`tb_fota_firmware` ADD COLUMN `inst_script_url` varchar(100) DEFAULT NULL COMMENT '安装脚本地址' after `inst_algorithm_type`;



ALTER TABLE `bnmotor_icv_vehicle_fota_dev`.`tb_upgrade_task_condition` 
DROP COLUMN `task_id`;


ALTER TABLE `bnmotor_icv_vehicle_fota_dev`.`tb_upgrade_strategy` 
DROP COLUMN `task_id`;


ALTER TABLE `bnmotor_icv_vehicle_fota_dev`.`tb_task_terminate` 
DROP COLUMN `task_id`;

ALTER TABLE `bnmotor_icv_vehicle_fota_dev`.`tb_fota_plan_obj_list` 
DROP COLUMN `object_id`,
DROP COLUMN `task_id`;


ALTER TABLE `bnmotor_icv_vehicle_fota_dev`.`tb_fota_plan_firmware_list` 
DROP COLUMN `task_id`;