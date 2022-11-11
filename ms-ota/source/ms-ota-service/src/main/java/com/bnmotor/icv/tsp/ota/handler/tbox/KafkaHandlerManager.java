package com.bnmotor.icv.tsp.ota.handler.tbox;

import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.adam.kafka.domain.KafkaMessage;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collection;

/**
 * @ClassName: KafkaHandlerManager.java KafkaHandlerManager
 * @Description: 
 * @author E.YanLonG
 * @since 2020-10-21 20:03:25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Slf4j
public class KafkaHandlerManager implements ApplicationContextAware {

	private ApplicationContext applicationContext;
	
	@Autowired
	CustOtaMessageHandler custOtaMessageHandler;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
	
	@SuppressWarnings("rawtypes")
	@PostConstruct
	public void post() {
		Collection<KafkaHandlerManager.KafkaHandler> kafkaHandlerList = applicationContext.getBeansOfType(KafkaHandlerManager.KafkaHandler.class).values();
		kafkaHandlerList.forEach(kafkaHandler -> custOtaMessageHandler.register(kafkaHandler));
		if(MyCollectionUtil.isEmpty(kafkaHandlerList)) {
			log.warn("系统未定义消息处理bean,请检查是否正常");
		}
	}
	
	public interface KafkaHandler<T> {

		void onMessage(T message);

		default T onReceive(KafkaMessage kafkaMessage, Class<T> clazz) {
			T otaMessage = null;
			try {
				log.info("OTA UP MESSAGE RECEIVED, MESSAGE : [ {} ]" + kafkaMessage.getValue());
				otaMessage = JsonUtil.toObject(kafkaMessage.getValue(), clazz);
			} catch (IOException e) {
				log.error("OTA UP MESSAGE RECEIVE FAILED, ERROR MESSAGE : [ {} ]", e.getMessage(), e);
			}
			return otaMessage;
		}
	}

}