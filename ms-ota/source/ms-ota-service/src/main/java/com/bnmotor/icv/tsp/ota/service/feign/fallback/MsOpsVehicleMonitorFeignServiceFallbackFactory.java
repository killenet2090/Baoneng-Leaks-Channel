package com.bnmotor.icv.tsp.ota.service.feign.fallback;

import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.model.req.feign.VehicleRegionPage;
import com.bnmotor.icv.tsp.ota.model.req.feign.VehicleRegionStateQuery;
import com.bnmotor.icv.tsp.ota.service.feign.MsOpsVehicleMonitorFeignService;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: MsOpsVehicleMonitorFeignServiceFallbackFactory.java MsOpsVehicleMonitorFeignServiceFallbackFactory
 * @Description: 查询车辆区域
 * @author E.YanLonG
 * @since 2020-12-11 14:40:11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
public class MsOpsVehicleMonitorFeignServiceFallbackFactory implements FallbackFactory<MsOpsVehicleMonitorFeignService> {

	@Override
	public MsOpsVehicleMonitorFeignService create(Throwable cause) {
		return new MsOpsVehicleMonitorFeignService() {
			@Override
			public RestResponse<VehicleRegionPage> batchlist(VehicleRegionStateQuery vehicleRegionStateQuery) {
				return new RestResponse<>(null, RespCode.SERVER_EXECUTE_ERROR.getDescription(),
						RespCode.SERVER_EXECUTE_ERROR.getValue());
			}
		};
	}

}
