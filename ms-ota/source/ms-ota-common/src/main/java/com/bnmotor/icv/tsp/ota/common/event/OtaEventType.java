package com.bnmotor.icv.tsp.ota.common.event;

/**
 * @ClassName: OtaEventType.java OtaEventType
 * @Description: 
 * @author E.YanLonG
 * @since 2020-11-16 14:18:47
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum OtaEventType {
	
	UPGRADE_PACKAGE_BUILD_COMPLETE(OtaEventConstant.UPGRADE_PACKAGE_BUILD_COMPLETE, "升级包制作完成"), //

	UPGRADE_PACKAGE_ENCRYPT_COMPLETE(OtaEventConstant.UPGRADE_PACKAGE_ENCRYPT_COMPLETE, "升级包加密完成"),

	/**
	 * ota更新消息类型
	 */
	OTA_UPGRADE_MESSAGE(OtaEventConstant.OTA_UPGRADE_MESSAGE, "版本检查更新"),

	/**
	 * 固件更新
	 */
	OTA_FOTA_DEVICE_SYNC_MESSAGE(OtaEventConstant.OTA_DEVICE_SYNC_MESSAGE, "固件更新"),
	
	/**
	 * 任务开关切换
	 */
	UPGRADE_PLAN_STATE_SWITCH(OtaEventConstant.UPGRADE_PLAN_STATE_SWITCH, "任务开关切换"),

	/**
	 * redis缓存清除消息
	 */
	OTA_CACHE_DEL_MESSAGE(OtaEventConstant.OTA_CACHE_DEL_MESSAGE, "redis缓存清除消息"),

	/**
	 * 来自TBOX的版本请求消息
	 */
	OTA_VERSION_CHECK_FROM_TBOX_MESSAGE(OtaEventConstant.OTA_VERSION_CHECK_FROM_TBOX_MESSAGE, "来自TBOX的版本请求消息"),
	;

	String type;
	String desc;

	OtaEventType(String type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public OtaEvent create(Object data) {
		OtaEvent otaEvent = new OtaEvent(this, data);
		return otaEvent;
	}

	public boolean requals0(OtaEventType that) {
		return this.type.equals(that.type);
	}
}
