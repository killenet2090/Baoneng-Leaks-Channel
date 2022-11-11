package com.bnmotor.icv.tsp.ota.event;

import com.bnmotor.icv.tsp.ota.common.OtaMessageAttribute;
import com.bnmotor.icv.tsp.ota.common.enums.Enums;
import com.bnmotor.icv.tsp.ota.common.event.OtaEventConstant;
import com.bnmotor.icv.tsp.ota.common.event.OtaEventHandler;
import com.bnmotor.icv.tsp.ota.handler.tbox.SendMsg4OtherHandler;
import com.bnmotor.icv.tsp.ota.service.IPush4AppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * @ClassName: TransactionExecutorHandler
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/10/20 10:15
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@OtaMessageAttribute(topics = OtaEventConstant.OTA_UPGRADE_MESSAGE, msgtype = OtaUpgradeMessageBase.class, route = true)
@Slf4j
public class TransactionExecutorHandler implements OtaEventHandler.EventHandler<OtaUpgradeMessageBase> {

	@Autowired
	private IPush4AppService push4AppService;

	@Autowired
	SendMsg4OtherHandler sendMsg4OtherHandler;

	@Override
	public OtaUpgradeMessageBase onMessage(OtaUpgradeMessageBase otaUpgradeMessageBase) {
		log.info("otaUpgradeMessageBase|{}", otaUpgradeMessageBase);
		//直接通过极光通信推送到APP
		if(Objects.nonNull(otaUpgradeMessageBase.getOtaUpgradeMessageType())) {
			if (otaUpgradeMessageBase.getOtaUpgradeMessageType().intValue() == Enums.OtaUpgradeMessageTypeEnum.NEW_VERSION.getType()) {
				OtaUpgradeNewVersionMessage otaUpgradeNewVersionMessage = (OtaUpgradeNewVersionMessage) otaUpgradeMessageBase.getData();
				push4AppService.pushNewVersionCheck2App(otaUpgradeNewVersionMessage);
			} else if (otaUpgradeMessageBase.getOtaUpgradeMessageType().intValue() == Enums.OtaUpgradeMessageTypeEnum.OTHER.getType()) {
				OtaUpgradeOtherMessage otaUpgradeOtherMessage = (OtaUpgradeOtherMessage) otaUpgradeMessageBase.getData();
				push4AppService.push2App(otaUpgradeOtherMessage.getT(), otaUpgradeMessageBase.getVin(), otaUpgradeOtherMessage.getReqId());
			}
		}
		//是否需要推送到消息中心
		if(Objects.nonNull(otaUpgradeMessageBase.getMessageCenterMsgTypeEnum())){
			push4AppService.push2MessageCenter(otaUpgradeMessageBase);
		}
		//是否需要推送给其他的kafka topic
		if(Objects.nonNull(otaUpgradeMessageBase.getOtaDispatch4OtherMessage()) && !StringUtils.isEmpty(otaUpgradeMessageBase.getOtaDispatch4OtherMessage().getJsonPayload())){
			sendMsg4OtherHandler.sendMsg(otaUpgradeMessageBase.getOtaDispatch4OtherMessage());
		}
		return otaUpgradeMessageBase;
	}
}