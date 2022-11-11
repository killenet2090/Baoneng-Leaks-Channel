package com.bnmotor.icv.tsp.device.service.feign;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.model.request.feign.*;
import com.bnmotor.icv.tsp.device.model.response.vehBind.VehicleUserVo;
import com.bnmotor.icv.tsp.device.model.response.feign.ExistVo;
import com.bnmotor.icv.tsp.device.model.response.feign.GetDeviceInfoVo;
import com.bnmotor.icv.tsp.device.model.response.vehDetail.MgtUserVo;
import com.bnmotor.icv.tsp.device.service.feign.fallback.UserFeignServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: IUserAuthFeighService
 * @Description: 基于feign调用鉴权服务，用于实现注册后自动登录，验证码发送和验证等操作
 * @author: zhangwei2
 * @date: 2020/5/25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@FeignClient(value = "ms-tsp-user", fallback = UserFeignServiceFallbackFactory.class)
public interface UserFeignService {

    @GetMapping(value = "/inner/userVeh/getUserInfo")
    RestResponse<MgtUserVo> getUserInfo(@RequestParam("vin") String vin);

    @PostMapping(value = "/inner/userVeh/getUserInfoList", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<MgtUserVo> getUserInfoList(@RequestBody MgtUserVo mgtUserVo);

    @PostMapping(value = "/inner/userVeh/getUserInfoList/vins", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<MgtUserVo> getUserInfoList(@RequestBody List<String> vins);

    @GetMapping(value = "/v1/userVeh/listVehicleUser")
    RestResponse<List<VehicleUserVo>> listVehicleUser(@RequestParam("vin") String vin);

    /**
     * 检测手机是否已注册
     */
    @GetMapping(value = "/v1/user/checkAccount")
    RestResponse<ExistVo> checkAccount(@RequestParam("account") String phone);

    /**
     * 用户关联车辆
     */
    @PostMapping(value = "/v1/vehicleBind/relationVehicle", consumes = MediaType.APPLICATION_JSON_VALUE)
    RestResponse relationVehicle(@RequestBody UserVehicleRelationDto relationDto);

    /**
     * 更新用户证件基本信息
     */
    @PostMapping(value = "/v1/user/identification/updateIdentificationBaseInfo", consumes = MediaType.APPLICATION_JSON_VALUE)
    RestResponse updateIdentificationBaseInfo(@RequestBody UpdateUserIdentificationDto updateUserIdentificationDto, @RequestHeader("uid") Long uid);

    /**
     * 将注销蓝牙钥匙放入任务
     */
    @PostMapping(value = "/v1/vehicleBind/putDelBlueKeyToTask", consumes = MediaType.APPLICATION_JSON_VALUE)
    RestResponse putDelBlueKeyToTask(@RequestBody DelBlueKeyCallbackDto delBlueKeyCallbackDto);

    /**
     * 查询用户设备信息
     */
    @GetMapping("/v1/deviceInfo/get")
    RestResponse<GetDeviceInfoVo> getDeviceInfo(@RequestHeader("uid") String uid);

    /**
     * 判断车辆是否已绑定
     */
    @GetMapping("/v1/userVeh/checkVehicleHasBind")
    RestResponse<Boolean> checkVehicleHasBind(@RequestHeader("uid") Long uid, @RequestParam("vin") String vin);

    @GetMapping("/v1/userVeh/updateVehInfo")
    RestResponse updateVehInfo(@RequestBody UpdateVehInfoDto updateVehInfoDto);

    /**
     * 根据vin号查询极光推送id
     * @param vin
     * @return
     */
    @GetMapping("/inner/user/app/device")
    RestResponse<GetDeviceInfoVo> device(@RequestParam("vin") String vin);
}
