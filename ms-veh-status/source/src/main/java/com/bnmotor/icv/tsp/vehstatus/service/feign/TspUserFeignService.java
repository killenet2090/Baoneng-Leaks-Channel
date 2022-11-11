package com.bnmotor.icv.tsp.vehstatus.service.feign;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.vehstatus.service.feign.fallback.TspUserFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName: TspUserFeignService
 * @Description: 调用用户服务接口
 * @author: huangyun1
 * @date: 2020/5/13
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@FeignClient(value = "ms-tsp-user", fallback = TspUserFeignFallbackFactory.class)
public interface TspUserFeignService {
    /**
     * 检查用户是否拥有车辆权(绑定和授权)
     * @param uid
     * @param vin
     * @return
     */
    @GetMapping("/v1/userVeh/checkUserHasVehicle")
    RestResponse<Boolean> checkUserHasVehicle(@RequestHeader("uid") Long uid, @RequestParam("vin") String vin);

}