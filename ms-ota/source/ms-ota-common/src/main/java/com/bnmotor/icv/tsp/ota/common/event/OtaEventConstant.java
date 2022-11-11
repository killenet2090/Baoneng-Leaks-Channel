package com.bnmotor.icv.tsp.ota.common.event;

/**
 * @ClassName: OtaEventConstant.java OtaEventConstant
 * @Description: 事件异步执行
 * @author E.YanLonG
 * @since 2020-11-16 12:02:22
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class OtaEventConstant {

	public static final String EVENT_DISPATCHER_EXECUTOR = "EventDispatcherExecutor";
	
	public static final String TASK_COMMON_EXECUTOR = "OtaCommonEventExecutor";
	
	public static final String TASK_TRANSACTION_EXECUTOR = "OtaTransactionEventExecutor";

	// 升级包制作完成
	public static final String UPGRADE_PACKAGE_BUILD_COMPLETE = "UPGRADE_PACKAGE_BUILD_COMPLETE";

	// 升级包加密完成
	public static final String UPGRADE_PACKAGE_ENCRYPT_COMPLETE = "UPGRADE_PACKAGE_ENCRYPT_COMPLETE";

	// ota升级消息变更
	public static final String OTA_UPGRADE_MESSAGE = "OTA_UPGRADE_MESSAGE";

	// ota固件变更
	public static final String OTA_DEVICE_SYNC_MESSAGE = "OTA_DEVICE_SYNC_MESSAGE";
	
	// 任务开关切换
	public static final String UPGRADE_PLAN_STATE_SWITCH = "UPGRADE_PLAN_STATE_SWITCH";

	// 更新缓存消息
	public static final String OTA_CACHE_DEL_MESSAGE = "OTA_CACHE_DEL_MESSAGE";

	// TBOX版本请求消息
	public static final String OTA_VERSION_CHECK_FROM_TBOX_MESSAGE = "OTA_VERSION_CHECK_FROM_TBOX_MESSAGE";

}