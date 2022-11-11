package com.bnmotor.icv.tsp.ota.common.event;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: OtaTransactionEvent.java OtaTransactionEvent
 * @Description: 事物处理
 * @author E.YanLonG
 * @since 2020-11-19 8:34:08
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Getter
@Setter
public class OtaTransactionEvent<OtaEvent> extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	
	private OtaEvent data;

	public OtaTransactionEvent(Object source, OtaEvent data) {
		super(source);
		this.data = data;
	}

	public OtaEvent data() {
		return data;
	}
}