package com.bnmotor.icv.tsp.device.controller.inner;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.model.request.vehDevice.UpdateVehDeviceBindedDto;
import com.bnmotor.icv.tsp.device.model.request.vehDevice.VehDeviceBindDto;
import com.bnmotor.icv.tsp.device.model.request.vehDevice.VehDeviceRebindDto;
import com.bnmotor.icv.tsp.device.model.request.vehDevice.VehDeviceUnbindDto;
import com.bnmotor.icv.tsp.device.model.response.vehDevice.VehicleDeviceVo;
import com.bnmotor.icv.tsp.device.service.IVehicleDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName: InnerVehicleDeviceController
 * @Description: 车辆零部件管理
 * @author: zhangwei2
 * @date: 2020/7/28
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/inner/vehDevice")
@Api(value = "车辆设备关系维护,包括设备和车进行绑定,解绑,提供车辆换件和换件记录查询", tags = {"车辆零部件管理接口"})
@Slf4j
public class InnerVehicleDeviceController {
    @Autowired
    private IVehicleDeviceService vehDeviceService;

    @GetMapping(value = "/device")
    @ApiOperation(value = "车辆设备接口,用于获取车辆绑定的设备")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceType", value = "设备类型,可以不传;1-hu;2-tbox;3-pep")
    })
    public ResponseEntity listDevice(@RequestParam("vin") String vin, @RequestParam(value = "deviceType", required = false) Integer deviceType) {
        List<VehicleDeviceVo> detailVo = vehDeviceService.listDevice(vin, deviceType);
        return RestResponse.ok(detailVo);
    }

    @GetMapping(value = "/listVehicleIccid")
    @ApiOperation(value = "根据车架号集合获取车辆绑定的物联网卡")
    public ResponseEntity listVehicleIccid(@RequestParam("vins") List<String> vins) {
        return RestResponse.ok(vehDeviceService.listDeviceSim(vins));
    }

    @GetMapping(value = "/getVehicleByIccid")
    @ApiOperation(value = "根据物联网卡查询当前绑定的车")
    public ResponseEntity getVehicle(@RequestParam("iccid") String iccid) {
        return RestResponse.ok(vehDeviceService.getVehicleSim(iccid));
    }

    @GetMapping(value = "/listReplacement")
    @ApiOperation(value = "根据车辆和设备类型查询车辆对应的设备换件记录")
    public ResponseEntity listReplacement(@RequestParam String vin, @RequestParam Integer deviceType) {
        return RestResponse.ok(vehDeviceService.listBindHistory(vin, deviceType));
    }

    @PostMapping(value = "/rebind")
    @ApiOperation(value = "车辆零部件重新绑定")
    public ResponseEntity rebind(@RequestBody @Validated VehDeviceRebindDto rebindDto) {
        vehDeviceService.rebind(rebindDto);
        return RestResponse.ok(null);
    }

    @PostMapping(value = "/updateBinded")
    @ApiOperation(value = "更新车辆零部件绑定信息")
    public ResponseEntity updateBinded(@RequestBody @Validated @Valid UpdateVehDeviceBindedDto updateBinded) {
        vehDeviceService.updateBinded(updateBinded);
        return RestResponse.ok(null);
    }

    @PostMapping(value = "/bind")
    @ApiOperation(value = "车辆零部件绑定")
    public ResponseEntity bind(@RequestBody @Validated VehDeviceBindDto bindDto) {
        vehDeviceService.bind(bindDto);
        return RestResponse.ok(null);
    }

    @PostMapping(value = "/unBind")
    @ApiOperation(value = "车辆零部件解除绑定")
    public ResponseEntity unbind(@RequestBody @Validated VehDeviceUnbindDto unbindDto) {
        vehDeviceService.unbind(unbindDto);
        return RestResponse.ok(null);
    }
}
