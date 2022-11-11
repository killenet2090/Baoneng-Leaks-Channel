package gaea.vehicle.basic.service.controller;

import java.util.List;

import gaea.vehicle.basic.service.models.domain.Vehicle;
import gaea.vehicle.basic.service.models.query.VehicleQuery;
import gaea.vehicle.basic.service.models.request.VehicleReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import gaea.vehicle.basic.service.models.page.PageResult;
import gaea.vehicle.basic.service.models.page.Resp;
import gaea.vehicle.basic.service.models.query.VehicleConditionQuery;
import gaea.vehicle.basic.service.models.validate.Update;
import gaea.vehicle.basic.service.models.vo.VehicleConditionPageVO;
import gaea.vehicle.basic.service.models.vo.VehicleConditionVO;
import gaea.vehicle.basic.service.service.VehicleService;

import javax.validation.Valid;

/**
 * 车辆基础信息表
 *
 * @author xiajiankang
 * @email xiajiankang1@bngrp.com
 * @date 2020-04-02 18:06:32
 */
@Slf4j
@Api(value="车辆管理接口",tags={"车辆管理接口"})
@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @ApiOperation("车辆基础信息表列表")
    @GetMapping("vehicles")
    public PageResult<List<Vehicle>> queryPage(VehicleQuery query) {
        return new PageResult<>(query, vehicleService.queryPage(query));
    }

    @ApiOperation("获取下拉预置车辆信息搜索条件")
    @GetMapping("conditions")
    public Resp<VehicleConditionVO> queryConditions() {
        return new Resp<>(vehicleService.queryConditions());
    }

    @ApiOperation("条件查询")
    @GetMapping("queryByConditions")
    public PageResult<List<VehicleConditionPageVO>> queryByConditions(VehicleConditionQuery vehicleConditionQuery) {
        return new PageResult<>(vehicleConditionQuery,vehicleService.queryByConditions(vehicleConditionQuery));
    }


    @ApiOperation("车辆基础信息表获取")
    @GetMapping("vehicles/{vehicleId}")
    public Resp<Vehicle> queryById(@PathVariable Long vehicleId) {
        return new Resp<>(vehicleService.queryById(vehicleId));
    }

    @ApiOperation("车辆基础信息表新增")
    @PostMapping("vehicles")
    public Resp<Integer> saveVehicle(@RequestBody @Valid VehicleReq vehicleReq) {
        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(vehicleReq, vehicle);
        return new Resp<>(vehicleService.insertVehicle(vehicle));
    }

    @ApiOperation("车辆基础信息表更新")
    @PutMapping("vehicles")
    public Resp<Integer> updateVehicle(@RequestBody @Validated(value = Update.class) VehicleReq vehicleReq) {
        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(vehicleReq, vehicle);
        return new Resp<>(vehicleService.updateVehicle(vehicle));
    }

    @ApiOperation("车辆基础信息表删除")
    @DeleteMapping("vehicles/{vehicleId}")
    public Resp<Integer> deleteById(@PathVariable Long vehicleId) {
        return new Resp<>(vehicleService.deleteById(vehicleId));
    }

}
