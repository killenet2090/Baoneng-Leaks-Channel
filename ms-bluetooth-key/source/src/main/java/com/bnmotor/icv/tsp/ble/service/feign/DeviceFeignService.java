package com.bnmotor.icv.tsp.ble.service.feign;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ble.model.request.feign.BleVerifyAuthCodeDto;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleDeviceSn;
import com.bnmotor.icv.tsp.ble.model.response.ble.VehicleInfoVo;
import com.bnmotor.icv.tsp.ble.service.feign.fallback.DeviceFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName: DeviceFeignService
 * @Description: 车辆服务接口
 * @author: huangyun1
 * @date: 2020/5/13
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@FeignClient(name = "ms-device",  fallback = DeviceFeignFallbackFactory.class)
public interface DeviceFeignService {
    /**
     * 车辆详情接口,用于获取车辆基本信息,车辆认证,车辆设备绑定信息,标签等信息
     * @param vin
     * @return
     */
    @GetMapping("/inner/vehicle/vehicle")
    RestResponse<VehicleInfoVo> getVehicleInfo(@RequestParam("vin") String vin);

    @GetMapping("/v1/vehicleBind/delBlueKeyCallback")
    RestResponse<String> delBlueKeyCallback(@RequestBody BleVerifyAuthCodeDto bleVerifyAuthCodeDto);

    @GetMapping("/inner/vehDevice/device")
    RestResponse<List<BleDeviceSn>> queryBleDeviceSn(@RequestParam("deviceType") int deviceType, @RequestParam("vin") String vin);
}