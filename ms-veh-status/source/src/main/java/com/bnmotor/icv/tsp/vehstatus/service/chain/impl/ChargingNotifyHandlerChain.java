package com.bnmotor.icv.tsp.vehstatus.service.chain.impl;


import com.bnmotor.icv.adam.core.uid.IUIDGenerator;
import com.bnmotor.icv.adam.data.redis.StringRedisProvider;
import com.bnmotor.icv.tsp.vehstatus.common.enums.BusinessTypeEnum;
import com.bnmotor.icv.tsp.vehstatus.common.enums.BmsChargingStateEnum;
import com.bnmotor.icv.tsp.vehstatus.common.enums.VehStatusEnum;
import com.bnmotor.icv.tsp.vehstatus.model.request.MessageDto;
import com.bnmotor.icv.tsp.vehstatus.model.request.NotifyEventDto;
import com.bnmotor.icv.tsp.vehstatus.service.chain.IHandlerChain;
import com.bnmotor.icv.tsp.vehstatus.service.kafka.producer.ChargingNotifyProducer;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName: ChargingNotifyHandlerChain
 * @Description: 充电通知处理类
 * @author: huangyun1
 * @date: 2020/11/25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@Slf4j
@RefreshScope
public class ChargingNotifyHandlerChain implements IHandlerChain {

    @Autowired
    private StringRedisProvider stringRedisProvider;
    @Resource
    private ChargingNotifyProducer chargingNotifyProducer;
    @Autowired
    private IUIDGenerator iuidGenerator;

    @Override
    public void handler(NotifyEventDto notifyEventDto) {
        String redisKey = notifyEventDto.getRedisKey();
        String vin = notifyEventDto.getVin();
        Map<Object, Object> newMap = notifyEventDto.getSaveRedisMap();
        //判断充电项
        // $0=Not charging
        if (Objects.isNull(newMap.get(VehStatusEnum.BMS_CHARGING_STATE.getCode())) ||
                BmsChargingStateEnum.DEFAULT.getValue().equals(newMap.get(VehStatusEnum.CHARGING_STATE.getCode())) ||
                BmsChargingStateEnum.PREPARE_CHARGING.getValue().equals(newMap.get(VehStatusEnum.CHARGING_STATE.getCode())) ||
                BmsChargingStateEnum.APPOINTMENT_CHARGE.getValue().equals(newMap.get(VehStatusEnum.CHARGING_STATE.getCode()))) {
            return;
        }
        String[] queryKey = new String[] {
                VehStatusEnum.BMS_CHARGING_STATE.getCode()
        };
        //判断是否需要通知
        List queryResult = stringRedisProvider.getMultiHash(redisKey, queryKey);
        Map<String, Object> oldMap = Maps.newHashMap();
        for(int i = 0; i < queryResult.size(); i++) {
            if (Objects.isNull(queryResult.get(i))) {
                continue;
            }
            oldMap.put(queryKey[i], queryResult.get(i));
        }
        //如果状态位未改变 无需重复提醒
        if (newMap.get(VehStatusEnum.BMS_CHARGING_STATE.getCode()).equals(oldMap.get(VehStatusEnum.BMS_CHARGING_STATE.getCode()))) {
            return;
        }
        //加入消息通知
        sendToKafka(vin, newMap, oldMap, notifyEventDto.getPlatformTime());
    }

    /**
     * //充电开始
     * // $1=Charging
     * //充电异常
     * // $3=BMS error stop
     * // $4=BMS abnormal stop
     * // $6=DC charger error stop
     * //充电完成
     * // $2=Charge Full
     * // $5=DC charger normal stop
     * // $7=VCU stop
     * // $8=OBC stop
     * @param vin
     * @param newMap
     * @param oldMap
     */
    private void sendToKafka(String vin, Map<Object, Object> newMap, Map<String, Object> oldMap, Long platformTime) {
        Object newValue = newMap.get(VehStatusEnum.BMS_CHARGING_STATE.getCode());
        MessageDto.MessageDtoBuilder builder = MessageDto.builder();
        builder.businessId(iuidGenerator.nextId())
                .platformTime(platformTime);
        if (BmsChargingStateEnum.CHARGING.getValue().equals(newValue)) {
            builder.businessType(BusinessTypeEnum.CHARGING_START_NOTIFY.getValue())
                    .timestamp(Long.parseLong(newMap.get(VehStatusEnum.TIMESTAMP.getCode()).toString()))
                    .vin(vin);
        } else if (BmsChargingStateEnum.COMPLETE_CHARGING.getValue().equals(newValue)) {
            builder.businessType(BusinessTypeEnum.CHARGING_FINISH_NOTIFY.getValue())
                    .timestamp(Long.parseLong(newMap.get(VehStatusEnum.TIMESTAMP.getCode()).toString()))
                    .vin(vin);
        } else if (BmsChargingStateEnum.ERROR_CHARGING.getValue().equals(newValue)) {
            builder.businessType(BusinessTypeEnum.CHARGING_EXCEPTION_NOTIFY.getValue())
                    .timestamp(Long.parseLong(newMap.get(VehStatusEnum.TIMESTAMP.getCode()).toString()))
                    .vin(vin);
        }
        if (Objects.isNull(builder.build().getBusinessType())) {
            return;
        }
        chargingNotifyProducer.sendChargingNotify(builder.build());
    }
}