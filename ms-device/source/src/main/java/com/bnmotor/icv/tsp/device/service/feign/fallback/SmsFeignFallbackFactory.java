package com.bnmotor.icv.tsp.device.service.feign.fallback;

import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.model.request.feign.TemplateMsgDto;
import com.bnmotor.icv.tsp.device.service.feign.SmsFeignService;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: PushFeignFallbackFactory
 * @Description: 消息推送服务降级工厂类
 * @author: huangyun1
 * @date 2020/05/18 *
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class SmsFeignFallbackFactory implements FallbackFactory<SmsFeignService>
{
    private static final Logger Logger = LoggerFactory.getLogger(SmsFeignFallbackFactory.class);


    @Override
    public SmsFeignService create(Throwable throwable) {
        if(Logger.isInfoEnabled()) {
            Logger.info("fallback; reason was: {}", throwable.getMessage());
        }

        return new SmsFeignService() {
            @Override
            public RestResponse sendByTemplate(TemplateMsgDto templateMsgDto) {
                return new RestResponse(null, RespCode.SERVER_EXECUTE_ERROR.getDescription(), RespCode.SERVER_EXECUTE_ERROR.getValue());
            }
        };
    }
}
