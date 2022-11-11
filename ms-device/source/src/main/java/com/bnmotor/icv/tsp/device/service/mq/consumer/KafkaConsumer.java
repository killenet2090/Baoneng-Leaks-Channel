package com.bnmotor.icv.tsp.device.service.mq.consumer;

import com.alibaba.excel.util.CollectionUtils;
import com.bnmotor.icv.tsp.device.service.mq.IMessageHandler;
import com.bnmotor.icv.tsp.device.service.mq.consumer.model.KafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: GuestModeConsumer
 * @Description: 临客模式消费
 * @author: zhangwei2
 * @date: 2020/11/25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
@Slf4j
@Order
public class KafkaConsumer {
    @Resource(name = "guestModelHandler")
    private IMessageHandler guestModelHandler;
    @Resource(name = "vehicleLoginHandler")
    private IMessageHandler vehicleLoginHandler;
    @Resource(name = "vehicleUpdateHandler")
    private IMessageHandler vehicleUpdateHandler;

    @KafkaListener(id = "${spring.kafka.guest.consumer.id}", topics = "#{'${spring.kafka.guest.consumer.topics}'.split(',')}",
            groupId = "${spring.kafka.guest.consumer.group.id}", concurrency = "${spring.kafka.guest.consumer.concurrency.num}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consumerGuestMsg(List<ConsumerRecord<String, String>> records, Acknowledgment acknowledgment) {
        commonProcess(records, acknowledgment, guestModelHandler);
    }

    @KafkaListener(id = "${spring.kafka.login.consumer.id}", topics = "#{'${spring.kafka.login.consumer.topics}'.split(',')}",
            groupId = "${spring.kafka.login.consumer.group.id}", concurrency = "${spring.kafka.login.consumer.concurrency.num}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consumerLoginMsg(List<ConsumerRecord<String, String>> records, Acknowledgment acknowledgment) {
        commonProcess(records, acknowledgment, vehicleLoginHandler);
    }

    @KafkaListener(id = "${spring.kafka.vehUpdate.consumer.id}", topics = "#{'${spring.kafka.vehUpdate.consumer.topics}'.split(',')}",
            groupId = "${spring.kafka.vehUpdate.consumer.group.id}", concurrency = "${spring.kafka.vehUpdate.consumer.concurrency.num}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consumerVehUpdateMsg(List<ConsumerRecord<String, String>> records, Acknowledgment acknowledgment) {
        commonProcess(records, acknowledgment, vehicleUpdateHandler);
    }

    private void commonProcess(List<ConsumerRecord<String, String>> records, Acknowledgment acknowledgment, IMessageHandler messageHandler) {
        // 消息进来后默认手动提交，默认消息都能正确处理
        if (CollectionUtils.isEmpty(records)) {
            log.error("收到kafka消息为空！");
        } else {
            records.forEach(record -> {
                log.info("收到kafka消息，消息topic：{}, 消息内容：{}，消息partition：{}。",
                        record.topic(), record.value(), record.partition());
                if (null != messageHandler) {
                    KafkaMessage kafkaMessage = constructKafkaMsg(record);
                    try {
                        messageHandler.onMessage(kafkaMessage);
                    } catch (Exception e) {
                        log.error("消息处理失败", e);
                    }
                }
            });
        }
    }

    private KafkaMessage constructKafkaMsg(ConsumerRecord<String, String> record) {
        KafkaMessage kafkaMessage = new KafkaMessage();
        kafkaMessage.setKey(record.key());
        kafkaMessage.setValue(record.value());
        kafkaMessage.setPartition(record.partition());
        kafkaMessage.setTimestamp(record.timestamp());
        kafkaMessage.setTopic(record.topic());
        return kafkaMessage;
    }
}
