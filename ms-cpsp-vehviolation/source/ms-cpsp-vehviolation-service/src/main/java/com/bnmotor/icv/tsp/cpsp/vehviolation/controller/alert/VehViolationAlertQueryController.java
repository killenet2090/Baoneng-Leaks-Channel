package com.bnmotor.icv.tsp.cpsp.vehviolation.controller.alert;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationAlertQueryOutput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.service.IVehviolationAlertQueryService;
import com.bnmotor.icv.tsp.cpsp.vehviolation.service.IVehviolationQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname VehViolationHandleController
 * @Description 违章办理控制器
 * @Date 2020/12/28 17:09
 * @Created by liuyiwei1@bngrp.com
 */
@RestController
@RequestMapping("/v1/violation")
@Api(tags = {"3.5 违章提醒"})
public class VehViolationAlertQueryController {

    @Autowired
    private IVehviolationAlertQueryService vehviolationAlertQueryService;

    @ApiOperation(value = "3.5.1 违章提醒查询接口", notes = "违章提醒查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vin", value = "车架号", paramType = "query", dataType = "string", required = true),
    })
    @GetMapping(value = "/alertQuery")
    public ResponseEntity warning(String vin) {
        VehviolationAlertQueryOutput output = vehviolationAlertQueryService.queryAlertVehviolation(vin);
        return RestResponse.ok(output);
    }
}
