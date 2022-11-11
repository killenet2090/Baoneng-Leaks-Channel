package com.bnmotor.icv.tsp.device.service.mq.consumer.model;

import lombok.Data;

/**
 * @ClassName: KafkaMessage
 * @Description: kafka消息体
 * @author: zhangwei2
 * @date: 2020/6/10 8:19 下午
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class KafkaMessage
{
    /**
     * key
     */
    private String key;

    /**
     * 消息内容
     */
    private String value;

    /**
     * 时间戳
     */
    private long timestamp;

    /**
     * topic
     */
    private String topic;

    /**
     * 消息所属分区
     */
    private int partition;
}

