package com.bnmotor.icv.tsp.ota.common.event;

import com.bnmotor.icv.tsp.ota.common.enums.OtaCacheTypeEnum;
import com.bnmotor.icv.tsp.ota.event.FotaCacheDelMessage;
import com.bnmotor.icv.tsp.ota.event.FotaCheckVersionFromTboxMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bnmotor.icv.tsp.ota.event.FotaUpgradePlanSwitch;
import com.bnmotor.icv.tsp.ota.event.PackageBuildCompleted;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: MyOtaEventKit.java 
 * @Description: 业务消息
 * @author E.YanLonG
 * @since 2021-1-15 9:02:06
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Slf4j
public class MyOtaEventKit {

	@Autowired
	OtaEventKit otaEventKit;

	public void triggerPackageBuildCompletedEvent(Long firmwarePkgId) {
		PackageBuildCompleted packageBuildCompleted = buildPackageBuildCompleted(firmwarePkgId);
		OtaEvent otaEvent = otaEventKit.buildOtaEvent(OtaEventType.UPGRADE_PACKAGE_BUILD_COMPLETE, packageBuildCompleted);
		OtaTransactionEvent<OtaEvent> otaTransactionEvent = otaEventKit.buildOtaTransactionEvent(otaEvent);
		otaEventKit.publishOtaTransactionEvent(otaTransactionEvent);
	}
	
	private PackageBuildCompleted buildPackageBuildCompleted(Long firmwarePkgId) {
		PackageBuildCompleted packageBuildCompleted = PackageBuildCompleted.of().setFirmwarePackageId(firmwarePkgId); //
		return packageBuildCompleted;
	}
	
	public void triggerFotaUpgradePlanSwitchEvent(Long fotaPlanId, Integer isEnable) {
		FotaUpgradePlanSwitch fotaUpgradePlanSwitch = buildFotaUpgradePlanSwitch(fotaPlanId, isEnable);
		OtaEvent otaEvent = otaEventKit.buildOtaEvent(OtaEventType.UPGRADE_PLAN_STATE_SWITCH, fotaUpgradePlanSwitch);
		OtaTransactionEvent<OtaEvent> otaTransactionEvent = otaEventKit.buildOtaTransactionEvent(otaEvent);
		otaEventKit.publishOtaTransactionEvent(otaTransactionEvent);
	}
	
	private FotaUpgradePlanSwitch buildFotaUpgradePlanSwitch(Long fotaPlanId, Integer isEnable) {
		FotaUpgradePlanSwitch fotaUpgradePlanSwitch = FotaUpgradePlanSwitch.of();
		fotaUpgradePlanSwitch.setFotaPlanId(fotaPlanId).setIsEnable(isEnable);
		return fotaUpgradePlanSwitch;
	}

	/**
	 * 发送缓存删除消息类
	 * @param key
	 * @param otaCacheTypeEnum
	 */
	public void triggerFotaCacheDelEvent(Object key, OtaCacheTypeEnum otaCacheTypeEnum) {
		FotaCacheDelMessage fotaCacheDelMessage = new FotaCacheDelMessage(key, otaCacheTypeEnum);
		triggerEvent(OtaEventType.OTA_CACHE_DEL_MESSAGE, fotaCacheDelMessage);
	}

	/**
	 * 发送来自TBOX的版本请求
	 * @param vin
	 * @param otaPlanObjId
	 */
	public void triggerFotaCheckVersionFromTboxEvent(String vin, Long otaPlanObjId) {
		FotaCheckVersionFromTboxMessage fotaCheckVersionFromTboxMessage = new FotaCheckVersionFromTboxMessage();
		fotaCheckVersionFromTboxMessage.setVin(vin);
		fotaCheckVersionFromTboxMessage.setOtaPlanObjId(otaPlanObjId);
		triggerEvent(OtaEventType.OTA_VERSION_CHECK_FROM_TBOX_MESSAGE, fotaCheckVersionFromTboxMessage);
	}

	/**
	 * 发送异步处理通知
	 * @param otaEventType
	 * @param t
	 * @param <T>
	 */
	public <T> void triggerEvent(OtaEventType otaEventType, T t) {
		OtaEvent otaEvent = otaEventKit.buildOtaEvent(otaEventType, t);
		OtaTransactionEvent<OtaEvent> otaTransactionEvent = otaEventKit.buildOtaTransactionEvent(otaEvent);
		otaEventKit.publishOtaTransactionEvent(otaTransactionEvent);
	}
}