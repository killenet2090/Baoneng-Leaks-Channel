package com.bnmotor.icv.tsp.vehicle.auth.controller;

import com.bnmotor.icv.tsp.vehicle.auth.common.enums.DeviceTypeEnum;
import com.bnmotor.icv.tsp.vehicle.auth.model.request.MqttAuthConnectDto;
import com.bnmotor.icv.tsp.vehicle.auth.service.impl.MqttAuthService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: VehicleAuthController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author: wuhao1
 * @date: 2020/12/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@RequiredArgsConstructor
@RequestMapping("/vehicle/auth")
@RestController
public class MqttAuthController {

    private final MqttAuthService vehicleAuthService;

    /**
     * TBox登录检查
     * @param
     * @return
     */
    @ApiOperation(value = "TBox连接认证")
    @PostMapping("/tbox")
    public ResponseEntity tboxConnectAuth(MqttAuthConnectDto mqttAuthConnectDto) {
        return vehicleAuthService.tboxAuth(mqttAuthConnectDto, DeviceTypeEnum.TBOX.getType());
    }
}
