package com.bnmotor.icv.tsp.ota.handler.tbox;

import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.adam.kafka.producer.KafkaProducer;
import com.bnmotor.icv.adam.sdk.ota.domain.OtaProtocol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @ClassName: TestSendMsgFromTboxHandler
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/9/19 11:25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Service
@Slf4j
public class TestSendMsgFromTboxHandler {
    
	@Autowired
    private KafkaProducer kafkaProducer;
    
	@Value("${adam.kafka.consumer.topics}")
    public String otaUpCommandTopic;

    public TestSendMsgFromTboxHandler() {
    }

    @PostConstruct
    protected void init() {
        log.info("当前应用消费主题 : [ {} ]", this.otaUpCommandTopic);
    }

    public void senMsg2Ota(OtaProtocol otaProtocol){
        try {
            this.kafkaProducer.sendMsg(this.otaUpCommandTopic, JsonUtil.toJson(otaProtocol));
            log.info("OTA DOWN MESSAGE SENT, PARAMETERS : [ {} ] , TOPIC : [ {} ]", JsonUtil.toJson(otaProtocol), this.otaUpCommandTopic);
        } catch (Throwable var3) {
            var3.printStackTrace();
            log.error("OTA DOWN MESSAGE SENT FAILED, ERROR MESSAGE : [ {} ]", var3);
        }

    }
}
