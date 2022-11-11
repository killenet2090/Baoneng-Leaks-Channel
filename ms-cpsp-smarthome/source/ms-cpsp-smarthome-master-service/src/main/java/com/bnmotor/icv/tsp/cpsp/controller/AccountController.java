package com.bnmotor.icv.tsp.cpsp.controller;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.cpsp.pojo.output.AccountBindStatusOutput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.AccountQrCodeOutput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.AccountUnBindOutput;
import com.bnmotor.icv.tsp.cpsp.service.IAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: AccountQrCodeController
 * @Description: 生成二维码控制器
 * @author: jiangchangyuan1
 * @date: 2021/3/19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/v1")
@Api(tags = {"账户管理控制器"})
public class AccountController {
    @Autowired
    private IAccountService accountService;

    /**
     * 车机端账户生成二维码接口
     * @param vin 车架号
     * @return
     */
    @ApiOperation(value = "车机端账户生成二维码接口接口", notes = "车机端账户生成二维码接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vin", value = "车架号", dataType = "String", required = true, example = "SVT57213700000018")
    })
    @GetMapping(value = "/account/qrcode")
    public ResponseEntity createQrcode(HttpServletRequest request, String vin) {
        AccountQrCodeOutput output = accountService.createQrcode(request,vin);
        return RestResponse.ok(output);
    }

    /**
     * 车端账户绑定状态查询
     * @param vin 车架号
     * @return
     */
    @ApiOperation(value = "车端账户绑定状态查询接口", notes = "车端账户绑定状态查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vin", value = "车架号", dataType = "String", required = true, example = "SVT57213700000018")
    })
    @GetMapping(value = "/account/bind/status")
    public ResponseEntity getAccountBindStatus(HttpServletRequest request, String vin) {
        AccountBindStatusOutput output = accountService.getAccountBindStatus(request,vin);
        return RestResponse.ok(output);
    }

    /**
     * 车端账户解绑接口
     * @param vin 车架号
     * @return
     */
    @ApiOperation(value = "车端账户解绑接口", notes = "车端账户解绑")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vin", value = "车架号", dataType = "String", required = true, example = "SVT57213700000018")
    })
    @GetMapping(value = "/account/unbind")
    public ResponseEntity accountUnbind(HttpServletRequest request, String vin) {
        AccountUnBindOutput output = accountService.accountUnbind(request,vin);
        return RestResponse.ok(output);
    }

}
