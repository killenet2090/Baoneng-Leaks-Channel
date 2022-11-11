package com.bnmotor.icv.tsp.device.controller.open;

import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.common.ReqContext;
import com.bnmotor.icv.tsp.device.model.response.vehBind.VehicleInvoiceVo;
import com.bnmotor.icv.tsp.device.service.IVehicleBindService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author huangyun1
 * @ClassName: TspDrivingLicensePo
 * @Description: 行驶证信息 controller层
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-09-24
 */
@RestController
@RequestMapping("/v1/vehicleSaleInfo")
@Api(value = "2c车辆销售发票相关信息", tags = {"2c车辆销售发票相关接口"})
@Slf4j
public class VehicleSaleInfoController {
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


}
