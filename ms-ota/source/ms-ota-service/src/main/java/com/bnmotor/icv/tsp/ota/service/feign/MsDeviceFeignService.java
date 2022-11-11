package com.bnmotor.icv.tsp.ota.service.feign;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.model.resp.feign.DrivingLicPlateVo;
import com.bnmotor.icv.tsp.ota.model.resp.feign.VehAllLevelVo;
import com.bnmotor.icv.tsp.ota.model.resp.feign.VehicleOrgRelationVo;
import com.bnmotor.icv.tsp.ota.service.feign.fallback.MsDeviceFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName: MsDeviceFeignService
 * @Description: 调用设备信息服务接口
 * @author: xuxiaochang1
 * @date: 2020/9/8
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@FeignClient(value = "ms-device", fallback = MsDeviceFeignFallbackFactory.class)
public interface MsDeviceFeignService {
    /**
     * 查询用户设备信息
     *
     * @param vin
     * @return
     */
    @GetMapping("/inner/vehicle/vehicle")
    RestResponse getVehicle(@RequestParam(value = "vin") String vin);

    @GetMapping("/inner/vehicle/getDrivingLicPlate")
    RestResponse<DrivingLicPlateVo> getDrivingLicPlate(@RequestParam(value = "vin") String vin);

    @GetMapping("/inner/vehicle/orgRelation")
    RestResponse<VehicleOrgRelationVo> getOrgRelationById(@RequestParam(value = "orgId") Long orgId);

    @GetMapping("/inner/vehicle/orgRelations")
    RestResponse<VehAllLevelVo> getOrgRelations();
}