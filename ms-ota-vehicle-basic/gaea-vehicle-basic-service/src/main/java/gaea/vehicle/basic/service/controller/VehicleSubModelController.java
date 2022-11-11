/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.vehicle.basic.service.controller;


import gaea.vehicle.basic.service.models.domain.VehicleSubModel;
import gaea.vehicle.basic.service.models.query.VehicleSubModelQuery;
import gaea.vehicle.basic.service.models.request.VehicleSubModelReq;
import gaea.vehicle.basic.service.models.validate.Update;
import gaea.vehicle.basic.service.service.VehicleSubModelService;
import org.springframework.beans.factory.annotation.Autowired;
import gaea.vehicle.basic.service.models.page.PageResult;
import gaea.vehicle.basic.service.models.page.Resp;
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
 *  车辆配置表 Api,控制业务流程.
 *  提供包括业务流程校验,业务流程控制.
 *
 *  <b>地址规则</b>
 *   列表或分页:GET /persons
 *   单人: GET /persons/1
 *   新增: POST /persons
 *   修改: PUT /persons
 *   删除: DELETE /persons/1
 *   关联
 *   用户
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
@Api(value="车辆配置信息管理接口",tags={"车辆配置信息管理接口"})
@RestController
public class VehicleSubModelController {
    /**
     * 车辆配置表服务接口
     */
    @Autowired
    private final VehicleSubModelService vehicleSubModelService;

    @ApiOperation("车辆配置表列表")
    @GetMapping("vehicle-sub-models")
    public PageResult<List<VehicleSubModel>> queryPage(VehicleSubModelQuery query) {
        return new PageResult<>(query, vehicleSubModelService.queryPage(query));
    }

    @ApiOperation("车辆配置表获取")
    @GetMapping("vehicle-sub-models/{vehicleSubModelId}")
    public Resp<VehicleSubModel> queryById(@PathVariable Long vehicleSubModelId) {
        return new Resp<>(vehicleSubModelService.queryById(vehicleSubModelId));
    }

    @ApiOperation("车辆配置表新增")
    @PostMapping("vehicle-sub-models")
    public Resp<Long> saveVehicleSubModel(@RequestBody @Valid VehicleSubModelReq vehicleSubModelReq) {
        VehicleSubModel vehicleSubModel = new VehicleSubModel();
        BeanUtils.copyProperties(vehicleSubModelReq, vehicleSubModel);
        return new Resp<>(vehicleSubModelService.insertVehicleSubModel(vehicleSubModel));
    }

    @ApiOperation("车辆配置表更新")
    @PutMapping("vehicle-sub-models")
    public Resp<Integer> updateVehicleSubModel(@RequestBody @Validated(value = Update.class) VehicleSubModelReq vehicleSubModelReq) {
        VehicleSubModel vehicleSubModel = new VehicleSubModel();
        BeanUtils.copyProperties(vehicleSubModelReq, vehicleSubModel);
        return new Resp<>(vehicleSubModelService.updateVehicleSubModel(vehicleSubModel));
    }

    @ApiOperation("车辆配置表删除")
    @DeleteMapping("vehicle-sub-models/{vehicleSubModelId}")
    public Resp<Integer> deleteById(@PathVariable Long vehicleSubModelId) {
        return new Resp<>(vehicleSubModelService.deleteById(vehicleSubModelId));
    }

}
