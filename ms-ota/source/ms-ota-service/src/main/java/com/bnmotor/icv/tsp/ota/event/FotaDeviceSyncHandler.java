package com.bnmotor.icv.tsp.ota.event;

import com.bnmotor.icv.tsp.ota.common.OtaMessageAttribute;
import com.bnmotor.icv.tsp.ota.common.event.OtaEventConstant;
import com.bnmotor.icv.tsp.ota.common.event.OtaEventHandler;
import com.bnmotor.icv.tsp.ota.service.IFotaDeviceSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @ClassName: FotaDeviceSyncHandler
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/10/20 10:15
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@Slf4j
@OtaMessageAttribute(topics = OtaEventConstant.OTA_DEVICE_SYNC_MESSAGE, msgtype = FotaDeviceSyncMessage.class)
public class FotaDeviceSyncHandler implements OtaEventHandler.EventHandler<FotaDeviceSyncMessage> {

	@Autowired
	@Qualifier("fotaDeviceSyncServiceV2")
	IFotaDeviceSyncService fotaDeviceSyncService;

	@Override
	public FotaDeviceSyncMessage onMessage(FotaDeviceSyncMessage fotaDeviceSyncMessage) {
		log.info("固件更新维护车辆固件列表结束开始.fotaDeviceSyncMessage|{}", fotaDeviceSyncMessage);
		FotaDeviceSyncResult fotaDeviceSyncResult = fotaDeviceSyncService.process4Firmware(fotaDeviceSyncMessage);
		log.info("固件更新维护车辆固件列表结束:fotaDeviceSyncResult={}", fotaDeviceSyncResult);
		return fotaDeviceSyncMessage;
	}
}