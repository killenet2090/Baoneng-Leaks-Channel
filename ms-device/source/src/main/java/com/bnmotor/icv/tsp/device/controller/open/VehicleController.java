package com.bnmotor.icv.tsp.device.controller.open;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.common.Acl;
import com.bnmotor.icv.tsp.device.common.enums.veh.VehicleType;
import com.bnmotor.icv.tsp.device.common.enums.vehStatus.VehLifecircleStatus;
import com.bnmotor.icv.tsp.device.model.entity.VehiclePo;
import com.bnmotor.icv.tsp.device.model.request.vehicle.VehicleLicPlateDto;
import com.bnmotor.icv.tsp.device.model.response.vehicle.VehicleBaseVo;
import com.bnmotor.icv.tsp.device.model.response.vehicle.VehicleVo;
import com.bnmotor.icv.tsp.device.service.IVehModelService;
import com.bnmotor.icv.tsp.device.service.IVehicleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName: VehicleController
 * @Description: 车辆控制器
 * @author: zhangwei2
 * @date: 2020/7/4
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/v1/vehicle")
@Api(value = "2c提供车辆相关公共信息查询接口", tags = {"2c车辆公共接口"})
@Slf4j
public class VehicleController {
    @Autowired
    private IVehicleService vehicleService;
    @Resource
    private IVehModelService modelService;

    @GetMapping(value = "/level")
    @ApiOperation(value = "车辆层级,用户获取全部车辆品牌,车系,车型信息,数据将用平铺的形式给出")
    public ResponseEntity queryLevel() {
        return RestResponse.ok(modelService.queryLevel());
    }

    @Acl
    @GetMapping("/checkValid")
    @ApiOperation(value = "判断车辆是否有效(售出并且未报废登记)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "请求头", required = true, paramType = "header", dataType = "string")
    })
    public ResponseEntity checkValid(@RequestParam("vin") String vin) {
        boolean isValid = vehicleService.checkValid(vin);
        return RestResponse.ok(isValid);
    }

    @Acl
    @PostMapping("/updateDrivingLicPlate")
    @ApiOperation(value = "修改车牌号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "请求头", required = true, paramType = "header", dataType = "string")
    })
    public ResponseEntity updateDrivingLicPlate(@RequestBody @Validated VehicleLicPlateDto vehicleLicPlateDto) {
        boolean checkDrivingLicPlate = vehicleService.checkDrivingLicPlate(vehicleLicPlateDto);
        if (checkDrivingLicPlate) {
            return RestResponse.error("V0001", "该车牌号已经存在,请更换车牌号后重试！！！");
        }
        int result = vehicleService.updateDrivingLicPlate(vehicleLicPlateDto);
        if (result == 0) {
            return RestResponse.error("V0002", "修改车牌号失败！！！");
        }
        return RestResponse.ok(result);
    }

    @GetMapping(value = "/get")
    @ApiOperation(value = "根据车辆vin查询车辆基本信息")
    public ResponseEntity getVehicle(@RequestParam String vin) {
        VehicleBaseVo vehicleVo = new VehicleBaseVo();
        VehiclePo vehiclePo = vehicleService.getVehicle(vin);
        VehicleType vehicleType = VehicleType.valueOf(vehiclePo.getVehType());
        if (vehicleType != null) {
            vehicleVo.setVehType(vehicleType.getDesp());
        }
        vehicleVo.setVin(vin);
        vehicleVo.setVehModel(vehiclePo.getVehModelName());
        vehicleVo.setEngineNo(vehiclePo.getEngineNo());
        vehicleVo.setDrivingLicPlate(vehiclePo.getDrivingLicPlate());
        if (VehLifecircleStatus.GUEST_MODE_OPENING.getStatus().equals(vehiclePo.getVehLifecircle()) || VehLifecircleStatus.GUEST_MODE_CLOSING.getStatus().equals(vehiclePo.getVehLifecircle())) {
            vehicleVo.setVehLifecircle(VehLifecircleStatus.GUEST_MODE.getStatus());
        } else {
            vehicleVo.setVehLifecircle(vehiclePo.getVehLifecircle());
        }

        return RestResponse.ok(vehicleVo);
    }
}
