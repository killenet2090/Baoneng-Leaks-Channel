package com.bnmotor.icv.tsp.device.controller.open;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.common.ReqContext;
import com.bnmotor.icv.tsp.device.model.request.vehBind.VehicleUnbindDto;
import com.bnmotor.icv.tsp.device.model.response.vehBind.VehicleInvoiceVo;
import com.bnmotor.icv.tsp.device.service.IVehicleBindService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author huangyun1
 * @ClassName: TspDrivingLicensePo
 * @Description: 行驶证信息 controller层
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-09-24
 */
@RestController
@RequestMapping("/v1/vehicleBind")
@Api(value = "2c车辆绑定相关信息", tags = {"2c车辆绑定相关接口"})
@Slf4j
public class VehicleBindController {
    @Autowired
    private IVehicleBindService vehicleBindService;

    @PostMapping("/recognitionVehInvoice")
    @ApiOperation(value = "识别机动车销售发票信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "请求头", required = true, paramType = "header", dataType = "string")
    })
    public ResponseEntity recognitionVehInvoice(@RequestParam("invoiceFile") MultipartFile invoiceFile) {
        if (ReqContext.getUid() == null) {
            return RestResponse.error(RespCode.USER_UNAUTHORIZED_ACCESS);
        }

        if (invoiceFile == null || invoiceFile.isEmpty()) {
            return RestResponse.error(RespCode.USER_INVALID_INPUT);
        }
        VehicleInvoiceVo vehicleInvoiceVo = vehicleBindService.recognitionVehInvoice(invoiceFile);
        return RestResponse.ok(vehicleInvoiceVo);
    }

    @PostMapping("/submitUnbind")
    @ApiOperation(value = "解绑操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "请求头", required = true, paramType = "header", dataType = "string")
    })
    public ResponseEntity submitUnbind(@RequestBody @Validated VehicleUnbindDto vehicleUnbindDto) {
        throw new AdamException("ZOOO1", "解绑业务需远程诊断配合,暂不开放");
/*        if (ReqContext.getUid() == null) {
            return RestResponse.error(RespCode.USER_UNAUTHORIZED_ACCESS);
        }

        vehicleUnbindDto.setUserId(ReqContext.getUid());
        vehicleBindService.submitUnbind(vehicleUnbindDto);
        return RestResponse.ok(null);*/
    }

}
