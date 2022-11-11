package com.bnmotor.icv.tsp.apigateway.service.impl;

import com.bnmotor.icv.tsp.apigateway.common.RestResponse;
import com.bnmotor.icv.tsp.apigateway.common.RespCode;
import com.bnmotor.icv.tsp.apigateway.model.entity.AuthCheckRetPo;
import com.bnmotor.icv.tsp.apigateway.service.AuthFeignService;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: AuthFeignFallbackFactory
 * @Description: 鉴权服务降级工厂类
 * @author: huangyun1
 * @date 2020/05/18 *
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class AuthFeignFallbackFactory implements FallbackFactory<AuthFeignService>
{
    private static final Logger Logger = LoggerFactory.getLogger(AuthFeignFallbackFactory.class);

    @Override
    public AuthFeignService create(Throwable throwable) {

        if(Logger.isInfoEnabled()) {
            Logger.info("fallback; reason was: {}", throwable.getMessage());
        }

        return new AuthFeignService() {
            @Override
            public RestResponse<AuthCheckRetPo> check(String servicePath, String authToken) throws Exception {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getCode());
            }
        };
    }
}
