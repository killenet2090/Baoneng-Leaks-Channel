package com.bnmotor.icv.tsp.device.service.mq.producer;

import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.tsp.device.model.request.feign.MqMessageDto;
import com.bnmotor.icv.tsp.device.service.mq.producer.dataaysn.DataSysnMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName: VehicleMqSender
 * @Description: 车辆增量信息同步
 * @author: zhangwei2
 * @date: 2020/10/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Slf4j
public class KafkaSender {
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 发送mq消息
     */
    public void sendMqMsg(MqMessageDto messageDto) {
        try {
            kafkaTemplate.send("user-vehicle-topic", JsonUtil.toJson(messageDto));
        } catch (Exception e) {
            log.error("异步查询发送消息异常", e);
        }
    }

    /**
     * 发送同步车辆消息
     *
     * @param topic 队列topic
     * @param msg   车辆数据
     */
    public void sendDataAysnMsg(String topic, DataSysnMessage msg) {
        try {
            kafkaTemplate.send(topic, JsonUtil.toJson(msg));
        } catch (Exception e) {
            log.error("发送消息异常", e);
        }
    }

    /**
     * 发送消息
     *
     * @param topic 队列
     * @param msg   消息
     */
    public void sendMsg(String topic, String msg) {
        try {
            kafkaTemplate.send(topic, msg);
        } catch (Exception e) {
            log.error("发送消息异常", e);
        }
    }
}
