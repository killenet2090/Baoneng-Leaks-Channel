package com.bnmotor.icv.tsp.vehstatus.service.kafka.producer;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.tsp.vehstatus.model.request.MessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @ClassName: SyncVehStatusProducer
 * @Description: 同步车辆状态
 * @author: huangyun1
 * @date: 2020/12/09
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Slf4j
public class StatusChangeNotifyProducer {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.producer.topics.status-change-notify-topic}")
    private String notifyTopic;

    public void sendStatusChangeNotify(MessageDto messageDto) {
        if (Objects.nonNull(messageDto.getVin())) {
            try {
                kafkaTemplate.send(notifyTopic, messageDto.getVin(), JsonUtil.toJson(messageDto));
            } catch (JsonProcessingException e) {
                log.error("发送车辆状态改变通知到kafka发送异常[{}]", e.getMessage());
                throw new AdamException(RespCode.UNKNOWN_ERROR);
            }
        }
    }

}
