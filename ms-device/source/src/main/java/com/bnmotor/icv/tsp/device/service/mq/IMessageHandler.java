package com.bnmotor.icv.tsp.device.service.mq;

import com.bnmotor.icv.tsp.device.service.mq.consumer.model.KafkaMessage;

/**
 * @ClassName: IMessageHandler
 * @Description: 消息处理接口
 * @author: zhangwei2
 * @date: 2020/11/25 8:17 下午
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public interface IMessageHandler {
    void onMessage(KafkaMessage kafkaMessage);
}

