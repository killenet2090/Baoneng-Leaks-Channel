package com.bnmotor.icv.tsp.device.service.mq.consumer.handler;

import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.tsp.device.common.enums.veh.LoginStatus;
import com.bnmotor.icv.tsp.device.common.enums.vehStatus.VehLifecircleStatus;
import com.bnmotor.icv.tsp.device.mapper.VehicleMapper;
import com.bnmotor.icv.tsp.device.model.entity.VehiclePo;
import com.bnmotor.icv.tsp.device.service.mq.IMessageHandler;
import com.bnmotor.icv.tsp.device.service.mq.consumer.model.KafkaMessage;
import com.bnmotor.icv.tsp.device.service.mq.consumer.model.LoginUp;
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
public class VehicleLoginHandler implements IMessageHandler {
    @Resource
    private VehicleMapper vehicleMapper;

    @Override
    public void onMessage(KafkaMessage kafkaMessage) {
        String message = kafkaMessage.getValue();
        try {
            LoginUp loginUp = JsonUtil.toObject(message, LoginUp.class);
            LoginStatus loginStatus = LoginStatus.valueOf(loginUp.getLoginStatus());
            if (loginStatus == null) {
                log.warn("车机登录上报状态错误:" + message);
                return;
            }

            if (loginStatus.equals(LoginStatus.LOGIN_OUT)) {
                return;
            }

            VehiclePo vehiclePo = vehicleMapper.selectByVin(loginUp.getVin());
            if (vehiclePo == null) {
                log.warn("车机登录上报状态错误,车辆不存在:" + message);
                return;
            }

            if (!(vehiclePo.getVehLifecircle().equals(VehLifecircleStatus.GUEST_MODE.getStatus())) &&
                    !(vehiclePo.getVehLifecircle().equals(VehLifecircleStatus.GUEST_MODE_OPENING.getStatus()))
                    && !(vehiclePo.getVehLifecircle().equals(VehLifecircleStatus.GUEST_MODE_CLOSING.getStatus()))) {
                return;
            }

            vehiclePo.setVehLifecircle(VehLifecircleStatus.USER_MODE.getStatus());
            vehiclePo.setUpdateTime(LocalDateTime.now());
            vehicleMapper.updateById(vehiclePo);
        } catch (Exception e) {
            log.error("车机登录数据错误:" + message);
        }
    }
}
