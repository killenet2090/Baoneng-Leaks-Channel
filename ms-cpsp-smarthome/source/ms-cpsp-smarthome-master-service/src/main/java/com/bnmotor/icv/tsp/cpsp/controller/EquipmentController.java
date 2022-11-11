package com.bnmotor.icv.tsp.cpsp.controller;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.cpsp.domain.request.EquipmentControlVo;
import com.bnmotor.icv.tsp.cpsp.domain.request.EquipmentSortVo;
import com.bnmotor.icv.tsp.cpsp.pojo.output.*;
import com.bnmotor.icv.tsp.cpsp.service.IEquipmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: EquipmentController
 * @Description: 设备管理控制器
 * @author: jiangchangyuan1
 * @date: 2021/2/10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/v1")
@Api(tags = {"3.2 家居设备管理"})
public class EquipmentController {
    @Autowired
    private IEquipmentService equipmentService;

    /**
     * 室内数据采集查询接口
     * @param vin 车架号
     * @return
     */
    @ApiOperation(value = "3.2.1 室内数据采集查询接口", notes = "室内数据采集查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vin", value = "车架号", dataType = "String", required = true, example = "SVT57213700000018")
    })
    @GetMapping(value = "/condition/list")
    public ResponseEntity getConditionData(String vin) {
        EquipmentConditionListOutput output = equipmentService.getConditionData(vin);
        return RestResponse.ok(output);
    }

    /**
     * 查询设备列表信息
     * @param sorting 排序方式：0-家居名称(默认),1-最近使用，2-添加时间,3-自定义
     * @param vin 车架号
     * @param current 当前页数
     * @param pageSize 每页条数
     * @return
     */
    @ApiOperation(value = "3.2.2 查询设备列表信息接口", notes = "查询设备列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vin", value = "车架号", dataType = "String", required = true, example = "SVT57213700000018"),
            @ApiImplicitParam(name = "sorting", value = "排序方式：0-家居名称(默认),1-最近使用，2-添加时间,3-自定义", dataType = "integer", required = true, example = "1"),
            @ApiImplicitParam(name = "current", value = "当前页数", dataType = "integer", required = true, example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", dataType = "integer", required = true, example = "10"),
    })
    @GetMapping(value = "/equipment/list")
    public ResponseEntity getEquipmentList(Integer sorting, @RequestParam("vin") String vin, Integer current, Integer pageSize) {
        EquipmentListOutput output = equipmentService.getEquipmentList(sorting,vin,current,pageSize);
        return RestResponse.ok(output);
    }

    /**
     * 查询设备状态信息
     * @param equipmentId 设备id
     * @param vin 车架号
     * @return
     */
    @ApiOperation(value = "3.2.3 查询设备状态信息接口", notes = "查询设备状态信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "equipmentId", value = "设备id", dataType = "String", required = true, example = "45893644567467"),
            @ApiImplicitParam(name = "vin", value = "车架号", dataType = "String", required = true, example = "SVT57213700000018"),
    })
    @GetMapping(value = "/equipment/status")
    public ResponseEntity getEquipmentList(@RequestParam("equipmentId") String equipmentId,@RequestParam("vin") String vin) {
        EquipmentStatusOutput output = equipmentService.getEquipmentStatus(equipmentId,vin);
        return RestResponse.ok(output);
    }

    /**
     * 设备控制开关
     * @param vo 请求体
     * @return
     */
    @ApiOperation(value = "3.2.4 设备控制开关接口", notes = "设备控制开关")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "equipmentId", value = "设备id", dataType = "String", required = true, example = "45893644567467"),
            @ApiImplicitParam(name = "status", value = "设备最终状态：最终状态：0-关闭，1-开启(当前操作为关闭设备，则此参数传递为0)", dataType = "integer", required = true, example = "0"),
            @ApiImplicitParam(name = "commonStatus", value = "拓展字段，状态数据", dataType = "String", required = false),
            @ApiImplicitParam(name = "vin", value = "车架号", dataType = "String", required = true, example = "SVT57213700000018")
    })
    @PostMapping(value = "/equipment/control")
    public ResponseEntity equipmentControl(@RequestBody EquipmentControlVo vo) {
        EquipmentControlOutput output = equipmentService.euipmentControl(vo.getEquipmentId(),vo.getStatus(),vo.getVin(),vo.getCommonStatus());
        return RestResponse.ok(output);
    }

    /**
     * 设备卡片排序
     * @param vo 请求实体
     * @return
     */
    @ApiOperation(value = "3.2.5 设备控制开关接口", notes = "设备控制开关")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vin", value = "车架号", dataType = "String", required = true, example = "SVT57213700000018"),
            @ApiImplicitParam(name = "equipmentIds", value = "设备ids,设备排序后的最终顺序", dataType = "String", required = true, example = "[1,2,3]")
    })
    @PostMapping(value = "/equipment/position")
    public ResponseEntity equipmentPositionSet(@RequestBody EquipmentSortVo vo) {
        EquipmentPositionOutput output = equipmentService.euipmentPositionSet(vo.getVin(),vo.getEquipmentIds());
        return RestResponse.ok(output);
    }
}
