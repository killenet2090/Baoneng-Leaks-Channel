package com.bnmotor.icv.tsp.device.service.feign;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.model.request.feign.VehLoginDto;
import com.bnmotor.icv.tsp.device.model.response.sim.JwtTokenVo;
import com.bnmotor.icv.tsp.device.service.feign.fallback.UserAuthFeignServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @ClassName: UserAuthFeighService
 * @Description: 基于feign调用鉴权服务，用于实现注册后自动登录，验证码发送和验证等操作
 * @author: huangyun1
 * @date: 2020/11/19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@FeignClient(value = "ms-tsp-user-auth", fallback = UserAuthFeignServiceFallbackFactory.class)
public interface UserAuthFeighService {
    /**
     * 车机登录
     */
    @PostMapping(value = "/v1/auth/vehLogin")
    RestResponse<JwtTokenVo> vehLogin(VehLoginDto vehLoginDto);
}
