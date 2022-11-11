package com.bnmotor.icv.tsp.device.service.feign;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.service.feign.fallback.DeviceOnlineFeignFallbackFactory;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName: DeviceOnlineFeignService
 * @Description: 车辆是在线服务接口
 * @author: huangyun1
 * @date: 2020/5/13
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@FeignClient(name = "octopus-ms-device-online", fallback = DeviceOnlineFeignFallbackFactory.class)
public interface DeviceOnlineFeignService {
    /**
     * 获取指定VIN码的车辆在线状态
     */
    @GetMapping("/octopusMsDeviceOnline/v1/onlineStatus")
    @ApiOperation(value = "获取单车在线状态")
    ResponseEntity<RestResponse<Boolean>> onlineStatus(@RequestParam(value = "vin") String vin);
}
