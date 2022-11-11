package com.bnmotor.icv.tsp.cpsp.vehviolation.controller.query;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationQueryOutput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.service.IVehviolationQueryService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname VehViolationQueryController
 * @Description 违章查询控制器
 * @Date 2020/12/28 17:09
 * @Created by liuyiwei1@bngrp.com
 */
@RestController
@RequestMapping("/v1/violation")
@Api(tags = {"3.3 违章查询"})
@ApiSort(value = 5)
public class VehViolationQueryController {

    @Autowired
    private IVehviolationQueryService vehviolationQueryService;

    @ApiOperation(value = "3.3.1 违章概览及列表查询接口", notes = "违章概览及列表查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vin", value = "车架号", paramType = "query", dataType = "string", required = true),
    })
    @GetMapping(value = "/query")
    public ResponseEntity warning(@RequestParam("vin") String vin) {
        VehviolationQueryOutput output = vehviolationQueryService.queryVehviolation(vin);
        return RestResponse.ok(output);
    }
}
