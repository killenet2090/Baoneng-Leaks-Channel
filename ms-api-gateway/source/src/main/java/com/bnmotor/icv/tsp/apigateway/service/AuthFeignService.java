package com.bnmotor.icv.tsp.apigateway.service;

import com.bnmotor.icv.tsp.apigateway.common.RestResponse;
import com.bnmotor.icv.tsp.apigateway.model.entity.AuthCheckRetPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.bnmotor.icv.tsp.apigateway.service.impl.*;

/**
 * @ClassName: AuthFeignService
 * @Description: 调用鉴权服务接口
 * @author: huangyun1
 * @date: 2020/5/13
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@FeignClient(value = "ms-tsp-user-auth", fallback = AuthFeignFallbackFactory.class)
@RequestMapping("/v1/auth")
public interface AuthFeignService {
    /**
     * 校验token的有效性
     * @param servicePath
     * @param authToken
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/check")
    public RestResponse<AuthCheckRetPo> check(@RequestParam("servicePath") String servicePath, @RequestHeader("Authorization") String authToken) throws Exception;

}