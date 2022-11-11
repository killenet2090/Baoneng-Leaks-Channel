package gaea.vehicle.basic.service.controller;

import java.util.List;

import gaea.vehicle.basic.service.models.domain.VehicleSeries;
import gaea.vehicle.basic.service.models.request.VehicleSeriesReq;
import gaea.vehicle.basic.service.models.validate.Update;
import gaea.vehicle.basic.service.service.VehicleSeriesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import gaea.vehicle.basic.service.models.page.PageResult;
import gaea.vehicle.basic.service.models.page.Resp;
import gaea.vehicle.basic.service.models.query.VehicleSeriesQuery;

import javax.validation.Valid;

/**
 * 车辆系列表
 *
 * @author xiajiankang
 * @email xiajiankang1@bngrp.com
 * @date 2020-04-02 18:06:32
 */
@RestController
@RequestMapping("/vehicleseries")
@Api(value="车系管理接口",tags={"车系管理接口"})
public class VehicleSeriesController {

    @Autowired
    private VehicleSeriesService vehicleSeriesService;

    @ApiOperation("车辆系列表列表")
    @GetMapping("vehicle-seriess")
    public PageResult<List<VehicleSeries>> queryPage(VehicleSeriesQuery query) {
        return new PageResult<>(query, vehicleSeriesService.queryPage(query));
    }

    @ApiOperation("车辆系列表获取")
    @GetMapping("vehicle-seriess/{vehicleSeriesId}")
    public Resp<VehicleSeries> queryById(@PathVariable Long vehicleSeriesId) {
        return new Resp<>(vehicleSeriesService.queryById(vehicleSeriesId));
    }

    @ApiOperation("车辆系列表新增")
    @PostMapping("vehicle-seriess")
    public Resp<Integer> saveVehicleSeries(@RequestBody @Valid VehicleSeriesReq vehicleSeriesReq) {
        VehicleSeries vehicleSeries = new VehicleSeries();
        BeanUtils.copyProperties(vehicleSeriesReq, vehicleSeries);
        return new Resp<>(vehicleSeriesService.insertVehicleSeries(vehicleSeries));
    }

    @ApiOperation("车辆系列表更新")
    @PutMapping("vehicle-seriess")
    public Resp<Integer> updateVehicleSeries(@RequestBody @Validated(value = Update.class) VehicleSeriesReq vehicleSeriesReq) {
        VehicleSeries vehicleSeries = new VehicleSeries();
        BeanUtils.copyProperties(vehicleSeriesReq, vehicleSeries);
        return new Resp<>(vehicleSeriesService.updateVehicleSeries(vehicleSeries));
    }

    @ApiOperation("车辆系列表删除")
    @DeleteMapping("vehicle-seriess/{vehicleSeriesId}")
    public Resp<Integer> deleteById(@PathVariable Long vehicleSeriesId) {
        return new Resp<>(vehicleSeriesService.deleteById(vehicleSeriesId));
    }

}
