package com.bnmotor.icv.tsp.ota.service.feign.fallback;

import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.service.feign.MessageCenterFeignService;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: MessageCenterFeignFallbackFactory
 * @Description: 消息中心降级工厂类
 * @author: huangyun1
 * @date 2020/05/18 *
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class MessageCenterFeignFallbackFactory implements FallbackFactory<MessageCenterFeignService>
{
    private static final Logger Logger = LoggerFactory.getLogger(MessageCenterFeignFallbackFactory.class);

    @Override
    public MessageCenterFeignService create(Throwable throwable) {

        if(Logger.isInfoEnabled()) {
            Logger.info("fallback; reason was: {}", throwable.getMessage());
        }
        return (commonVO, reqVO) -> new RestResponse(null, RespCode.SERVER_EXECUTE_ERROR.getDescription(), RespCode.SERVER_EXECUTE_ERROR.getValue());
    }
}
