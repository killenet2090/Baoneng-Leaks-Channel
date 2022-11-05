package com.bnmotor.icv.tsp.ble.service.feign.fallback;

import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ble.model.request.feign.HuCallBack;
import com.bnmotor.icv.tsp.ble.model.request.feign.TokenCheck;
import com.bnmotor.icv.tsp.ble.model.request.feign.TokenDto;
import com.bnmotor.icv.tsp.ble.model.request.feign.VehBindBack;
import com.bnmotor.icv.tsp.ble.model.response.user.UserCheckVo;
import com.bnmotor.icv.tsp.ble.model.response.user.UserPhoneVo;
import com.bnmotor.icv.tsp.ble.model.response.user.UserVo;
import com.bnmotor.icv.tsp.ble.service.feign.UserFeignService;
import feign.hystrix.FallbackFactory;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: UserFeignFallbackFactory
 * @Description: 用户feign调用异常接口
 * @author: shuqi1
 * @date: 2020/6/8
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class UserFeignFallbackFactory implements FallbackFactory<UserFeignService> {
    private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(UserFeignFallbackFactory.class);
    @Override
    public UserFeignService create(Throwable throwable) {
        return new UserFeignService(){
            @Override
            public RestResponse<UserPhoneVo> queryUserPhoneByNum(String uid) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }

            @Override
            public RestResponse<UserVo> getUserInfo(String uid) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }

            @Override
            public RestResponse<UserCheckVo> checkUserAccount(String account) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }

            @Override
            public RestResponse<UserVo> queryUserInfo(String account, String type, String uid) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }

            @Override
            public RestResponse<UserPhoneVo> queryUserPushId(String phone, String uid) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }

            @Override
            public RestResponse verifyServicePwd(TokenCheck servicePwd, String uid) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }

            @Override
            public RestResponse updateAuthStatus(HuCallBack huCallBack, String uid) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }

            @Override
            public RestResponse tokenValid(TokenDto tokenDto, String uid) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }

            @Override
            public RestResponse vehBindCallback(VehBindBack vehBindBack) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }
        };

    }
}
