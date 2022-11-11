package com.bnmotor.icv.tsp.ota.common.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OtaEventKit {

	@Autowired
	OtaEventPublisher otaEventPublisher;

	public OtaEvent buildOtaEvent(OtaEventType otaEventType, Object source) {
		return new OtaEvent(otaEventType, source);
	}
	
	public  <T> OtaTransactionEvent<T> buildOtaTransactionEvent(T data) {
		OtaTransactionEvent<T> otaTransactionEvent = new OtaTransactionEvent<>(data, data);
		return otaTransactionEvent;
	}
	
	public void publishOtaEvent(OtaEvent otaEvent) {
		log.info("发起otaEvent|{}", otaEvent);
		otaEventPublisher.pushOtaEvent(otaEvent);
	}
	
	public void publishOtaTransactionEvent(OtaTransactionEvent<OtaEvent> otaTransactionEvent) {
		log.info("发起otaTransactionEvent|{}", otaTransactionEvent);
		otaEventPublisher.pushEvent(otaTransactionEvent);
	}

	/**
	 *
	 * @param source
	 * @param <T>
	 */
	public <T> void publishOtaTransactionEvent(OtaEventType otaEventType, T source) {
		log.info("发起otaTransactionEvent|{}", source);
		OtaEvent otaEvent = buildOtaEvent(otaEventType, source);
		otaEventPublisher.pushEvent(buildOtaTransactionEvent(otaEvent));
	}
}