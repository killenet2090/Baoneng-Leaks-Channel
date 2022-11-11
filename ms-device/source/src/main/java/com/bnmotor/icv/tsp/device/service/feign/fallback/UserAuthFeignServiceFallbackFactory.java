package com.bnmotor.icv.tsp.device.service.feign.fallback;

import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.model.request.feign.VehLoginDto;
import com.bnmotor.icv.tsp.device.model.response.sim.JwtTokenVo;
import com.bnmotor.icv.tsp.device.service.feign.UserAuthFeighService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @ClassName: UserAuthFeignServiceFallbackFactory
 * @Description: 消息推送服务降级工厂类
 * @author: zhangwei2
 * @date 2020/05/18 *
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
public class UserAuthFeignServiceFallbackFactory implements FallbackFactory<UserAuthFeighService> {

    @Override
    public UserAuthFeighService create(Throwable throwable) {
        log.info("fallback; reason was: {}", throwable.getMessage());
        return new UserAuthFeighService() {
            @Override
            public RestResponse<JwtTokenVo> vehLogin(@RequestBody @Validated VehLoginDto vehLoginDto) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getDescription(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }
        };
    }
}
