package com.bnmotor.icv.tsp.ota.common;

public class CommonConstant {

    public static String SUCCESS_CODE = "200";

    public static String FAIL_CODE = "500";

    public static String SUCCESS_MESSAGE = "success";

    public static String FAIL_MESSAGE = "fail";

    public static String START_CODE = "2";

    public static final String SEPARATOR_SEMICOLON = ";";
    /**
     * 升级包类型delta
     */
    public static final String TYPE_DELTA = "delta";

    /**
     * 升级包类型full
     */
    public static final String TYPE_FULL = "full";
    /**
     * MINIO中桶的存储名称
     */
    public final static String BUCKET_OTA_NAME = "tsp-ota";

    /**
     * MINIO中桶的存储升级包的群组
     */
    public final static String BUCKET_OTA_GROUP_PKG = "pkg";

    /**
     * MINIO中桶的存储测试报告的群组
     */
    public final static String BUCKET_OTA_GROUP_REPORT = "report";

    /**
     * MINIO中桶的存储脚本的群组
     */
    public final static String BUCKET_OTA_GROUP_SCRIPT = "script";

    /**
     * MINIO中桶的存储脚本的群组
     */
    public final static String BUCKET_OTA_GROUP_UPGRADELOG = "upgradeLog";

    /**
     * 待分片
     */
    public final static Integer OTA_SLICE_UPLOAD_WAIT_SLICE = -1;

    /**
     * 上传成功
     */
    public final static Integer OTA_SLICE_UPLOAD_SUCCESS = 0;

    /**
     * 部分上传
     */
    public final static Integer OTA_SLICE_UPLOAD_PART = 1;

    /**
     * 上传失败/文件合失败
     */
    public final static Integer OTA_SLICE_UPLOAD_FAIL = 2;

    /**
     * 文件合并中
     */
    public final static Integer OTA_SLICE_UPLOAD_COMBINNATING = 3;

    /**
     * 文件待合并
     */
    public final static Integer OTA_SLICE_UPLOAD_COMBINNATE_WAIT = 4;

    /**
     * 上传失败/文件合失败(SHA256码值异常)
     */
    public final static Integer OTA_SLICE_UPLOAD_FAIL_SHA256 = 4;

    /**
     * 默认的projectId
     */
    public final static String PROJECT_ID_DEFAULT = "bngrp";

	/**
	 * MINIO中桶的存储升级日志的群组
	 */
	public final static String BUCKET_OTA_GROUP_UPGRADE_LOG = "upgradeLog";

    /**
     * OTA缓存key
     */
    public final static String TSP_USER_KEY_PREFIX = "system:user:info:";


    /**
     * 存储到mongodb中的版本检测请求数据
     */
    //public final static String MONGODB_COLLECTION_VERSION_CHECK_REQ = "ota_version_check_req";

    /**
     * 存储到mongodb中的版本检测请求数据
     */
    //public final static String MONGODB_COLLECTION_VERSION_CHECK_RESP = "ota_version_check_resp";

    /**
     * 存储到mongodb中的安装汇报过程数据
     */
    //public final static String MONGODB_COLLECTION_UPGRADE_PROCESS = "ota_upgrade_process";

    /**
     * 存储到mongodb中的安装汇报过程结果数据
     */
    //public final static String MONGODB_COLLECTION_UPGRADE_RESULT = "ota_upgrade_result";

    /**
     * 存储到mongodb中的下载汇报过程数据
     */
    //public final static String MONGODB_COLLECTION_DOWNLOAD_PROCESS = "ota_download_process";

    /**
     * 系统用户
     */
    public final static String USER_ID_SYSTEM = "sys";

    public final static String TASK_ID = "taskId";

    public final static String TASK_NAME = "taskName";


    public final static String TASK_INFO = "taskInfo";

    public final static String TOTAL="total";

	public final static String DATA="data";

    public final static int NOT_UPGRADE_STATUS_RANGE_START = -2;

    public final static int NOT_UPGRADE_STATUS_RANGE_END = 10;

    public final static int UPGRADING_STATUS_RANGE_START = 11;

    public final static int UPGRADING_STATUS_RANGE_END = 29;

	//public final static String PUBLIC_MINI_URL_PREFIX = "http://minio-dev.dev.int.bnicvc.com:8001";

    public final static String PUBLIC_HTTP_URL_PREFIX = "http://";

    public static String X_ACCESS_TOKEN = "X-Access-Token";

    public static String FOTA_OBJECT_INIT_VERSION = "VB OS .000";

    public static Double FOTA_FIRMWARE_VERSION_PKG_ESTIMATE_UPGRADE_TIME_SCALE = 1.2D;
}