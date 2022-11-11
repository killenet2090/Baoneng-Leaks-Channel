package com.bnmotor.icv.tsp.ota.service.feign.fallback;

import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.service.feign.MsOperationTagsFeignService;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: MsOperationTagsFeignFallbackFactory
 * @Description: 标签服务降级工厂类（可考虑由web后台直接调用）
 * @author: xuxiaochang1
 * @date 2020/05/18 *
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class MsOperationTagsFeignFallbackFactory implements FallbackFactory<MsOperationTagsFeignService>
{
    private static final Logger Logger = LoggerFactory.getLogger(MsOperationTagsFeignFallbackFactory.class);

    @Override
    public MsOperationTagsFeignService create(Throwable throwable) {

        if(Logger.isInfoEnabled()) {
            Logger.info("fallback; reason was: {}", throwable.getMessage());
        }

        return (businessId, categoryId, tagIds) -> new RestResponse(null, RespCode.SERVER_EXECUTE_ERROR.getDescription(), RespCode.SERVER_EXECUTE_ERROR.getValue());
    }
}
