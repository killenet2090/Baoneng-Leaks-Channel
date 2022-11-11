package com.bnmotor.icv.tsp.device.controller.inner;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.common.VehLocalCache;
import com.bnmotor.icv.tsp.device.model.request.vehModel.VehModelListQueryDto;
import com.bnmotor.icv.tsp.device.model.response.vehModel.VehicleModelVo;
import com.bnmotor.icv.tsp.device.service.IOrgRelationService;
import com.bnmotor.icv.tsp.device.service.IVehicleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: InnerOrgRealtionController
 * @Description: 车型配置层级关系表
 * @author: zhangwei2
 * @date: 2020/12/4
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/inner/vehicle")
@Api(value = "车型配置层级关系接口,用于查询车型，配置等信息", tags = {"车型配置层级关系相关接口"})
@Slf4j
public class InnerOrgRealtionController {
    @Autowired
    private IOrgRelationService relationService;
    @Autowired
    private IVehicleService vehicleService;
    @Resource
    private VehLocalCache vehLocalCache;

    @GetMapping("/orgRelations")
    @ApiOperation(value = "获取车辆组织关系平铺列表,所有层级")
    public ResponseEntity getOrgRelations() {
        return RestResponse.ok(relationService.getVehAllLevelVo());
    }

    @GetMapping("/orgRelation/vins")
    @ApiOperation(value = "根据车辆配置获取vin列表")
    public ResponseEntity getVinsByOrg(@RequestParam(value = "orgId") Long orgId) {
        return RestResponse.ok(vehicleService.listVinsByOrgId(orgId));
    }

    @GetMapping("/orgRelation")
    @ApiOperation(value = "根据组织id获取组织关系")
    public ResponseEntity getOrgRelationById(@RequestParam(value = "orgId") Long orgId) {
        return RestResponse.ok(vehLocalCache.getOrgById(orgId));
    }

    @PostMapping("/getVehModelList")
    @ApiOperation(value = "获取满足条件的车型列表")
    public ResponseEntity getVehModelList(@RequestBody VehModelListQueryDto dto) {
        List<VehicleModelVo> vehModelList = relationService.getVehModelList(dto);
        return RestResponse.ok(vehModelList);
    }
}
