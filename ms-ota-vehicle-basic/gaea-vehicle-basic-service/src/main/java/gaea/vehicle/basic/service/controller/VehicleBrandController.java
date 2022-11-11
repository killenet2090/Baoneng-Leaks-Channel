package gaea.vehicle.basic.service.controller;

import java.util.List;

import gaea.vehicle.basic.service.models.domain.VehicleBrand;
import gaea.vehicle.basic.service.models.query.VehicleBrandQuery;
import gaea.vehicle.basic.service.models.request.VehicleBrandReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import gaea.vehicle.basic.service.models.page.PageResult;
import gaea.vehicle.basic.service.models.page.Resp;
import gaea.vehicle.basic.service.models.validate.Update;
import gaea.vehicle.basic.service.service.VehicleBrandService;

import javax.validation.Valid;


/**
 * 车辆品牌表
 *
 * @author xiajiankang
 * @email xiajiankang1@bngrp.com
 * @date 2020-04-02 18:06:32
 */
@RestController
@Api(value="车辆品牌管理接口",tags={"车辆品牌管理接口"})
@RequestMapping("/vehiclebrand")
public class VehicleBrandController {

    @Autowired
    private VehicleBrandService vehicleBrandService;

    /**
     * 列表
     */

    @ApiOperation("车辆品牌表列表")
    @GetMapping("vehicle-brands")
    public PageResult<List<VehicleBrand>> queryPage(VehicleBrandQuery query) {
        return new PageResult<>(query, vehicleBrandService.queryPage(query));
    }

    @ApiOperation("车辆品牌表获取")
    @GetMapping("vehicle-brands/{vehicleBrandId}")
    public Resp<VehicleBrand> queryById(@PathVariable Long vehicleBrandId) {
        return new Resp<>(vehicleBrandService.queryById(vehicleBrandId));
    }

    @ApiOperation("车辆品牌表新增")
    @PostMapping("vehicle-brands")
    public Resp<Integer> saveVehicleBrand(@RequestBody @Valid VehicleBrandReq vehicleBrandReq) {
        VehicleBrand vehicleBrand = new VehicleBrand();
        BeanUtils.copyProperties(vehicleBrandReq, vehicleBrand);
        return new Resp<>(vehicleBrandService.insertVehicleBrand(vehicleBrand));
    }

    @ApiOperation("车辆品牌表更新")
    @PutMapping("vehicle-brands")
    public Resp<Integer> updateVehicleBrand(@RequestBody @Validated(value = Update.class) VehicleBrandReq vehicleBrandReq) {
        VehicleBrand vehicleBrand = new VehicleBrand();
        BeanUtils.copyProperties(vehicleBrandReq, vehicleBrand);
        return new Resp<>(vehicleBrandService.updateVehicleBrand(vehicleBrand));
    }

    @ApiOperation("车辆品牌表删除")
    @DeleteMapping("vehicle-brands/{vehicleBrandId}")
    public Resp<Integer> deleteById(@PathVariable Long vehicleBrandId) {
        return new Resp<>(vehicleBrandService.deleteById(vehicleBrandId));
    }

}
