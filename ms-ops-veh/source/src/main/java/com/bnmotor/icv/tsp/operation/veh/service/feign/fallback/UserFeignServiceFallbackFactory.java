package com.bnmotor.icv.tsp.operation.veh.service.feign.fallback;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.operation.veh.model.request.VehicleDto;
import com.bnmotor.icv.tsp.operation.veh.model.response.MgtUserVo;
import com.bnmotor.icv.tsp.operation.veh.model.response.UserBindDetailVo;
import com.bnmotor.icv.tsp.operation.veh.model.response.VehicleBindVo;
import com.bnmotor.icv.tsp.operation.veh.service.feign.UserFeighService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhoulong1
 * @ClassName: UserFeignServiceFallbackFactory
 * @Description: user服务降级工厂类
 * @since: 2020/7/17
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
public class UserFeignServiceFallbackFactory implements FallbackFactory<UserFeighService> {

    @Override
    public UserFeighService create(Throwable throwable) {
        return new UserFeighService() {
            @Override
            public RestResponse<MgtUserVo> getUserInfo(String vin) {
                return genServiceInvokeResp();
            }

            @Override
            public RestResponse<List<VehicleBindVo>> listBinding(String vin) {
                return genServiceInvokeResp();
            }

            @Override
            public RestResponse<Page<UserBindDetailVo>> listVehicle(VehicleDto vehicleDto) {
                return genServiceInvokeResp();
            }
            @Override
            public List<MgtUserVo> getUserInfoList(MgtUserVo mgtUserVo) {
                return new ArrayList<>();
            }

            @Override
            public List<MgtUserVo> getUserInfoList(List<String> vins) {
                return new ArrayList<>();
            }
        };
    }

    private RestResponse genServiceInvokeResp() {
        return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getDescription(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
    }
}
