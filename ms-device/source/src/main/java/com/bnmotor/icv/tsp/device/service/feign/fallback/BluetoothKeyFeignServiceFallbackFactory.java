package com.bnmotor.icv.tsp.device.service.feign.fallback;

import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.model.request.feign.BleDeviceDelDto;
import com.bnmotor.icv.tsp.device.model.request.feign.UpdateVehInfoBluDto;
import com.bnmotor.icv.tsp.device.service.feign.BluetoothKeyFeignService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: BluetoothKeyFeignServiceFallbackFactory
 * @Description: 蓝牙钥匙服务降级工厂类
 * @author: zhangwei2
 * @date 2020/05/18 *
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
public class BluetoothKeyFeignServiceFallbackFactory implements FallbackFactory<BluetoothKeyFeignService> {

    @Override
    public BluetoothKeyFeignService create(Throwable throwable) {
        log.info("fallback; reason was: {}", throwable.getMessage());
        return new BluetoothKeyFeignService() {
            @Override
            public RestResponse delDeviceBlekeyApp(String uid, String projectId, BleDeviceDelDto bleDeviceDelDto) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getDescription(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }

            @Override
            public RestResponse update(UpdateVehInfoBluDto updateVehInfoBluDto) {
                return null;
            }
        };
    }
}
