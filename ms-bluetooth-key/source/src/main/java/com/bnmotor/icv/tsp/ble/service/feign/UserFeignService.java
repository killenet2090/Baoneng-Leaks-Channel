package com.bnmotor.icv.tsp.ble.service.feign;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ble.model.request.feign.HuCallBack;
import com.bnmotor.icv.tsp.ble.model.request.feign.TokenCheck;
import com.bnmotor.icv.tsp.ble.model.request.feign.TokenDto;
import com.bnmotor.icv.tsp.ble.model.request.feign.VehBindBack;
import com.bnmotor.icv.tsp.ble.model.response.user.UserCheckVo;
import com.bnmotor.icv.tsp.ble.model.response.user.UserPhoneVo;
import com.bnmotor.icv.tsp.ble.model.response.user.UserVo;
import com.bnmotor.icv.tsp.ble.service.feign.fallback.UserFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;


@FeignClient(value = "ms-tsp-user",fallback = UserFeignFallbackFactory.class/*,configuration = FeignConfig.class*/)

@Service
public interface UserFeignService {
    @RequestMapping(value = "/v1/deviceInfo/get", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,method = RequestMethod.GET)
    RestResponse<UserPhoneVo> queryUserPhoneByNum(@RequestHeader(name = "uid") String uid);

    @GetMapping(value = "/v1/user/get")
    RestResponse<UserVo> getUserInfo(@RequestHeader("uid") String uid);

    @GetMapping(value = "/v1/user/checkAccount")
    RestResponse<UserCheckVo> checkUserAccount(@RequestParam(name="account") String account);

    @GetMapping(value = "/v1/user")
    RestResponse<UserVo> queryUserInfo(@RequestParam(name="account") String account, @RequestParam(name="type") String type, @RequestHeader(name="uid") String uid);

    @GetMapping(value = "/v1/deviceInfo/getByPhone")
    RestResponse<UserPhoneVo> queryUserPushId(@RequestParam(name="phone") String phone, @RequestHeader(name="uid") String uid);

    @PostMapping(value = "/inner/useropen/service/token/check")
    RestResponse verifyServicePwd(@RequestBody TokenCheck tokenCheck, @RequestHeader(name="uid") String uid);

    @PostMapping(value = "/inner/userVehAuthorization/ble/confirm")
    RestResponse updateAuthStatus(HuCallBack huCallBack, @RequestHeader(name="uid") String uid);

    @PostMapping(value = "/inner/useropen/service/token/valid")
    RestResponse tokenValid(TokenDto tokenDto, @RequestHeader(name="uid") String uid);

    @PostMapping(value = "/v1/vehicleBind/delBlueKeyCallback")
    RestResponse vehBindCallback(VehBindBack vehBindBack);
}
