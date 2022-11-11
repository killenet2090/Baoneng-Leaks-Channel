package com.bnmotor.icv.tsp.ota.event;

import com.bnmotor.icv.tsp.ota.common.OtaMessageAttribute;
import com.bnmotor.icv.tsp.ota.common.event.OtaEventConstant;
import com.bnmotor.icv.tsp.ota.common.event.OtaEventHandler;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanObjListPo;
import com.bnmotor.icv.tsp.ota.service.IFotaPlanObjListDbService;
import com.bnmotor.icv.tsp.ota.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @ClassName: FotaCheckVersionFromTboxHandler
 * @Description: 来自TBox的版本检查消息处理
 * @author: xuxiaochang1
 * @date: 2020/10/20 10:15
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@OtaMessageAttribute(topics = OtaEventConstant.OTA_VERSION_CHECK_FROM_TBOX_MESSAGE, msgtype = FotaCheckVersionFromTboxMessage.class, route = true)
@Slf4j
public class FotaCheckVersionFromTboxHandler implements OtaEventHandler.EventHandler<FotaCheckVersionFromTboxMessage> {

	@Autowired
	IFotaPlanObjListDbService fotaPlanObjListDbService;

	@Override
	public FotaCheckVersionFromTboxMessage onMessage(FotaCheckVersionFromTboxMessage fotaCheckVersionFromTboxMessage) {
		log.info("fotaCheckVersionFromTboxMessage|{}", fotaCheckVersionFromTboxMessage);
		FotaPlanObjListPo fotaPlanObjListPo = new FotaPlanObjListPo();
		fotaPlanObjListPo.setId(fotaCheckVersionFromTboxMessage.getOtaPlanObjId());
		fotaPlanObjListPo.setNotifyStatus(2);
		fotaPlanObjListPo.setNotifyCallbackTime(LocalDateTime.now());
		CommonUtil.wrapBasePo4Update(fotaPlanObjListPo);
		fotaPlanObjListDbService.updateById(fotaPlanObjListPo);
		return fotaCheckVersionFromTboxMessage;
	}
}