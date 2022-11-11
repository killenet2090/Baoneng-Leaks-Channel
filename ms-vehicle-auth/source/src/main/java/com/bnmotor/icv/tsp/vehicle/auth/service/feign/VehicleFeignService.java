package com.bnmotor.icv.tsp.vehicle.auth.service.feign;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.vehicle.auth.model.response.VehicleDeviceVo;
import com.bnmotor.icv.tsp.vehicle.auth.service.feign.fallback.VehicleFeignServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName: VehicleFeignService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author: wuhao1
 * @date: 2020/12/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@FeignClient(value = "ms-device", fallback = VehicleFeignServiceFallbackFactory.class)
public interface VehicleFeignService {

    /**
     * 车辆设备接口，获取车辆绑定的设备
     * @param vin
     * @param deviceType
     * @return
     */
    @GetMapping("/inner/vehDevice/device")
    RestResponse<List<VehicleDeviceVo>> listDevice(@RequestParam("vin") String vin,
                                                   @RequestParam(value = "deviceType", required = false) Integer deviceType);
}
