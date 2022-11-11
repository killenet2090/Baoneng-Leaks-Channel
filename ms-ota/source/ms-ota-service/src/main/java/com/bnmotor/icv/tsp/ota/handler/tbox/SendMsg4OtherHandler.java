package com.bnmotor.icv.tsp.ota.handler.tbox;

import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.adam.kafka.producer.KafkaProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @ClassName: SendMsg4OtherHandler
 * @Description: OTA平台下发给其他应用系统的kafka发送类
 * @author: xuxiaochang1
 * @date: 2021/1/13 15:25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Service
public class SendMsg4OtherHandler {
    private static final Logger log = LoggerFactory.getLogger(SendMsg4OtherHandler.class);
    @Autowired
    private KafkaProducer kafkaProducer;
    @Value("${bnmotor.icv.kafka.ota.dispatch.send}")
    public String otaDispatchTopic;

    @PostConstruct
    protected void init() {
        log.info("当前应用消费主题 : [ {} ]", this.otaDispatchTopic);
    }

    public <T> void sendMsg(T t) {
        try {
            this.kafkaProducer.sendMsg(this.otaDispatchTopic, JsonUtil.toJson(t));
            log.info("OTA DISPATCH MESSAGE SENT, PARAMETERS : [ {} ] , TOPIC : [ {} ]", JsonUtil.toJson(t), this.otaDispatchTopic);
        } catch (Throwable var3) {
            log.error("OTA DISPATCH MESSAGE SENT FAILED, ERROR MESSAGE : [ {} ]", var3);
        }
    }
}
