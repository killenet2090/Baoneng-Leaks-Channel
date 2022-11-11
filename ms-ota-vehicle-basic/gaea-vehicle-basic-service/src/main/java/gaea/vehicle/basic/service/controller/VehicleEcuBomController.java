/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.vehicle.basic.service.controller;

import gaea.vehicle.basic.service.models.domain.VehicleEcuBom;
import gaea.vehicle.basic.service.models.query.VehicleEcuBomQuery;
import gaea.vehicle.basic.service.models.request.VehicleEcuBomReq;
import gaea.vehicle.basic.service.models.request.VehicleEcuBomRequest;
import org.springframework.beans.factory.annotation.Autowired;
import gaea.vehicle.basic.service.models.page.PageResult;
import gaea.vehicle.basic.service.models.page.Resp;
import gaea.vehicle.basic.service.models.validate.Update;
import gaea.vehicle.basic.service.service.VehicleEcuBomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <pre>
 *  ECU信息表 Api,控制业务流程.
 *  提供包括业务流程校验,业务流程控制.
 *
 *  <b>地址规则</b>
 *   列表或分页:GET /persons
 *   单人: GET /persons/1
 *   新增: POST /persons
 *   修改: PUT /persons
 *   删除: DELETE /persons/1
 *   关联
 *   用户下狗
 *   列表 GET /persons/1/dogs
 *   单狗 GET /persons/1/dogs/2
 *   增加狗 POST /persons/1/dogs
 *   修改狗 PUT /persons/1/dogs
 *   删除狗 DELETE /persons/1/dogs/2
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@AllArgsConstructor
@Api(value="ECU信息管理接口",tags={"ECU信息管理接口"})
@RestController
public class VehicleEcuBomController {
    /**
     * ECU信息表服务接口
     */
    @Autowired
    private final VehicleEcuBomService vehicleEcuBomService;

    @ApiOperation("ECU信息表列表")
    @GetMapping("vehicle-ecu-boms")
    public PageResult<List<VehicleEcuBom>> queryPage(VehicleEcuBomQuery query) {
        return new PageResult<>(query, vehicleEcuBomService.queryPage(query));
    }

    @ApiOperation("ECU信息表获取")
    @GetMapping("vehicle-ecu-boms/{vehicleEcuBomId}")
    public Resp<VehicleEcuBom> queryById(@PathVariable Long vehicleEcuBomId) {
        return new Resp<>(vehicleEcuBomService.queryById(vehicleEcuBomId));
    }

    @ApiOperation("ECU信息表新增")
    @PostMapping("vehicle-ecu-boms")
    public Resp<Long> saveVehicleEcuBom(@RequestBody @Valid VehicleEcuBomReq vehicleEcuBomReq) {
        VehicleEcuBom vehicleEcuBom = new VehicleEcuBom();
        BeanUtils.copyProperties(vehicleEcuBomReq, vehicleEcuBom);
        return new Resp<>(vehicleEcuBomService.insertVehicleEcuBom(vehicleEcuBom));
    }

    @ApiOperation("根据vin查询ECU列表")
    @GetMapping("queryByVin")
    public Resp<List<VehicleEcuBom>> queryByVin(VehicleEcuBomRequest vehicleEcuBomReq) {
        return new Resp<>(vehicleEcuBomService.queryByVin(vehicleEcuBomReq));
    }

    @ApiOperation("ECU信息表更新")
    @PutMapping("vehicle-ecu-boms")
    public Resp<Integer> updateVehicleEcuBom(@RequestBody @Validated(value = Update.class) VehicleEcuBomReq vehicleEcuBomReq) {
        VehicleEcuBom vehicleEcuBom = new VehicleEcuBom();
        BeanUtils.copyProperties(vehicleEcuBomReq, vehicleEcuBom);
        return new Resp<>(vehicleEcuBomService.updateVehicleEcuBom(vehicleEcuBom));
    }

    @ApiOperation("ECU信息表删除")
    @DeleteMapping("vehicle-ecu-boms/{vehicleEcuBomId}")
    public Resp<Integer> deleteById(@PathVariable Long vehicleEcuBomId) {
        return new Resp<>(vehicleEcuBomService.deleteById(vehicleEcuBomId));
    }

}
