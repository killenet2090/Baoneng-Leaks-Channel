package com.bnmotor.icv.tsp.ota.common.event;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationEvent;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @ClassName: OtaEvent.java OtaEvent
 * @Description: 应用内消息处理
 * @author E.YanLonG
 * @since 2020-11-16 11:59:08
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Accessors(chain = true)
@Getter
@Setter
public class OtaEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	
	static final AtomicLong EVENT_ID = new AtomicLong(0);

	String transId;
	Integer times;
	OtaEventType eventType;

	@Deprecated
	public OtaEvent(Object source, String transId, Integer times) {
		super(source);
		this.transId = transId;
		this.times = times;
	}
	
	public OtaEvent(OtaEventType otaEventType, Object source) {
		super(source);
		this.eventType = otaEventType;
		this.transId = "$$" + EVENT_ID.getAndIncrement();
		this.times = 0;
	}

}