package com.bnmotor.icv.tsp.ble.service.feign.fallback;

import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ble.service.feign.DeviceIsOnlineService;
import feign.hystrix.FallbackFactory;
import org.springframework.http.ResponseEntity;

/**
 * @ClassName: DeviceFeignFallbackFactory
 * @Description: 汇聚平台服务feign调用接口，判断车辆是否在线
 * @author: shuqi1
 * @date: 2020/9/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class DeviceOnlineFeignFallbackFactory implements FallbackFactory<DeviceIsOnlineService> {
    @Override
    public DeviceIsOnlineService create(Throwable throwable) {
        return new DeviceIsOnlineService(){
            @Override
            public ResponseEntity<RestResponse<Boolean>> onlineStatus(String vin) {
                return RestResponse.error(RespCode.SERVER_EXECUTE_ERROR);
            }
        };

    }
}
