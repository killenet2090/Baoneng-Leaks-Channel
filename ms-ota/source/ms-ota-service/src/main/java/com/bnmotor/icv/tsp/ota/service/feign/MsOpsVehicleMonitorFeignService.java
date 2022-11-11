package com.bnmotor.icv.tsp.ota.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.model.req.feign.VehicleRegionPage;
import com.bnmotor.icv.tsp.ota.model.req.feign.VehicleRegionStateQuery;
import com.bnmotor.icv.tsp.ota.service.feign.fallback.MsOpsVehicleMonitorFeignServiceFallbackFactory;

/**
 * 
 * @ClassName: T.java T
 * @Description: 
 * @author E.YanLonG
 * @since 2020-12-8 21:09:46
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@FeignClient(value = "ms-ops-veh-monitor-service", fallback = MsOpsVehicleMonitorFeignServiceFallbackFactory.class)
public interface MsOpsVehicleMonitorFeignService {

	@PostMapping("/internal/queryBatchVehicle")
    RestResponse<VehicleRegionPage> batchlist(@RequestBody VehicleRegionStateQuery vehicleRegionStateQuery);

}