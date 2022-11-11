package com.bnmotor.icv.tsp.ota.common.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

/**
 * @ClassName: OtaEventPublisher.java 
 * @Description: OTQ消息OtaEventPublisher
 * @author E.YanLonG
 * @since 2020-11-15 9:02:51
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
public class OtaEventPublisher implements ApplicationEventPublisherAware {

	private ApplicationEventPublisher publisher;

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}

//	@Deprecated
//	private void pushOtaEvent(String serviceNo, Integer times) {
//		publisher.publishEvent(new OtaEvent(this, serviceNo, times));
//	}

	public void pushOtaEvent(OtaEvent otaEvent) {
		publisher.publishEvent(otaEvent);
	}
	
	public void pushEvent(ApplicationEvent applicationEvent) {
		publisher.publishEvent(applicationEvent);
	}

}