package com.bnmotor.icv.tsp.cpsp.controller;

import com.bnmotor.icv.adam.web.filter.ReqContext;
import com.bnmotor.icv.adam.web.interceptor.annotation.Acl;
import com.bnmotor.icv.adam.web.rest.BaseController;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.cpsp.domain.request.SceneExcutionVo;
import com.bnmotor.icv.tsp.cpsp.domain.request.SceneGeofenceVo;
import com.bnmotor.icv.tsp.cpsp.pojo.output.SceneDetailsOutput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.SceneExecutionOutput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.SceneListOutput;
import com.bnmotor.icv.tsp.cpsp.service.ISceneService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: SceneController
 * @Description: 场景管理控制器
 * @author: jiangchangyuan1
 * @date: 2021/2/22
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping(value="/v1")
@Slf4j
@Api(tags = {"3.3 场景管理接口"})
public class SceneController extends BaseController {

    @Autowired
    ISceneService sceneService;

    /**
     * 查询场景信息列表
     * @param title 场景标题
     * @param executionStyle 执行方式：0-仅手动，1-自动,默认-全部
     * @param geofenceId 地理围栏id
     * @param vin 车架号
     * @param current 当前页数
     * @param pageSize 每页条数
     * @return
     */
    @Acl
    @ApiOperation(value = "3.3.1 查询场景信息列表接口", notes = "查询场景信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "场景标题", dataType = "string", required = true, example = "场景标题"),
            @ApiImplicitParam(name = "executionStyle", value = "执行方式：0-仅手动，1-自动,默认-全部", dataType = "integer", required = true, example = "1"),
            @ApiImplicitParam(name = "geofenceId", value = "地理围栏id", dataType = "string", required = true, example = "3547864753654"),
            @ApiImplicitParam(name = "vin", value = "车架号", dataType = "string", required = true, example = "SVT57213700000018"),
            @ApiImplicitParam(name = "current", value = "当前页数", dataType = "integer", required = true, example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", dataType = "integer", required = true, example = "20"),
    })
    @GetMapping(value = "/scene/list")
    public ResponseEntity getSceneList(String title,Integer executionStyle,String geofenceId,String vin,Integer current,Integer pageSize) {
        SceneListOutput output = sceneService.getSceneList(title,executionStyle,geofenceId,vin,current,pageSize);
        return RestResponse.ok(output);
    }

    /**
     * 查询场景信息详情
     * @param sceneId 场景id
     * @param vin 车架号
     * @return
     */
    @Acl
    @ApiOperation(value = "3.3.2 查询场景信息详情接口", notes = "查询场景信息详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vin", value = "车架号", dataType = "string", required = true, example = "SVT57213700000018"),
            @ApiImplicitParam(name = "sceneId", value = "场景id", dataType = "string", required = true, example = "13425234563537"),
    })
    @GetMapping(value = "/scene/details")
    public ResponseEntity getSceneDetails(String sceneId,String vin) {
        SceneDetailsOutput output = sceneService.getSceneDetails(sceneId,vin);
        return RestResponse.ok(output);
    }

    @Acl
    @ApiOperation(value = "场景条件设置保存接口", notes = "场景关联地理围栏")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vin", value = "车架号", dataType = "string", required = true, example = "SVT57213700000018"),
            @ApiImplicitParam(name = "sceneId", value = "场景id", dataType = "string", required = true, example = "24563845645634"),
            @ApiImplicitParam(name = "geofenceId", value = "地理围栏id", dataType = "string", required = true, example = "24387956965"),
            @ApiImplicitParam(name = "isAuto", value = "1-手动，2-自动", dataType = "int", required = true, example = "1"),
            @ApiImplicitParam(name = "prerequisite", value = "执行条件：0-进入，1-离开", dataType = "integer", required = true, example = "1"),
    })
    @PostMapping(value = "/scene/save")
    public ResponseEntity sceneSave(SceneGeofenceVo vo) {
        Long uid = ReqContext.getUid();
        vo.setUid(String.valueOf(uid));
        sceneService.sceneSave(vo);
        return RestResponse.ok(null);
    }

    /**
     * 场景关联地理围栏
     * @param vo 关联参数接收实体
     * @return
     */
    @Acl
    @ApiOperation(value = "3.3.3 场景关联地理围栏接口", notes = "场景关联地理围栏")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vin", value = "车架号", dataType = "string", required = true, example = "SVT57213700000018"),
            @ApiImplicitParam(name = "sceneId", value = "场景id", dataType = "string", required = true, example = "24563845645634"),
            @ApiImplicitParam(name = "geofenceId", value = "地理围栏id", dataType = "string", required = true, example = "24387956965"),
            @ApiImplicitParam(name = "prerequisite", value = "执行条件：0-进入，1-离开", dataType = "integer", required = true, example = "1"),
    })
    @PostMapping(value = "/scene/geofence/bind")
    public ResponseEntity sceneGeofenceBind(SceneGeofenceVo vo) {
        sceneService.sceneGeofenceBind(vo);
        return RestResponse.ok(null);
    }

    /**
     * 场景执行
     * @param vo 场景执行请求体
     * @return
     */
    @Acl
    @ApiOperation(value = "3.3.4 场景执行接口", notes = "场景批量执行")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vin", value = "车架号", dataType = "string", required = true, example = "SVT57213700000018"),
            @ApiImplicitParam(name = "sceneIds", value = "场景ids", dataType = "list", required = true, example = "[13425234563537,342828695485]"),
    })
    @PostMapping(value = "/scene/execution")
    public ResponseEntity sceneExecution(@RequestBody SceneExcutionVo vo) {
        SceneExecutionOutput output = sceneService.sceneExecution(vo.getSceneIds(),vo.getVin());
        return RestResponse.ok(output);
    }

}
