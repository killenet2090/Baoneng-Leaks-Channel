package com.bnmotor.icv.tsp.vehstatus.service.chain.impl;


import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.adam.data.redis.StringRedisProvider;
import com.bnmotor.icv.tsp.vehstatus.common.enums.BusinessTypeEnum;
import com.bnmotor.icv.tsp.vehstatus.common.enums.VehStatusEnum;
import com.bnmotor.icv.tsp.vehstatus.common.util.EnumUtils;
import com.bnmotor.icv.tsp.vehstatus.controller.assemble.ColumnHandler;
import com.bnmotor.icv.tsp.vehstatus.model.request.MessageDto;
import com.bnmotor.icv.tsp.vehstatus.model.request.NotifyEventDto;
import com.bnmotor.icv.tsp.vehstatus.service.chain.IHandlerChain;
import com.bnmotor.icv.tsp.vehstatus.service.kafka.producer.StatusChangeNotifyProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
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
 * @ClassName: StatusChangeNotifyHandlerChain
 * @Description: 状态改变通知处理类
 * @author: huangyun1
 * @date: 2020/11/25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@Slf4j
@RefreshScope
public class StatusChangeNotifyHandlerChain implements IHandlerChain {

    @Autowired
    private StringRedisProvider stringRedisProvider;
    @Resource
    private StatusChangeNotifyProducer statusChangeNotifyProducer;
    @Autowired
    private List<ColumnHandler> handlerChainList;

    @Override
    public void handler(NotifyEventDto notifyEventDto) {
        String redisKey = notifyEventDto.getRedisKey();
        String vin = notifyEventDto.getVin();
        Map<Object, Object> newMap = notifyEventDto.getSaveRedisMap();
        //充电枪&充电状态
        if (Objects.isNull(newMap.get(VehStatusEnum.CHARGING_GUN_CONNECT_STATE.getCode())) &&
                Objects.isNull(newMap.get(VehStatusEnum.BMS_CHARGING_STATE.getCode()))) {
            return;
        }
        //
        String[] queryKey = new String[] {
                VehStatusEnum.CHARGING_GUN_CONNECT_STATE.getCode(),
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
        Object chargingGunConnectStateNewValue = newMap.get(VehStatusEnum.CHARGING_GUN_CONNECT_STATE.getCode());
        if (chargingGunConnectStateNewValue == null || chargingGunConnectStateNewValue.equals(oldMap.get(VehStatusEnum.CHARGING_GUN_CONNECT_STATE.getCode()))) {
            oldMap.remove(VehStatusEnum.CHARGING_GUN_CONNECT_STATE.getCode());
        }

        Object bmsChargingStateNewValue = newMap.get(VehStatusEnum.BMS_CHARGING_STATE.getCode());
        if (bmsChargingStateNewValue == null || bmsChargingStateNewValue.equals(oldMap.get(VehStatusEnum.BMS_CHARGING_STATE.getCode()))) {
            oldMap.remove(VehStatusEnum.BMS_CHARGING_STATE.getCode());
        }
        if (oldMap.keySet().isEmpty()) {
            return;
        }
        //加入消息通知
        sendToKafka(vin, newMap, oldMap, notifyEventDto.getPlatformTime());
    }

    private void sendToKafka(String vin, Map<Object, Object> newMap, Map<String, Object> oldMap, Long platformTime) {
        MessageDto.MessageDtoBuilder builder = MessageDto.builder();
        try {
            Map statusMap = Maps.newHashMap();
            oldMap.entrySet().stream().forEach(entry -> {
                statusMap.put(EnumUtils.getDescByCode(VehStatusEnum.class, entry.getKey()), newMap.get(entry.getKey()));
            });
            statusMap.put(VehStatusEnum.DISTANCE_TO_EMPTY.getColumnName(), newMap.get(VehStatusEnum.DISTANCE_TO_EMPTY.getCode()));
            handlerChainList.stream().forEach(handler -> {
                handler.handleVo(statusMap);
            });
            builder.businessType(BusinessTypeEnum.VEH_STATUS_CHANGE_NOTIFY.getValue())
                    .timestamp(Long.parseLong(newMap.get(VehStatusEnum.TIMESTAMP.getCode()).toString()))
                    .vin(vin)
                    .jsonPayload(JsonUtil.toJson(statusMap))
                    .platformTime(platformTime);
        } catch (JsonProcessingException e) {
            log.error("车辆状态发生改变推送至kafka发生异常[{}]", e.getMessage());
            throw new AdamException(RespCode.UNKNOWN_ERROR);
        }
        statusChangeNotifyProducer.sendStatusChangeNotify(builder.build());
    }
}