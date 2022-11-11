package com.bnmotor.icv.tsp.ota.service.feign;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.model.resp.feign.GetDeviceInfoVo;
import com.bnmotor.icv.tsp.ota.model.resp.feign.VehicleUserVo;
import com.bnmotor.icv.tsp.ota.service.feign.fallback.TspUserFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName: TspUserFeignService
 * @Description: 调用用户服务接口
 * @author: xuxiaochang1
 * @date: 2020/9/8
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@FeignClient(value = "ms-tsp-user", fallback = TspUserFeignFallbackFactory.class)
public interface TspUserFeignService {
    /**
     * 查询用户设备信息
     *
     * @param uid
     * @return
     */
    @GetMapping("/v1/deviceInfo/get")
    RestResponse<GetDeviceInfoVo> getDeviceInfo(@RequestHeader("uid") String uid);

    /**
     * 根据vin号查询极光推送id
     * @param vin
     * @return
     */
    @GetMapping("/inner/user/app/device")
    RestResponse<GetDeviceInfoVo> device(@RequestParam("vin") String vin);

    /**
     * 根据vin查询用户信息
     * @param vin
     * @return
     */
    @GetMapping("/inner/userVeh/getUserInfo")
    RestResponse<VehicleUserVo> getUserInfo(@RequestParam("vin") String vin);
}