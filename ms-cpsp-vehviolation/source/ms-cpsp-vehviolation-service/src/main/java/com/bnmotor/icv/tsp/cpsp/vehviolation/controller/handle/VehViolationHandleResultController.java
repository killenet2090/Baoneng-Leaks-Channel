package com.bnmotor.icv.tsp.cpsp.vehviolation.controller.handle;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationHandleResultOutput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.service.IVehviolationHandleResultService;
import com.bnmotor.icv.tsp.cpsp.vehviolation.service.IVehviolationHandleService;
import io.swagger.annotations.*;
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
@Api(tags = {"3.4 违章办理"})
public class VehViolationHandleResultController {

    @Autowired
    private IVehviolationHandleResultService vehviolationHandleResultService;

    @ApiOperation(value = "3.4.2 违章办理结果查询接口", notes = "用户支付后， 轮询查询违章办理结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "订单号", paramType = "query", dataType = "string", required = true),
    })
    @GetMapping(value = "/getPayResult")
    public ResponseEntity warning(@RequestParam("orderNo") String orderNo) {
        VehviolationHandleResultOutput output = vehviolationHandleResultService.queryHandleResultVehviolation(orderNo);
        return RestResponse.ok(output);
    }
}
