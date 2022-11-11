DROP DATABASE IF EXISTS tsp_common_oss;
CREATE DATABASE tsp_common_oss
CHARACTER SET utf8mb4      
COLLATE utf8mb4_general_ci;  


CREATE TABLE `file_upload_his` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '上传用户id',
  `file_size` bigint(20) DEFAULT NULL COMMENT '文件大小 （单位 byte）',
  `origin_name` varchar(500) DEFAULT NULL COMMENT '源文件名称',
  `file_name` varchar(255) DEFAULT NULL COMMENT '文件名称',
  `file_source` tinyint(1) DEFAULT NULL COMMENT '文件来源 1： 本地文件  2：网络文件',
  `bucket` varchar(255) DEFAULT NULL COMMENT '文件所属桶',
  `relative_path` varchar(255) DEFAULT NULL COMMENT '文件相对路径（返回前端的路径）',
  `file_suffix` varchar(10) DEFAULT NULL COMMENT '文件后缀名',
  `file_group` varchar(30) DEFAULT NULL COMMENT '文件所属组',
  `upload_time` datetime DEFAULT NULL COMMENT '上传时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='上传文件历史记录表';