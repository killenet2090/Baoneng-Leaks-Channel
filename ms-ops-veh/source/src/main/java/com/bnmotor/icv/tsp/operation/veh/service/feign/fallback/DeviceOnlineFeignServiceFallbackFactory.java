package com.bnmotor.icv.tsp.operation.veh.service.feign.fallback;

import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.operation.veh.service.feign.DeviceOnlineFeignService;
import feign.hystrix.FallbackFactory;

/**
 * @author zhoulong1
 * @ClassName: DeviceOnlineFeignServiceFallbackFactory
 * @Description: 车辆在线服务降级工厂
 * @since: 2020/7/23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class DeviceOnlineFeignServiceFallbackFactory implements FallbackFactory<DeviceOnlineFeignService> {
    @Override
    public DeviceOnlineFeignService create(Throwable throwable) {
        return vins -> genServiceInvokeResp();
    }

    private RestResponse genServiceInvokeResp() {
        return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getDescription(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
    }
}
