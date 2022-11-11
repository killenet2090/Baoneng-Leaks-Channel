package gaea.vehicle.basic.service.controller;

import java.util.List;

import gaea.vehicle.basic.service.models.domain.VehicleModel;
import gaea.vehicle.basic.service.models.query.VehicleModelQuery;
import gaea.vehicle.basic.service.models.request.VehicleModelReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import gaea.vehicle.basic.service.models.page.PageResult;
import gaea.vehicle.basic.service.models.page.Resp;
import gaea.vehicle.basic.service.models.validate.Update;
import gaea.vehicle.basic.service.service.VehicleModelService;

import javax.validation.Valid;


/**
 * 
 *
 * @author xiajiankang
 * @email xiajiankang1@bngrp.com
 * @date 2020-04-02 18:06:32
 */
@RestController
@Api(value="车型管理接口",tags={"车型管理接口"})
@RequestMapping("/vehiclemodel")
public class VehicleModelController {

    @Autowired
    private VehicleModelService vehicleModelService;

    /**
     * 列表
     */
    @ApiOperation("列表")
    @GetMapping("vehicle-models")
    public PageResult<List<VehicleModel>> queryPage(VehicleModelQuery query) {
        return new PageResult<>(query, vehicleModelService.queryPage(query));
    }

    @ApiOperation("获取")
    @GetMapping("vehicle-models/{vehicleModelId}")
    public Resp<VehicleModel> queryById(@PathVariable Long vehicleModelId) {
        return new Resp<>(vehicleModelService.queryById(vehicleModelId));
    }

    @ApiOperation("新增")
    @PostMapping("vehicle-models")
    public Resp<Integer> saveVehicleModel(@RequestBody @Valid VehicleModelReq vehicleModelReq) {
        VehicleModel vehicleModel = new VehicleModel();
        BeanUtils.copyProperties(vehicleModelReq, vehicleModel);
        return new Resp<>(vehicleModelService.insertVehicleModel(vehicleModel));
    }

    @ApiOperation("更新")
    @PutMapping("vehicle-models")
    public Resp<Integer> updateVehicleModel(@RequestBody @Validated(value = Update.class) VehicleModelReq vehicleModelReq) {
        VehicleModel vehicleModel = new VehicleModel();
        BeanUtils.copyProperties(vehicleModelReq, vehicleModel);
        return new Resp<>(vehicleModelService.updateVehicleModel(vehicleModel));
    }

    @ApiOperation("删除")
    @DeleteMapping("vehicle-models/{vehicleModelId}")
    public Resp<Integer> deleteById(@PathVariable Long vehicleModelId) {
        return new Resp<>(vehicleModelService.deleteById(vehicleModelId));
    }

}
