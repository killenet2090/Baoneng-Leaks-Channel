package com.bnmotor.icv.tsp.ota.controller.test;


import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.controller.inner.AbstractController;
import com.bnmotor.icv.tsp.ota.service.IFotaDeviceSyncService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: FotaFirmwareController
 * @Description: 设备同步接口，从tsp设备管理系统提供接口回调
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@RestController
@Api(value="tsp设备同步",tags={"tsp设备同步"})
@RequestMapping(value = "/v1/deviceSync")
@AllArgsConstructor
public class FotaDeviceSyncController extends AbstractController {

    @Autowired
    @Qualifier("fotaDeviceSyncServiceV2")
    private IFotaDeviceSyncService fotaDeviceSyncService;

    /*@RequestMapping(value = "addFotaCar", method = RequestMethod.POST)
    @ApiOperation(value="添加车辆", notes="添加车辆")
    public ResponseEntity addFotaCar(@RequestBody FotaCarInfoDto fotaCarInfoDto){
        fotaDeviceSyncService.syncFotaCar(fotaCarInfoDto);
        return RestResponse.ok(null);
    }*/

    /*@RequestMapping(value = "updateFotaCar", method = RequestMethod.POST)
    @ApiOperation(value="更新车辆", notes="更新车辆")
    public ResponseEntity updateFotaCar(@RequestBody FotaCarInfoDto fotaCarInfoDto){
        fotaDeviceSyncService.updateFotaCar(fotaCarInfoDto);
        return RestResponse.ok(null);
    }*/

    /*@RequestMapping(value = "addFotaCarDevices", method = RequestMethod.POST)
    @ApiOperation(value="添加车辆设备信息", notes="添加车辆设备信息")
    public ResponseEntity addFotaCarDevices(@RequestBody FotaCarDeviceInfoDto fotaCarDeviceInfoDto){
        fotaDeviceSyncService.addFotaCarDevices(fotaCarDeviceInfoDto);
        return RestResponse.ok(null);
    }

    @RequestMapping(value = "delFotaCarDevices", method = RequestMethod.POST)
    @ApiOperation(value="删除车辆设备信息", notes="删除车辆设备信息")
    public ResponseEntity delFotaCarDevices(@RequestBody FotaCarDeviceInfoDto fotaCarDeviceInfoDto){
        fotaDeviceSyncService.delFotaCarDevices(fotaCarDeviceInfoDto);
        return RestResponse.ok(null);
    }

    @RequestMapping(value = "addFotaDeviceTreeNode", method = RequestMethod.POST)
    @ApiOperation(value="添加车辆设备信息", notes="添加车辆设备信息")
    public ResponseEntity addFotaDeviceTreeNode(@RequestBody FotaDeviceTreeNodeDto fotaDeviceTreeNodeDto){
        fotaDeviceSyncService.addFotaDeviceTreeNode(fotaDeviceTreeNodeDto);
        return RestResponse.ok(null);
    }*/

    /*@RequestMapping(value = "syncFotaDeviceComponentInfo", method = RequestMethod.POST)
    @ApiOperation(value="车型设备零件同步", notes="车型设备零件同步")
    public ResponseEntity syncFotaDeviceComponentInfo(@RequestBody List<FotaDeviceComponentInfoDto> fotaDeviceComponentInfoDtoList){
        fotaDeviceSyncService.syncFotaDeviceComponentInfo(fotaDeviceComponentInfoDtoList);
        return RestResponse.ok("success");
    }

    @RequestMapping(value = "syncFotaCar", method = RequestMethod.POST)
    @ApiOperation(value="车辆信息同步", notes="车辆信息同步")
    public ResponseEntity syncFotaCar(@RequestBody FotaCarInfoDto fotaCarInfoDto){
        fotaDeviceSyncService.syncFotaCar(fotaCarInfoDto);
        return RestResponse.ok("success");
    }*/

    @RequestMapping(value = "buildTreeNodeFromTspDevice", method = RequestMethod.GET)
    @ApiOperation(value="创建设备树同步", notes="创建设备树同步", response = Void.class)
    public ResponseEntity buildTreeNodeFromTspDevice(){
        fotaDeviceSyncService.buildTreeNodeFromTspDevice();
        return RestResponse.ok("success");
    }
}
