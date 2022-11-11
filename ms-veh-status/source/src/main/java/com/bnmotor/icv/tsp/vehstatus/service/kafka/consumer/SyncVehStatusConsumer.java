package com.bnmotor.icv.tsp.vehstatus.service.kafka.consumer;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.tsp.vehstatus.common.enums.BusinessTypeEnum;
import com.bnmotor.icv.tsp.vehstatus.controller.AcceptEventController;
import com.bnmotor.icv.tsp.vehstatus.model.response.MessageVo;
import com.bnmotor.icv.tsp.vehstatus.model.request.SetVehStatusDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @ClassName: SyncVehStatusConsumer
 * @Description: 同步车辆状态消费
 * @author: huangyun1
 * @date: 2020/12/09
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Slf4j
@Order
public class SyncVehStatusConsumer {
    @Autowired
    private AcceptEventController acceptEventController;

    @KafkaListener(id = "${spring.kafka.consumer.status-sync.client-id}", topics = "#{'${spring.kafka.consumer.status-sync.topic}'.split(',')}",
            groupId = "${spring.kafka.consumer.status-sync.group-id}", concurrency = "${spring.kafka.consumer.status-sync.concurrency}",
            containerFactory = "statusSyncContainerFactory")
    public void chargingStartNotify(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        log.info("接收到同步车辆状态信息,消息topic：{}, 消息内容：{}，消息partition：{}。", record.topic(), record.value(), record.partition());
        try {
            MessageVo messageVo = JsonUtil.toObject(record.value(), MessageVo.class);
            if (BusinessTypeEnum.SYNC_VEH_STATUS.getValue().equals(messageVo.getBusinessType())) {
                SetVehStatusDto setVehStatusDto = JsonUtil.toObject(messageVo.getJsonPayload(), SetVehStatusDto.class);
                setVehStatusDto.setVin(messageVo.getVin());
                acceptEventController.setVehStatus(setVehStatusDto);
            }
            acknowledgment.acknowledge();
        } catch (IOException e) {
            log.error("处理同步车辆状态信息发生异常[{}]", e.getMessage());
            throw new AdamException(RespCode.UNKNOWN_ERROR);
        }
    }

}
