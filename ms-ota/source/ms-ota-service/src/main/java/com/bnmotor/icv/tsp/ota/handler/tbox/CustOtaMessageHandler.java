package com.bnmotor.icv.tsp.ota.handler.tbox;

import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Service;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.kafka.domain.KafkaMessage;
import com.bnmotor.icv.adam.kafka.handler.IMessageHandler;
import com.bnmotor.icv.tsp.ota.common.OtaMessageAttribute;
import com.bnmotor.icv.tsp.ota.common.event.OtaEventMonitor;
import com.google.common.collect.Maps;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: CustOtaMessageHandler
 * @Description: ota响应处理接口类处理接口
 * @author xuxiaochang1@bngrp.com
 * @version 1.0.0
 * @date 2020-10-21
 * @copyright 2020 www.baoneng.com Inc. All rights reserved.
 *            注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service("custOtaMessageHandler")
@Slf4j
public class CustOtaMessageHandler implements IMessageHandler {

	final Map<String, TriplePair> otaMessageHandlerClass = Maps.newHashMap();

	@PostConstruct
	public void init() {
		log.info("CustOtaMessageHandler init");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void onMessage(KafkaMessage kafkaMessage) {
		String topic = kafkaMessage.getTopic();
		TriplePair triplePair = selectHandler(topic);
		
		String sgin = OtaEventMonitor.init();
		String threadname0 = threadName(sgin);
		try {
			if (Objects.nonNull(triplePair)) {
				KafkaHandlerManager.KafkaHandler otaMessageHandler = triplePair.getHandle();
				Class clazz = triplePair.getMsgtype();
				Object otamessage = otaMessageHandler.onReceive(kafkaMessage, clazz);
				otaMessageHandler.onMessage(otamessage);
			} else {
				log.error("Kafka message|{} lost...", kafkaMessage);
			}
		} catch (AdamException e) {
			log.error("业务异常|{}", e.getMessage(), e);
			throw e;
		} catch (Exception e) { // AdamException
			log.error("ota message error|{}", e.getMessage(), e);
			throw e;
		} finally {
			Thread.currentThread().setName(threadname0);
			OtaEventMonitor.clean();
		}
	}

	public String threadName(String sign) {
		String threadname0 = Thread.currentThread().getName();
		Thread.currentThread().setName(threadname0 + "-" + sign);
		return threadname0;

	}

	public TriplePair selectHandler(String topic) {
		return otaMessageHandlerClass.get(topic);
	}

	public void register(KafkaHandlerManager.KafkaHandler<?> otaMessageHandler) {
		Class<?> target = AopUtils.getTargetClass(otaMessageHandler);
		OtaMessageAttribute otaMessageAttribute = target.getAnnotation(OtaMessageAttribute.class);
		String[] topics = otaMessageAttribute.topics();
		Class<?> type = otaMessageAttribute.msgtype();

		if (ArrayUtils.isEmpty(topics)) {
			return;
		}
		for (String topic : topics) {
			TriplePair triplePair = TriplePair.of().setTopic(topic).setHandle(otaMessageHandler).setMsgtype(type);
			otaMessageHandlerClass.put(topic, triplePair);
		}

	}

	@NoArgsConstructor(staticName = "of")
	@Accessors(chain = true)
	@Data
	public static class TriplePair {
		String topic;
		KafkaHandlerManager.KafkaHandler<?> handle;
		Class<?> msgtype;
	}
}