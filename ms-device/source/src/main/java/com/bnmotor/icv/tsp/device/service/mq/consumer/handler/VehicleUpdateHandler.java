package com.bnmotor.icv.tsp.device.service.mq.consumer.handler;

import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.tsp.device.common.VehLocalCache;
import com.bnmotor.icv.tsp.device.service.mq.IMessageHandler;
import com.bnmotor.icv.tsp.device.service.mq.consumer.model.KafkaMessage;
import com.bnmotor.icv.tsp.device.service.mq.consumer.model.VehicleUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName: VehicleUpdateHandler
 * @Description: 车辆数据更新信息同步
 * @author: zhangwei2
 * @date: 2020/11/26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
@Service
public class VehicleUpdateHandler implements IMessageHandler {
    @Resource
    private VehLocalCache vehLocalCache;

    @Override
    public void onMessage(KafkaMessage kafkaMessage) {
        String message = kafkaMessage.getValue();
        try {
            VehicleUpdate vehicleUpdate = JsonUtil.toObject(message, VehicleUpdate.class);
            vehLocalCache.process(vehicleUpdate);
        } catch (Exception e) {
            log.error("车辆数据更新错误:" + message);
        }
    }
}
