package com.bnmotor.icv.tsp.ble.service.feign.fallback;

import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ble.model.request.feign.TemplateMsgDto;
import com.bnmotor.icv.tsp.ble.service.feign.SmsFeignService;
import feign.hystrix.FallbackFactory;

/**
 * @ClassName: SmsFeignFallbackFactory
 * @Description: 短信feign调用异常接口
 * @author: shuqi1
 * @date: 2020/6/8
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class SmsFeignFallbackFactory implements FallbackFactory<SmsFeignService> {
    @Override
    public SmsFeignService create(Throwable throwable) {
        return new SmsFeignService() {
            @Override
            public RestResponse sendByTemplate(TemplateMsgDto templateMsgDto) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }
        };
    }
}
