package com.bnmotor.icv.tsp.operation.veh.service.feign.fallback;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.operation.veh.model.request.EditVehLabelDto;
import com.bnmotor.icv.tsp.operation.veh.model.request.QueryVehProjectDto;
import com.bnmotor.icv.tsp.operation.veh.model.request.QueryVehicleDto;
import com.bnmotor.icv.tsp.operation.veh.model.response.*;
import com.bnmotor.icv.tsp.operation.veh.service.feign.VehicleFeignService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author zhoulong1
 * @ClassName: VehicleFeignServiceFallbackFactory
 * @Description: 车辆服务降级工厂
 * @since: 2020/7/18
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
public class VehicleFeignServiceFallbackFactory implements FallbackFactory<VehicleFeignService> {


    @Override
    public VehicleFeignService create(Throwable throwable) {
        return new VehicleFeignService() {
            @Override
            public RestResponse getVehicleDetail(String vin) {
                return genServiceInvokeResp();
            }

            @Override
            public RestResponse<Page<VehProjectStatisticsVo>> queryVehicleProjects(QueryVehProjectDto projectDto) {
                return genServiceInvokeResp();
            }

            @Override
            public RestResponse<List<VehProjectDetailVo>> queryVehicleModels(String projectCode,Integer vehType,String searchKey) {
                return genServiceInvokeResp();
            }

            @Override
            public RestResponse<List<VehicleDeviceVo>> queryDevice(String vin, Integer deviceType) {
                return null;
            }

            @Override
            public RestResponse<Page<VehicleVo>> queryVehicles(QueryVehicleDto vehicleDto)
            {
                return genServiceInvokeResp();
            }

            @Override
            public RestResponse editVehLabel(EditVehLabelDto labelDto)
            {
                return genServiceInvokeResp();
            }

            @Override
            public RestResponse<List<UserBindDetailVo>> getVehicles(List<String> vins) {
                return genServiceInvokeResp();
            }
        };
    }

    private RestResponse genServiceInvokeResp() {
        return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getDescription(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
    }
}
