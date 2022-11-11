package com.bnmotor.icv.tsp.device.service.mq.consumer.handler;

import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.tsp.device.common.enums.vehStatus.GuestModelType;
import com.bnmotor.icv.tsp.device.common.enums.vehStatus.VehLifecircleStatus;
import com.bnmotor.icv.tsp.device.mapper.VehicleMapper;
import com.bnmotor.icv.tsp.device.model.entity.VehiclePo;
import com.bnmotor.icv.tsp.device.service.mq.IMessageHandler;
import com.bnmotor.icv.tsp.device.service.mq.consumer.model.KafkaMessage;
import com.bnmotor.icv.tsp.device.service.mq.consumer.model.GuestUp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @ClassName: GuestModelHandler
 * @Description: 临客模式handler实现
 * @author: zhangwei2
 * @date: 2020/11/25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
@Service
public class GuestModelHandler implements IMessageHandler {
    @Resource
    private VehicleMapper vehicleMapper;

    @Override
    public void onMessage(KafkaMessage kafkaMessage) {
        String message = kafkaMessage.getValue();
        try {
            GuestUp guestUp = JsonUtil.toObject(message, GuestUp.class);
            GuestModelType status = GuestModelType.valueOf(guestUp.getStatus());
            if (status == null) {
                log.warn("临客模式上行数据错误:" + message);
                return;
            }

            VehiclePo vehiclePo = vehicleMapper.selectByVin(guestUp.getVin());
            if (vehiclePo == null) {
                log.warn("临客模式上行数据错误,车辆不存在:" + message);
                return;
            }

            VehLifecircleStatus lifecircleStatus = GuestModelType.transform(status);
            if (lifecircleStatus == null) {
                return;
            }

            vehiclePo.setVehLifecircle(lifecircleStatus.getStatus());
            vehiclePo.setUpdateTime(LocalDateTime.now());
            vehicleMapper.updateById(vehiclePo);
        } catch (Exception e) {
            log.error("临客模式上行数据错误:" + message);
        }
    }
}
