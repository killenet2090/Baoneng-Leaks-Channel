package gaea.vehicle.basic.service.controller;

import java.util.List;

import gaea.vehicle.basic.service.models.domain.VehicleOem;
import gaea.vehicle.basic.service.models.query.VehicleOemQuery;
import gaea.vehicle.basic.service.models.request.VehicleOemReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import gaea.vehicle.basic.service.models.page.PageResult;
import gaea.vehicle.basic.service.models.page.Resp;
import gaea.vehicle.basic.service.models.validate.Update;
import gaea.vehicle.basic.service.service.VehicleOemService;

import javax.validation.Valid;


/**
 * 车辆主机厂表
 *
 * @author xiajiankang
 * @email xiajiankang1@bngrp.com
 * @date 2020-04-02 18:06:32
 */
@RestController
@Api(value="车辆主机厂管理接口",tags={"车辆主机厂管理接口"})
@RequestMapping("/vehicleoem")
public class VehicleOemController {

    @Autowired
    private VehicleOemService vehicleOemService;

    /**
     * 列表
     */
    @ApiOperation("车辆主机厂表列表")
    @GetMapping("vehicle-oems")
    public PageResult<List<VehicleOem>> queryPage(VehicleOemQuery query) {
        return new PageResult<>(query, vehicleOemService.queryPage(query));
    }

    @ApiOperation("车辆主机厂表获取")
    @GetMapping("vehicle-oems/{vehicleOemId}")
    public Resp<VehicleOem> queryById(@PathVariable Long vehicleOemId) {
        return new Resp<>(vehicleOemService.queryById(vehicleOemId));
    }

    @ApiOperation("车辆主机厂表新增")
    @PostMapping("vehicle-oems")
    public Resp<Integer> saveVehicleOem(@RequestBody @Valid VehicleOemReq vehicleOemReq) {
        VehicleOem vehicleOem = new VehicleOem();
        BeanUtils.copyProperties(vehicleOemReq, vehicleOem);
        return new Resp<>(vehicleOemService.insertVehicleOem(vehicleOem));
    }

    @ApiOperation("车辆主机厂表更新")
    @PutMapping("vehicle-oems")
    public Resp<Integer> updateVehicleOem(@RequestBody @Validated(value = Update.class) VehicleOemReq vehicleOemReq) {
        VehicleOem vehicleOem = new VehicleOem();
        BeanUtils.copyProperties(vehicleOemReq, vehicleOem);
        return new Resp<>(vehicleOemService.updateVehicleOem(vehicleOem));
    }

    @ApiOperation("车辆主机厂表删除")
    @DeleteMapping("vehicle-oems/{vehicleOemId}")
    public Resp<Integer> deleteById(@PathVariable Long vehicleOemId) {
        return new Resp<>(vehicleOemService.deleteById(vehicleOemId));
    }

}
