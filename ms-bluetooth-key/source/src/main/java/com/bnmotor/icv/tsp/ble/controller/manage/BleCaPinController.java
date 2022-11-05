package com.bnmotor.icv.tsp.ble.controller.manage;

import com.bnmotor.icv.adam.web.rest.BaseController;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ble.common.RespCode;
import com.bnmotor.icv.tsp.ble.common.constant.Constant;
import com.bnmotor.icv.tsp.ble.model.entity.BleCaPinPo;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleCaPinDto;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleDevicePinDto;
import com.bnmotor.icv.tsp.ble.service.BleCaPinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @ClassName: BleCaPinController
 * @Description: 证书pin码业务Controller入口
 * @author: shuqi1
 * @date: 2020/6/25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/v1/ble/capin")
@Api(value = "pin码生成与查询接口", tags = {"3.蓝牙钥匙管理"})
public class BleCaPinController extends BaseController {
    @Autowired
    private BleCaPinService bleCaPinService;

    @PostMapping(value = "/update")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "uid", value = "用户的id", defaultValue = "", required = true, dataType = "String")
    })
    @ApiOperation(value = "更新指定pin码", notes = "申请证书和生成蓝牙")
    public ResponseEntity blekeyCaPinCreateApp(@RequestHeader("uid") String uid, @RequestHeader("projectId") String projectId, @Validated @RequestBody BleCaPinDto bleCaPinDto) {
        bleCaPinDto.setProjectId(projectId);
        //查询在DB里面是否存在
        Optional<BleCaPinPo> tspBleCaPinPo = Optional.ofNullable(bleCaPinService.queryPinInfo(bleCaPinDto));
        if (!tspBleCaPinPo.isPresent()) {
            return RestResponse.error(RespCode.CAPIN_WAS_EXIST.getValue(), RespCode.CAPIN_WAS_EXIST.getDescription());
        }
        BleCaPinPo bleCaPinPoList = bleCaPinService.generateCaPinInfo(bleCaPinDto, uid);
        return RestResponse.ok(bleCaPinPoList);

    }

    @PostMapping(value = "/device/update")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "uid", value = "用户的id", defaultValue = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "projectId", value = "项目id", defaultValue = "", required = true, dataType = "String"),
    })
    @ApiOperation(value = "更新车下所有pin码", notes = "申请证书和生成蓝牙")
    public ResponseEntity deviceBlekeyCaPinUpdateApp(@RequestHeader("uid") String uid, @RequestHeader("projectId") String projectId, @Validated @RequestBody BleDevicePinDto bleDevicePinDto) {
        bleDevicePinDto.setProjectId(projectId);
        //查询在DB里面是否存在
        Optional<List<BleCaPinPo>> tspBleCaPinPo = Optional.ofNullable(bleCaPinService.queryDevicePinInfo(bleDevicePinDto));

//        if (tspBleCaPinPo.isPresent() && tspBleCaPinPo.get().size() == Constant.BLE_NUM_LIMIT) {
//            return RestResponse.error(RespCode.CAPIN_WAS_EXIST.getValue(), RespCode.CAPIN_WAS_EXIST.getDescription());
//        }
        List<BleCaPinPo> bleCaPinPo = bleCaPinService.generateDevicePinInfo(tspBleCaPinPo, bleDevicePinDto, uid);

        return RestResponse.ok(bleCaPinPo);

    }

    @GetMapping(value = "/query")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "uid", value = "用户的id", defaultValue = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "projectId", value = "项目id", defaultValue = "", required = true, dataType = "String"),
    })
    @ApiOperation(value = "查询证书pin码", notes = "查询证书pin码")
    public ResponseEntity blekeyCaPinQueryApp(@RequestHeader("uid") String uid, @RequestHeader("projectId") String projectId, @Validated @RequestParam("deviceId") String deviceId) {
        List<BleCaPinPo> queryBleCaPins = bleCaPinService.blekeyCaPinQuery(deviceId);
        return RestResponse.ok(queryBleCaPins);
    }

}
