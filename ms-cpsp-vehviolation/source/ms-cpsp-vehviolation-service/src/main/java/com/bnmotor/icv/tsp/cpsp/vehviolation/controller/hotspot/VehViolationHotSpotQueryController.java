package com.bnmotor.icv.tsp.cpsp.vehviolation.controller.hotspot;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationHotSpotQueryOutput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.service.IVehviolationHotSpotQueryService;
import com.bnmotor.icv.tsp.cpsp.vehviolation.service.IVehviolationQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname VehViolationHandleController
 * @Description 违章办理控制器
 * @Date 2020/12/28 17:09
 * @Created by liuyiwei1@bngrp.com
 */
@RestController
@RequestMapping("/v1/violation")
@Api(tags = {"3.6 违章热点查询"})
public class VehViolationHotSpotQueryController {

    @Autowired
    private IVehviolationHotSpotQueryService vehviolationHotSpotQueryService;

    @ApiOperation(value = "3.6.1 违章热点查询接口", notes = "违章热点查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lng", value = "经度", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "lat", value = "纬度", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "type", value = "违章类型", paramType = "query", dataType = "string", required = false),
            @ApiImplicitParam(name = "mapType", value = "地图类型", paramType = "query", dataType = "string", required = false),
            @ApiImplicitParam(name = "radius", value = "半径", paramType = "query", dataType = "string", required = false),
    })
    @GetMapping(value = "/hotSpot")
    public ResponseEntity warning(@RequestParam("lng") String lng,@RequestParam("lat") String lat, String type, String mapType, String radius) {
        VehviolationHotSpotQueryOutput output = vehviolationHotSpotQueryService.queryVehviolationHotSpot(lng, lat, type, mapType, radius);
        return RestResponse.ok(output);
    }
}
