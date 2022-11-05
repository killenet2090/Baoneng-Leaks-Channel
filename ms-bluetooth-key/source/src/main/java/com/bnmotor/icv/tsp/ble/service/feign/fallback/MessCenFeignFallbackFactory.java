package com.bnmotor.icv.tsp.ble.service.feign.fallback;

import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ble.model.request.feign.MessageCenterDto;
import com.bnmotor.icv.tsp.ble.service.feign.MessCenFeignService;
import feign.hystrix.FallbackFactory;

/**
 * @ClassName: DeviceFeignFallbackFactory
 * @Description: 车辆服务feign调用异常接口
 * @author: shuqi1
 * @date: 2020/6/8
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class MessCenFeignFallbackFactory implements FallbackFactory<MessCenFeignService> {
    @Override
    public MessCenFeignService create(Throwable throwable) {
        return new MessCenFeignService(){
            @Override
            public RestResponse<String> singleSend(MessageCenterDto messageCenterDto) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }
        };
    }
}
