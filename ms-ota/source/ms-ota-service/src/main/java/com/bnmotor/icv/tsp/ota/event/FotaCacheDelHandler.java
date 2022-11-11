package com.bnmotor.icv.tsp.ota.event;

import com.bnmotor.icv.tsp.ota.common.OtaMessageAttribute;
import com.bnmotor.icv.tsp.ota.common.event.OtaEventConstant;
import com.bnmotor.icv.tsp.ota.common.event.OtaEventHandler;
import com.bnmotor.icv.tsp.ota.service.IFotaObjectCacheInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@OtaMessageAttribute(topics = OtaEventConstant.OTA_CACHE_DEL_MESSAGE, msgtype = FotaCacheDelMessage.class)
public class FotaCacheDelHandler implements OtaEventHandler.EventHandler<FotaCacheDelMessage> {

	@Autowired
	IFotaObjectCacheInfoService fotaObjectCacheInfoService;

	@Override
	public FotaCacheDelMessage onMessage(FotaCacheDelMessage fotaCacheDelMessage) {
		log.info("删除redis缓存操作开始.fotaCacheDelMessage|{}", fotaCacheDelMessage);
		fotaObjectCacheInfoService.delFotaCacheInfo(fotaCacheDelMessage.getObject(), fotaCacheDelMessage.getOtaCacheTypeEnum());
		log.info("删除redis缓存操作结束");
		return fotaCacheDelMessage;
	}
}