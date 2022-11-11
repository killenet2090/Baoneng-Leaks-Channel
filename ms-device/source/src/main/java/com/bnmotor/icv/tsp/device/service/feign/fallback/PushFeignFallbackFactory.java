package com.bnmotor.icv.tsp.device.service.feign.fallback;

import com.bnmotor.icv.tsp.device.service.feign.PushFeignService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: PushFeignFallbackFactory
 * @Description: 消息推送服务降级工厂类
 * @author: huangyun1
 * @date 2020/05/18 *
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
public class PushFeignFallbackFactory implements FallbackFactory<PushFeignService> {
    @Override
    public PushFeignService create(Throwable throwable) {
        if (log.isInfoEnabled()) {
            log.info("fallback; reason was: {}", throwable.getMessage());
        }

        return jpushMsgDTO -> null;
    }
}
