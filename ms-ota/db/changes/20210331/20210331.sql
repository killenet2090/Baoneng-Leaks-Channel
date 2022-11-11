use bnmotor_icv_vehicle_fota;

-- 变更日期：2021-03-31
-- 变更人：E.YanLonG

-- 创建审批流程记录表`tb_fota_approval_record`
CREATE TABLE `tb_fota_approval_record` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `business_key` varchar(50) DEFAULT NULL COMMENT '审批业务唯一编码 OTA_1234567890123',
  `form_title` varchar(50) NOT NULL default '' COMMENT '表单显示名称',
  `approval_type` tinyint(4) NOT NULL COMMENT '审批类型 1策略审批 2任务审批',
  `definition_key` varchar(50) not null default '' comment '流程定义key',
  `process_instance_id` varchar(100) NOT NULL default '' COMMENT '响应Id',
  `ota_object_key` bigint(16) DEFAULT NULL COMMENT '策略或者任务的主键',
  `ota_object_body` TEXT DEFAULT NULL COMMENT '策略或者任务的json串',
  `vars` varchar(500) DEFAULT NULL COMMENT '页面链接地址',
  `status` smallint(2) DEFAULT '0' COMMENT '审批状态:1免审批 2待审批 3审批中 4已审批 5拒绝 6驳回 7撤回',
  `description` varchar(500) DEFAULT NULL COMMENT '描述说明',
  `version` int(11) DEFAULT '0' COMMENT '数据版本，用于更新并发控制使用',
  `del_flag` smallint(6) DEFAULT '0' COMMENT '删除标志:0-正常1-删除',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='OTA审批流信息';



-- 策略表增加审批报告的文件上传id
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_strategy ADD file_record_id BIGINT(16) NULL COMMENT '上传的审批报告文件id' AFTER estimate_upgrade_time;
ALTER TABLE bnmotor_icv_vehicle_fota.tb_fota_strategy ADD remark varchar(500) NULL COMMENT '策略审核备注' AFTER file_record_id;


-- 文件上传表修改fileKey长度
ALTER TABLE tb_fota_file_upload_record
MODIFY COLUMN `file_key` varchar(256) DEFAULT NULL COMMENT 'OSS存储中的key值';