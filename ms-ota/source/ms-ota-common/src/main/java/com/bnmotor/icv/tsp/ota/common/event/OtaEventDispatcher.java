package com.bnmotor.icv.tsp.ota.common.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: AsyncEventDispatcher.java AsyncEventDispatcher
 * @Description: 应用内的消息处理 目前多端共用一个Kafka，后面考虑使用kafka处理消息
 * 				单线程投递保证消息有序
 * @author E.YanLonG
 * @since 2020-11-16 11:54:09
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Async(OtaEventConstant.EVENT_DISPATCHER_EXECUTOR)
@Slf4j
public class OtaEventDispatcher {

	@Autowired
	OtaEventPublisher otaEventPublisher;

	@Autowired
	OtaEventHandler otaEventHandler;
	
	@EventListener
	public void processRepayEvent(OtaEvent otaEvent) {
		log.debug("dispatcher otaEvent|{}", otaEvent);
		otaEventHandler.handle(otaEvent);
	}  
	
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
	public void transactionalEventListener(OtaTransactionEvent<OtaEvent> otaTransactionEvent) {
		OtaEvent otaEvent = otaTransactionEvent.getData();
		log.debug("dispatcher otaEvent|{}", otaEvent);
		otaEventHandler.handle(otaEvent);
	}

}