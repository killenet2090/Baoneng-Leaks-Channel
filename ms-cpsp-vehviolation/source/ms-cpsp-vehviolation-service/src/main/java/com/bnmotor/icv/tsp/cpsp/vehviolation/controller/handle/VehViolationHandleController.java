package com.bnmotor.icv.tsp.cpsp.vehviolation.controller.handle;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.cpsp.vehviolation.model.request.VehViolationHandleDto;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationHandleOutput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.service.IVehviolationHandleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@Api(tags = {"3.4 违章办理"})
@ApiSort(value = 5)
public class VehViolationHandleController {

    @Autowired
    private IVehviolationHandleService vehviolationHandleService;

    @ApiOperation(value = "3.4.1 违章办理接口", notes = "返回提供商收银台支付二维码")
    @ApiOperationSupport(order = 1)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vin", value = "车架号", paramType = "query", dataType = "string", required = true),
    })
    @PostMapping(value = "/getPayUrl")
    public ResponseEntity vehViolationHandle(@RequestBody @Validated VehViolationHandleDto vehViolationHandleDto) {
        VehviolationHandleOutput output = vehviolationHandleService.handleVehviolation(vehViolationHandleDto);
        return RestResponse.ok(output);
    }
}
