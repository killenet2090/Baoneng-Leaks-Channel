package com.bnmotor.icv.tsp.ble.service.feign.fallback;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ble.common.RespCode;
import com.bnmotor.icv.tsp.ble.model.request.feign.JpushMsgDto;
import com.bnmotor.icv.tsp.ble.service.feign.PushFeignService;
import feign.hystrix.FallbackFactory;

/**
 * @ClassName: PushFeignFallbackFactory
 * @Description: 极光推送feign调用异常接口
 * @author: shuqi1
 * @date: 2020/6/8
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class PushFeignFallbackFactory implements FallbackFactory<PushFeignService> {
    @Override
    public PushFeignService create(Throwable throwable) {
        return new PushFeignService(){
            @Override
            public RestResponse sendMessage(JpushMsgDto jpushMsgDto) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }

            @Override
            public RestResponse sendNotification(JpushMsgDto jpushMsgDto) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }
        };
    }
}
