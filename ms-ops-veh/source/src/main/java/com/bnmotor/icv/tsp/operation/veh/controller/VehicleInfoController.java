package com.bnmotor.icv.tsp.operation.veh.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.operation.veh.model.request.EditVehLabelDto;
import com.bnmotor.icv.tsp.operation.veh.model.request.QueryVehicleDto;
import com.bnmotor.icv.tsp.operation.veh.model.request.VehicleDto;
import com.bnmotor.icv.tsp.operation.veh.model.response.UserBindDetailVo;
import com.bnmotor.icv.tsp.operation.veh.model.response.VehicleDetailVo;
import com.bnmotor.icv.tsp.operation.veh.model.response.VehicleVo;
import com.bnmotor.icv.tsp.operation.veh.service.IVehicleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhoulong1
 * @ClassName: VehicleInfoController
 * @Description: 车辆信息controller
 * @since: 2020/7/18
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Api(value = "管理后台车辆相关接口", tags = {"管理后台车辆相关接口"})
@Slf4j
@RequestMapping("/vehicle")
@RestController
public class VehicleInfoController {

    @Autowired
    private IVehicleService vehicleService;

    @GetMapping(value = "/get", produces = "application/json")
    @ApiOperation(value = "车辆详情接口,用于获取车辆基本信息,车辆认证,车辆设备绑定信息,标签等信息")
    public ResponseEntity getVehicleDetail(String vin) {
        log.info("获取车辆详情接口入参{}", vin);
        VehicleDetailVo detailVo = vehicleService.getVehDetail(vin);
        return RestResponse.ok(detailVo);
    }

    @PostMapping("/update")
    @ApiOperation(value = "编辑某台指定车辆的标签")
    public ResponseEntity editVehLabel(@RequestBody @Validated EditVehLabelDto labelDto) {
        log.info("编辑车辆信息入参{}", JSON.toJSONString(labelDto));
        vehicleService.editVehLabel(labelDto);
        return RestResponse.ok(null);
    }

    @PostMapping("/bindVehicleInfo")
    @ApiOperation(value = "车辆绑定信息")
    public ResponseEntity getUserBindVehicleInfo(@RequestBody VehicleDto vehicleDto) {
        log.info("查询车辆绑定信息入参{}", JSON.toJSONString(vehicleDto));
        IPage<UserBindDetailVo> userBindVehicleInfo = vehicleService.getUserBindVehicleInfo(vehicleDto);
        return RestResponse.ok(userBindVehicleInfo);
    }


    @GetMapping("/list")
    @ApiOperation("车辆列表")
    public ResponseEntity queryVehicles(QueryVehicleDto vehicleDto) {
        IPage<VehicleVo> pagedVehicleVos = vehicleService.getPagedVehicles(vehicleDto);
        return RestResponse.ok(pagedVehicleVos);
    }
}
