package com.bnmotor.icv.tsp.device.controller.inner;

import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.model.request.vehBind.*;
import com.bnmotor.icv.tsp.device.model.response.vehBind.VehicleBindVo;
import com.bnmotor.icv.tsp.device.service.IVehicleBindService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangyun1
 * @ClassName: InnerVehicleBindController
 * @Description: 车辆绑定相关接口
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-09-24
 */
@RestController
@RequestMapping("/inner/vehicleBind")
@Api(value = "车辆绑定相关信息", tags = {"车辆绑定相关接口"})
@Slf4j
public class InnerVehicleBindController {
    @Autowired
    private IVehicleBindService vehicleBindService;

    @PostMapping("/submitBind")
    @ApiOperation(value = "提交绑定")
    public ResponseEntity submitBind(@Validated SubmitBindDto submitBindDto) {
        if (submitBindDto.getInvoiceFile() == null || submitBindDto.getInvoiceFile().isEmpty()) {
            return RestResponse.error(RespCode.USER_INVALID_INPUT);
        }
        VehicleBindVo vehicleBindVo = vehicleBindService.submitBind(submitBindDto);
        return RestResponse.ok(vehicleBindVo);
    }


    @PostMapping("/submitUnbind")
    @ApiOperation(value = "解绑操作")
    public ResponseEntity submitUnbind(@RequestBody @Validated VehicleUnbindDto vehicleUnbindDto) {
        vehicleBindService.submitUnbind(vehicleUnbindDto);
        return RestResponse.ok(null);
    }

    @PostMapping("/finishBind")
    @ApiOperation(value = "车辆绑定")
    public ResponseEntity finishBind(@ApiParam(name = "finishBindDto", value = "传入json格式", required = true)
                                              @Validated @RequestBody FinishBindDto finishBindDto) {
        vehicleBindService.finishBind(finishBindDto);
        return RestResponse.ok(null);
    }

    @PostMapping("/synchronizationSaleInfo")
    @ApiOperation(value = "同步销售信息")
    public ResponseEntity synchronizationSaleInfo(@RequestBody @Validated VehicleSaleInfoDto vehicleSaleInfoDto) {
        if (StringUtils.isBlank(vehicleSaleInfoDto.getSaleDateStr()) || StringUtils.isBlank(vehicleSaleInfoDto.getInvoiceDateStr())) {
            return RestResponse.error(RespCode.USER_INVALID_INPUT);
        }
        vehicleBindService.synchronizationSaleInfo(vehicleSaleInfoDto);
        return RestResponse.ok(null);
    }

    @PostMapping("/resetBindStatus")
    @ApiOperation(value = "重置用户车辆绑定状态")
    public ResponseEntity resetBindStatus(@RequestBody ResetVehicleBindStatusDto resetVehicleBindStatusDto) {
        vehicleBindService.resetBindStatus(resetVehicleBindStatusDto);
        return RestResponse.ok(null);
    }
}
