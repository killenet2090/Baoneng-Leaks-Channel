package com.bnmotor.icv.tsp.device.controller.inner;

import com.bnmotor.icv.adam.data.mysql.metadata.PageRequest;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.model.response.device.DeviceModelInfoVo;
import com.bnmotor.icv.tsp.device.service.IDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: InnerDeviceController
 * @Description: 设备零部件管理
 * @author: zhangwei2
 * @date: 2020/7/28
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/inner/device")
@Api(value = "零部件管理相关接口, 提供管理人员进行零部件信息的管理和维护接口", tags = {"零部件管理接口"})
@Slf4j
public class InnerDeviceController {
    @Autowired
    private IDeviceService deviceService;

    @GetMapping("/devices")
    @ApiOperation(value = "获取设备列表信息")
    public ResponseEntity listDevices(PageRequest pageRequest,
                                      @RequestParam(value = "searchValue", required = false) String searchValue) {
        return RestResponse.ok(deviceService.listAllDevices(pageRequest, pageRequest.getSearchKey(), searchValue));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "查看设备详细信息，包括车辆信息，sim信息，绑定历史信息等")
    public ResponseEntity getDetail(@RequestParam("id") Long id) {
        return RestResponse.ok(deviceService.getDetail(id));
    }

    @GetMapping("/deviceType")
    @ApiOperation(value = "获取设备类型")
    public ResponseEntity listDeviceTypes(PageRequest pageRequest) {
        return RestResponse.ok(deviceService.listDeviceTypes(pageRequest));
    }

    @GetMapping("/deviceModel")
    @ApiOperation(value = "根据设备类型查询对应的设备型号")
    public ResponseEntity listDeviceModels(PageRequest pageRequest, @RequestParam(value = "deviceType") Integer deviceType) {
        return RestResponse.ok(deviceService.listDeviceModels(pageRequest, deviceType));
    }

    @GetMapping("/bindRecords")
    @ApiOperation(value = "根据设备id查询设备的绑车记录")
    public ResponseEntity listBindRecords(PageRequest pageRequest, @RequestParam(value = "deviceId", required = false) String deviceId) {
        return RestResponse.ok(deviceService.listBindRecords(pageRequest, deviceId));
    }

    @ApiOperation(value = "所有设备名称和ID")
    @GetMapping("/deviceModel/info/all")
    public ResponseEntity listAllDeviceModelInfos() {
        List<DeviceModelInfoVo> vos = deviceService.listAllDeviceModelInfoVo();
        return RestResponse.ok(vos);
    }
}
