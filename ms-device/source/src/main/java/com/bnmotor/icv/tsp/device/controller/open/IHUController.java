package com.bnmotor.icv.tsp.device.controller.open;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.common.Constant;
import com.bnmotor.icv.tsp.device.common.ReqContext;
import com.bnmotor.icv.tsp.device.model.request.vehicle.VehicleActivateDto;
import com.bnmotor.icv.tsp.device.model.request.vehicle.VehicleActiveCallbackDto;
import com.bnmotor.icv.tsp.device.model.response.vehicle.QRCodeScanVo;
import com.bnmotor.icv.tsp.device.model.response.vehicle.VehActivateStateVo;
import com.bnmotor.icv.tsp.device.service.IHUService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: IHUController
 * @Description: 车机控制器
 * @author: huangyun1
 * @date: 2020/11/4
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/v1/ihu")
@Api(value = "2c提供车机相关接口", tags = {"2c车机相关接口"})
@Slf4j
public class IHUController {
    @Autowired
    private IHUService ihuService;

    @GetMapping(value = "/active/queryState")
    @ApiOperation(value = "查询车机激活状态")
    public ResponseEntity queryActiveState(@RequestParam("vin") String vin, @RequestParam("deviceId") String deviceId){
        VehActivateStateVo vehActivateStateVo = ihuService.queryActiveState(vin, deviceId);
        return RestResponse.ok(vehActivateStateVo);
    }

    @GetMapping(value = "/active/qrcode", produces = MediaType.IMAGE_JPEG_VALUE)
    @ApiOperation(value = "车机获取激活二维码接口")
    public byte[] getHuActivationQRCode(@RequestParam("vin") String vin, @RequestParam("deviceId") String deviceId, HttpServletResponse response){
        String token = ihuService.getActivateToken(vin, deviceId);
        byte[] qrCodeImage = ihuService.generateActivateQRCode(vin, token);
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");

        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        response.setHeader(Constant.QR_CODE_TOKEN, token);
        return qrCodeImage;
    }

    @PostMapping("/active/scan")
    @ApiOperation(value = "APP扫描激活二维码返回URL接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "请求头", required = true, paramType = "header", dataType = "string"),
    })
    public ResponseEntity scanHuActivationQRCode(@RequestBody @Validated VehicleActivateDto vehicleActivateDto) {
        if (ReqContext.getUid() == null) {
            return RestResponse.error(RespCode.USER_UNAUTHORIZED_ACCESS);
        }
        QRCodeScanVo QRCodeScanVo = ihuService.scanQRCode(vehicleActivateDto);
        return RestResponse.ok(QRCodeScanVo);
    }

    @PostMapping("/active/scanConfirm")
    @ApiOperation(value = "APP扫描激活二维码确认接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "请求头", required = true, paramType = "header", dataType = "string"),
    })
    public ResponseEntity activateHu(@RequestBody @Validated VehicleActivateDto vehicleActivateDto) {
        if (ReqContext.getUid() == null) {
            return RestResponse.error(RespCode.USER_UNAUTHORIZED_ACCESS);
        }
        ihuService.vehicleActivate(vehicleActivateDto);
        return RestResponse.ok(null);
    }

    @PostMapping("/active/scanCancel")
    @ApiOperation(value = "APP扫描激活二维码取消接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "请求头", required = true, paramType = "header", dataType = "string"),
    })
    public ResponseEntity cancelActivateHu(@RequestBody @Validated VehicleActivateDto vehicleActivateDto) {
        if (ReqContext.getUid() == null) {
            return RestResponse.error(RespCode.USER_UNAUTHORIZED_ACCESS);
        }
        ihuService.cancelActivateHu(vehicleActivateDto);
        return RestResponse.ok(null);
    }

    @GetMapping(value = "/active/checkState")
    @ApiOperation(value = "车机获取激活二维码后发起http短轮询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "qrcodeKey", value = "请求头", required = true, paramType = "header", dataType = "string"),
    })
    public ResponseEntity roundCheckHuActivateState(@RequestParam("vin") String vin, @RequestParam("deviceId") String deviceId, HttpServletRequest req) {
        String token = req.getHeader(Constant.QR_CODE_TOKEN);
        return RestResponse.ok(ihuService.roundCheckActivateState(vin, deviceId, token));
    }

    @PostMapping("/active/callback")
    @ApiOperation(value = "车机激活回调")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vin", value = "请求头", required = true, paramType = "header", dataType = "string"),
    })
    public ResponseEntity activeCallback(HttpServletRequest req, @Validated VehicleActiveCallbackDto vehicleActiveCallbackDto) {
        String vin = req.getHeader(Constant.VIN);
        if (StringUtils.isEmpty(vin)) {
            throw new AdamException(RespCode.USER_UNAUTHORIZED_ACCESS);
        }
        ihuService.activeCallback(vin, vehicleActiveCallbackDto);
        return RestResponse.ok(null);
    }
}
