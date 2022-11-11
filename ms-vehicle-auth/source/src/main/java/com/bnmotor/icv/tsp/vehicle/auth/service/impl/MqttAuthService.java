package com.bnmotor.icv.tsp.vehicle.auth.service.impl;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.vehicle.auth.model.request.MqttAuthConnectDto;
import com.bnmotor.icv.tsp.vehicle.auth.model.response.VehicleDeviceVo;
import com.bnmotor.icv.tsp.vehicle.auth.service.IMqttAuthService;
import com.bnmotor.icv.tsp.vehicle.auth.service.feign.VehicleFeignService;
import com.bnmotor.icv.tsp.vehicle.auth.util.ClientIdUtils;
import com.bnmotor.icv.tsp.vehicle.auth.util.FeignUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: VehicleAuthService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author: wuhao1
 * @date: 2020/12/22
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class MqttAuthService implements IMqttAuthService {

    @Value("${bnmotor.icv.mqtt.auth.connect.tbox.user-name}")
    private String tboxUserName;

    @Value("${bnmotor.icv.mqtt.auth.connect.tbox.password}")
    private String tboxPassword;

    private final VehicleFeignService vehicleFeignService;

    @Override
    public ResponseEntity tboxAuth(MqttAuthConnectDto mqttAuthConnectDto, int deviceType) {
        // clientId格式校验
        if (!(ClientIdUtils.isClientIdTbox(mqttAuthConnectDto.getClientId()) || ClientIdUtils.isClientIdIHU(mqttAuthConnectDto.getClientId()))) {
            log.warn("clientId [{}] is not tbox or ihu", mqttAuthConnectDto.getClientId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("clientId格式校验错误");
        }
        String clientVin = ClientIdUtils.getVinFromClientId(mqttAuthConnectDto.getClientId());
        String clientSN = ClientIdUtils.getSNFromClientId(mqttAuthConnectDto.getClientId());
        RestResponse<List<VehicleDeviceVo>> restResponse = vehicleFeignService.listDevice(clientVin, deviceType);
        FeignUtils.checkRestResponse(restResponse);
        List<VehicleDeviceVo> vehicleDeviceVos = restResponse.getRespData();
        VehicleDeviceVo expectDeviceVo = null;
        if (CollectionUtils.isNotEmpty(vehicleDeviceVos)) {
            // 随便取一个
            expectDeviceVo = vehicleDeviceVos.get(0);
        }
        if (expectDeviceVo == null) {
            String msg = "vin " + clientVin + " and deviceType " + deviceType + " not found";
            log.warn(msg);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        if (StringUtils.equalsIgnoreCase(clientSN, expectDeviceVo.getProductSn())) {
            return ResponseEntity.ok("VIN和SN匹配");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("VIN:" + clientVin + "的SN号不匹配, req sn:" + clientSN);
        }
    }
}
