package com.bnmotor.icv.tsp.device.controller.inner;

import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.data.mysql.DataAccessContext;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.common.ReqContext;
import com.bnmotor.icv.tsp.device.common.VehLocalCache;
import com.bnmotor.icv.tsp.device.common.enums.veh.VehicleType;
import com.bnmotor.icv.tsp.device.model.entity.VehiclePo;
import com.bnmotor.icv.tsp.device.model.request.vehicle.QueryVehicleDto;
import com.bnmotor.icv.tsp.device.model.request.vehicle.VehicleCerStatusUpdateDto;
import com.bnmotor.icv.tsp.device.model.response.View.VehicleLevel;
import com.bnmotor.icv.tsp.device.model.response.vehDetail.VehInfoVo;
import com.bnmotor.icv.tsp.device.model.response.vehicle.DrivingLicPlateVo;
import com.bnmotor.icv.tsp.device.model.response.vehicle.OrgLocalCacheVo;
import com.bnmotor.icv.tsp.device.model.response.vehicle.VehicleVo;
import com.bnmotor.icv.tsp.device.service.IVehicleService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: VehicleController
 * @Description: 车辆相关管理后台接口, 提供管理人员进行车辆信息查询和进行车辆信息维护接口
 * @author: zhangwei2
 * @date: 2020/6/30
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/inner/vehicle")
@Api(value = "车辆相关管理接口,主要包括车辆入库,车辆生命周期管理,提供管理人员进行车辆信息查询和进行车辆信息维护接口", tags = {"车辆管理相关接口"})
@Slf4j
public class InnerVehicleController {
    @Autowired
    private IVehicleService vehicleService;
    @Autowired
    private VehLocalCache vehLocalCache;

    @PostMapping("/list")
    @ApiOperation(value = "车辆列表查询")
    public ResponseEntity queryVehicles(@RequestBody QueryVehicleDto vehicleDto) {
        if (ReqContext.getUid() == null && vehicleDto.getUserID() == null) {
            return RestResponse.ok(Collections.EMPTY_LIST);
        }

        if (ReqContext.getUid() == null && vehicleDto.getUserID() != null) {
            DataAccessContext.setUid(Long.valueOf(vehicleDto.getUserID()));
        }

        return RestResponse.ok(vehicleService.list(vehicleDto));
    }

    @GetMapping(value = "/detail")
    @ApiOperation(value = "车辆详情接口,用于获取车辆基本信息,车辆认证,车辆设备绑定信息,标签等信息")
    public ResponseEntity getVehicleDetail(@RequestParam(value = "vin") String vin) {
        return RestResponse.ok(vehicleService.getVehDetail(vin));
    }

    @GetMapping(value = "/getVehInfo")
    @ApiOperation(value = "cmp系统使用，获取车辆相关信息")
    public ResponseEntity getVehInfo(@RequestParam(value = "vin") String vin) {
        VehInfoVo vo=vehicleService.getVehInfo(vin);
        return RestResponse.ok(vo);
    }

    @GetMapping(value = "/vehicles")
    @JsonView(VehicleLevel.class)
    @ApiOperation(value = "根据vins集合查询车辆信息")
    public ResponseEntity vehicleLevel(@RequestParam("vins") List<String> vins) {
        List<VehiclePo> vehicles = vehicleService.listVehicle(vins);
        List<VehicleVo> vehicleVos = vehicles.stream().map(vehiclePo -> {
            VehicleVo vehicleVo = new VehicleVo();
            BeanUtils.copyProperties(vehiclePo, vehicleVo);
            vehicleVo.setId(String.valueOf(vehiclePo.getId()));
            vehicleVo.setLifeCircle(vehiclePo.getVehLifecircle());
            vehicleVo.setOrgId(String.valueOf(vehiclePo.getOrgId()));
            OrgLocalCacheVo cacheVo = vehLocalCache.getOrgById(vehiclePo.getOrgId());
            if (cacheVo != null) {
                vehicleVo.setYearStyleName(cacheVo.getYearStyleName());
                vehicleVo.setVehConfigName(cacheVo.getConfigName());
            }

            VehicleType type = VehicleType.valueOf(vehiclePo.getVehType());
            vehicleVo.setVehType(type != null ? type.getDesp() : null);
            return vehicleVo;
        }).collect(Collectors.toList());

        return RestResponse.ok(vehicleVos);
    }

    @GetMapping(value = "/vehicle")
    @JsonView(VehicleLevel.class)
    @ApiOperation(value = "根据车辆vin快速查询车辆基本信息")
    public ResponseEntity getVehicle(@RequestParam(value = "vin") String vin) {
        return RestResponse.ok(vehicleService.getVehicleVo(vin));
    }

    @GetMapping(value = "/getDrivingLicPlate")
    @ApiOperation(value = "车牌号码查询")
    public ResponseEntity getDrivingLicPlate(@RequestParam(value = "vin") String vin) {
        VehicleVo detailVo = vehicleService.getVehicleVo(vin);
        DrivingLicPlateVo plateVo = new DrivingLicPlateVo();
        plateVo.setDrivingLicPlate(detailVo.getDrivingLicPlate());
        return RestResponse.ok(plateVo);
    }

    @GetMapping("/asynQuery")
    @ApiOperation(value = "异步查询车辆,查询结果通过mq推送到指定的队列")
    public ResponseEntity asynQuery(@RequestParam(value = "uid") Long uid,
                                    @RequestParam(value = "configIds", required = false) List<Long> configIds,
                                    @RequestParam(value = "tagIds", required = false) List<Long> tagIds) {
        if (CollectionUtils.isEmpty(configIds) && CollectionUtils.isEmpty(tagIds)) {
            return RestResponse.error(RespCode.USER_REQUIRED_PARAMETER_EMPTY);
        }

        vehicleService.aysnQuery(uid, configIds, tagIds);
        return RestResponse.ok(null);
    }

    @GetMapping("/listByCombinedId")
    @ApiOperation(value = "根据组合id分页查询车辆信息")
    public ResponseEntity listByCombinedId(@RequestParam(required = false) List<Long> modelIds,
                                           @RequestParam(required = false, defaultValue = "0") Long fromId,
                                           @RequestParam(required = false, defaultValue = "500") Integer size) {
        if (CollectionUtils.isEmpty(modelIds)) {
            return RestResponse.error(RespCode.REQUEST_PARAMETER_MISSING);
        }

        return RestResponse.ok(vehicleService.listByCombinedId(modelIds, fromId, size));
    }

    @PostMapping(value = "/updateCertificationStatus")
    @ApiOperation(value = "更新车辆实名认证信息")
    public ResponseEntity updateCertificationStatus(@RequestBody VehicleCerStatusUpdateDto vehicleCerStatusUpdateDto) {
        vehicleService.updateCertificationStatus(vehicleCerStatusUpdateDto);
        return RestResponse.ok(null);
    }
}
