//package com.bnmotor.icv.tsp.ota.service.v2;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.bnmotor.icv.adam.web.rest.RestResponse;
//import com.bnmotor.icv.tsp.ota.model.req.feign.VehicleRegion;
//import com.bnmotor.icv.tsp.ota.service.feign.VehicleRegionFeignService;
//
//import lombok.extern.slf4j.Slf4j;
//
///**
// * @ClassName: VehicleRegionService.java VehicleRegionService
// * @Description: 车辆地区服务 维护车辆所在地区的服务
// * @author E.YanLonG
// * @since 2020-12-1 11:52:11
// * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
// */
//@Component
//@Slf4j
//public class VehicleRegionService {
//
//	@Autowired
//	VehicleRegionFeignService vehicleRegionFeignService;
//	/**
//	 * 根据vin查询车辆所在区域
//	 * @param vin
//	 */
//	public void select(String vin) {
//		
//	}
//	
//	public VehicleRegion call(VehicleRegion vehicleRegion ) {
//		VehicleRegion vehicleRegion0 = null;
//		RestResponse<VehicleRegion> response = vehicleRegionFeignService.queryVehicleRegion(vehicleRegion);
//		if ("0000".equals(response.getRespCode())) {
//			vehicleRegion0 = response.getRespData();
//		}
//		return vehicleRegion0;
//	}
//
//}