package com.bnmotor.icv.tsp.vehstatus.service.impl;

import com.bnmotor.icv.adam.data.redis.StringRedisProvider;
import com.bnmotor.icv.adam.sdk.realtime.status.dto.RealtimeStatusDto;
import com.bnmotor.icv.adam.sdk.realtime.status.handler.IRealtimeStatusHandler;
import com.bnmotor.icv.tsp.vehstatus.common.Constant;
import com.bnmotor.icv.tsp.vehstatus.common.util.CommonUtils;
import com.bnmotor.icv.tsp.vehstatus.config.convert.VehStatusConvertForMap;
import com.bnmotor.icv.tsp.vehstatus.model.request.NotifyEventDto;
import com.bnmotor.icv.tsp.vehstatus.service.impl.notify.NotifyEvent;
import com.bnmotor.icv.tsp.vehstatus.service.impl.notify.NotifyPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: VelStatusAcceptHandler
 * @Description: 车况上行消息接收
 * @author: huangyun1
 * @date: 2020/6/16
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class VelStatusAcceptHandler implements IRealtimeStatusHandler {

    @Autowired
    private StringRedisProvider stringRedisProvider;
    @Autowired
    private NotifyPublisher notifyPublisher;

    private final VehStatusConvertForMap<String> statusNameForStatusConvertMap;

    private final Integer MIN_VALUE = Integer.MIN_VALUE;

    @Override
    public void onMessage(RealtimeStatusDto realtimeStatusDto) {
        Map<Object, Object> tempRedisMap = new HashMap<>(130);
        Map<String, Number> statusNameValueMap = realtimeStatusDto.getStatusKeyValueMap();
        tempRedisMap.putAll(statusNameValueMap);
        Map<Object, Object> saveRedisMap = new HashMap<>(130);
        //移除无效值
        tempRedisMap.forEach((key,value) -> {
            if(!MIN_VALUE.equals(value)) {
                saveRedisMap.put(key, String.valueOf(value));
            }
        });
        //存入更新时间
        String msgTimeFieldKey = statusNameForStatusConvertMap.get(Constant.TIMESTAMP).getCode();
        saveRedisMap.put(msgTimeFieldKey, String.valueOf(realtimeStatusDto.getMsgtime()));
        String redisKey = CommonUtils.appendString(Constant.REDIS_JOINER_CHAR, Constant.REDIS_PROJECT_PREFIX, Constant.STATUS_MODEL, realtimeStatusDto.getVin());

        Boolean hasKey = stringRedisProvider.hasKey(redisKey);
        if (hasKey && Constant.IS_RE_SEND.equals(realtimeStatusDto.getIsReSend())) {
            return;
        }
        if (!Constant.IS_RE_SEND.equals(realtimeStatusDto.getIsReSend())) {
            //通知其它业务
            NotifyEventDto notifyEventDto = NotifyEventDto.builder()
                    .redisKey(redisKey)
                    .vin(realtimeStatusDto.getVin())
                    .saveRedisMap(saveRedisMap)
                    .platformTime(realtimeStatusDto.getPlatformTime())
                    .build();
            notifyPublisher.publisherEvent(new NotifyEvent(notifyEventDto));
        }

        //存入redis缓存
        stringRedisProvider.setMultiHash(redisKey, saveRedisMap);
    }
}
