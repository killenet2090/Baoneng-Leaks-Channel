package com.bnmotor.icv.tsp.device.service.feign.fallback;

import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.model.request.feign.*;
import com.bnmotor.icv.tsp.device.model.response.vehBind.VehicleUserVo;
import com.bnmotor.icv.tsp.device.model.response.feign.ExistVo;
import com.bnmotor.icv.tsp.device.model.response.feign.GetDeviceInfoVo;
import com.bnmotor.icv.tsp.device.model.response.vehDetail.MgtUserVo;
import com.bnmotor.icv.tsp.device.service.feign.UserFeignService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: UserAuthFeignServiceFallbackFactory
 * @Description: 消息推送服务降级工厂类
 * @author: zhangwei2
 * @date 2020/05/18 *
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
public class UserFeignServiceFallbackFactory implements FallbackFactory<UserFeignService> {

    @Override
    public UserFeignService create(Throwable throwable) {
        log.info("fallback; reason was: {}", throwable.getMessage());
        return new UserFeignService() {
            @Override
            public RestResponse<MgtUserVo> getUserInfo(String vin) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getDescription(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }

            @Override
            public List<MgtUserVo> getUserInfoList(MgtUserVo mgtUserVo) {
                return new ArrayList<>();
            }

            @Override
            public List<MgtUserVo> getUserInfoList(List<String> vins) {
                return null;
            }

            @Override
            public RestResponse<List<VehicleUserVo>> listVehicleUser(String vin) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getDescription(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }

            @Override
            public RestResponse<ExistVo> checkAccount(String phone) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getDescription(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }

            @Override
            public RestResponse relationVehicle(UserVehicleRelationDto relationDto) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getDescription(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }

            @Override
            public RestResponse updateIdentificationBaseInfo(UpdateUserIdentificationDto updateUserIdentificationDto, Long uid) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getDescription(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }

            @Override
            public RestResponse putDelBlueKeyToTask(DelBlueKeyCallbackDto delBlueKeyCallbackDto) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getDescription(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }

            @Override
            public RestResponse<GetDeviceInfoVo> getDeviceInfo(String uid) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getDescription(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }

            @Override
            public RestResponse<Boolean> checkVehicleHasBind(Long uid, String vin) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getDescription(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }

            @Override
            public RestResponse updateVehInfo(UpdateVehInfoDto updateVehInfoDto) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getDescription(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }

            @Override
            public RestResponse<GetDeviceInfoVo> device(String vin) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getDescription(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }
        };
    }
}
