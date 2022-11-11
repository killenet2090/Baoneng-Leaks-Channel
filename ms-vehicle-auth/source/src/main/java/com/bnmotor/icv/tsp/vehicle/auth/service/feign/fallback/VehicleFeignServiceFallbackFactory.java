package com.bnmotor.icv.tsp.vehicle.auth.service.feign.fallback;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.vehicle.auth.model.response.VehicleDeviceVo;
import com.bnmotor.icv.tsp.vehicle.auth.service.feign.VehicleFeignService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @ClassName: VehicleFeignServiceFallbackFactory
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author: wuhao1
 * @date: 2020/12/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Slf4j
public class VehicleFeignServiceFallbackFactory implements FallbackFactory<VehicleFeignService> {

    @Override
    public VehicleFeignService create(Throwable throwable) {
        return new VehicleFeignService() {

            @Override
            public RestResponse<List<VehicleDeviceVo>> listDevice(String vin, Integer deviceType) {
                return null;
            }
        };
    }
}
