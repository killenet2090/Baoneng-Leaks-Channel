package com.bnmotor.icv.tsp.operation.veh.service.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.operation.veh.model.request.VehicleDto;
import com.bnmotor.icv.tsp.operation.veh.model.response.MgtUserVo;
import com.bnmotor.icv.tsp.operation.veh.model.response.UserBindDetailVo;
import com.bnmotor.icv.tsp.operation.veh.model.response.VehicleBindVo;
import com.bnmotor.icv.tsp.operation.veh.service.feign.fallback.UserFeignServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author zhoulong1
 * @ClassName: UserFeighService
 * @Description: feign调用user服务
 * @since: 2020/7/17
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@FeignClient(value = "ms-tsp-user", fallback = UserFeignServiceFallbackFactory.class)
public interface UserFeighService {

    /**
     * 根据vin查询用户信息
     *
     * @param vin 车辆VIN码
     * @return
     */
    @GetMapping(value = "/inner/userVeh/getUserInfo")
    RestResponse<MgtUserVo> getUserInfo(@RequestParam("vin") String vin);

    /**
     * 根据vin历史绑定信息
     *
     * @param vin 车辆VIN码
     * @return
     */
    @GetMapping(value = "/inner/userVeh/bindings")
    RestResponse<List<VehicleBindVo>> listBinding(@RequestParam("vin") String vin);

    /**
     * 根据用户ID，绑定状态查询车辆绑定信息
     *
     * @param vehicleDto
     * @return
     */
    @PostMapping("user/vehicle/list")
    RestResponse<Page<UserBindDetailVo>> listVehicle(@RequestBody VehicleDto vehicleDto);

    /**
     * 获取车辆用户信息
     *
     * @param mgtUserVo
     * @return
     */
    @PostMapping(value = "/inner/userVeh/getUserInfoList", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<MgtUserVo> getUserInfoList(@RequestBody MgtUserVo mgtUserVo);

    /**
     * 根据车架号获取车辆用户信息
     *
     * @param vins
     * @return
     */
    @PostMapping(value = "/inner/userVeh/getUserInfoList/vins", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<MgtUserVo> getUserInfoList(@RequestBody List<String> vins);
}
