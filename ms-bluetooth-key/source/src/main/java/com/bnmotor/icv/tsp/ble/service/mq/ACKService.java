package com.bnmotor.icv.tsp.ble.service.mq;

import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.adam.kafka.producer.KafkaProducer;
import com.bnmotor.icv.adam.sdk.bluetooth.down.BluetoothDownDto;
import com.bnmotor.icv.adam.sdk.bluetooth.up.BluetoothUpDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;

/**
 * @ClassName: ACKService
 * @Description:
 * @author: wuhao1
 * @data: 2020-08-03
 * @Copyrigght: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
@RequiredArgsConstructor
public class ACKService {
    private final KafkaProducer kafkaProducer;

    @Value("bluetooth.down.topic:tsp-bluetooth-down")
    private String bluetoothDownTopic;

    public BluetoothDownDto generateDownDto(BluetoothUpDto upDto) {
        BluetoothDownDto downDto = new BluetoothDownDto();
        downDto.setAck(true);
        downDto.setBusinessSerialNum(upDto.getBusinessSerialNum());
        downDto.setCmdEnum(upDto.getCmdEnum());
        downDto.setVin(upDto.getVin());
        downDto.setVersion(upDto.getVersion());
        downDto.setPlatformTime(Calendar.getInstance().getTimeInMillis());
        downDto.setPshContentDto(upDto.getPshContentDto());
        return downDto;
    }

    public void sendDownDto(BluetoothDownDto downDto) throws JsonProcessingException {
        String value = JsonUtil.toJson(downDto);
        kafkaProducer.sendMsg(bluetoothDownTopic, value);
    }
}
