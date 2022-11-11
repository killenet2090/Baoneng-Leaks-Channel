package com.bnmotor.icv.tsp.operation.veh.service.feign;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.operation.veh.service.feign.fallback.DeviceOnlineFeignServiceFallbackFactory;
import com.bnmotor.icv.tsp.operation.veh.service.feign.fallback.UserFeignServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author zhoulong1
 * @ClassName: DeviceOnlineFeignService
 * @Description: feign调用车辆在线服务
 * @since: 2020/7/23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@FeignClient(value = "octopus-ms-device-online", fallback = DeviceOnlineFeignServiceFallbackFactory.class)
public interface DeviceOnlineFeignService {

    /**
     * 车辆上下线服务
     * @param vins
     * @return
     */
    @PostMapping("octopusMsDeviceOnline/v1/onlineStatus/batch")
    RestResponse onlineStatusBatch(@RequestBody List<String> vins);
}
