package com.bnmotor.icv.tsp.ble.controller.manage;

import com.bnmotor.icv.adam.core.utils.DateUtil;
import com.bnmotor.icv.adam.web.rest.BaseController;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ble.common.RespCode;
import com.bnmotor.icv.tsp.ble.common.constant.Constant;
import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleDeviceDelDto;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyDelDto;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleUserAuthDelDto;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleUserKeyDelDto;
import com.bnmotor.icv.tsp.ble.model.request.feign.TokenCheck;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleDeviceDelVo;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleKeyDelVo;
import com.bnmotor.icv.tsp.ble.model.response.ble.VehicleInfoVo;
import com.bnmotor.icv.tsp.ble.service.BleApplyService;
import com.bnmotor.icv.tsp.ble.service.BleDelService;
import com.bnmotor.icv.tsp.ble.service.BleUserService;
import com.bnmotor.icv.tsp.ble.service.feign.BleCommonFeignService;
import com.bnmotor.icv.tsp.ble.service.feign.DeviceIsOnlineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: BleDelController
 * @Description: 蓝牙钥匙注销业务Controller入口
 * @author: shuqi1
 * @date: 2020/6/25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/v1/ble/mgt")
@Api(value = "蓝牙钥匙注销接口", tags = {"3.蓝牙钥匙管理"})
public class BleDelController extends BaseController {
    @Autowired
    private BleDelService bleDelService;

    @Resource
    private BleApplyService bleApplyService;

    @Resource
    private BleUserService bleUserService;

    @Resource
    private BleCommonFeignService bleCommonFeignService;

    @Resource
    private DeviceIsOnlineService deviceIsOnlineService;

    @PostMapping(value = "/deregister")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "uid", value = "用户的id", defaultValue = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "projectId", value = "项目id", defaultValue = "", required = true, dataType = "String"),
    })
    @ApiOperation(value = "注销蓝牙钥匙", notes = "注销蓝牙钥匙")
    public ResponseEntity delBlekeyApp(@RequestHeader("uid") String uid, @RequestHeader("projectId") String projectId, @Validated @RequestBody BleKeyDelDto bleKeyDelDto) {
        if (bleKeyDelDto.getDelType()==Constant.OP_BASE_DEL) {
            TokenCheck tokenCheck = new TokenCheck();
            tokenCheck.setServicePwd(bleKeyDelDto.getServicePwd());
            bleCommonFeignService.verifyServicePwd(tokenCheck, uid);
        }

        //查询在DB里面是否存在
        boolean existInDB = bleDelService.bleKeyIsExistInDb(bleKeyDelDto, projectId, uid);

        if (!existInDB) {
            BleKeyDelVo bleKeyDelVo = bleDelService.AssembleDelBleVo(bleKeyDelDto);
            return RestResponse.ok(bleKeyDelVo);
        }

        String userName = bleUserService.getUserName(uid).getNickname();
        VehicleInfoVo vehicleInfoVo = bleCommonFeignService.bleGetFeignVehicleInfoVo(bleKeyDelDto.getDeviceId());

        UserBleKeyPo userBleKeyPoQuery = bleApplyService.queryBlekeyInfo(bleKeyDelDto, projectId);

        /** merge
        if (userBleKeyPoQuery.getBleKeyStatus()==Constant.ACTIVE_STATUS){
            bleCommonFeignService.verifyVehIsOnline(bleKeyDelDto.getDeviceId());
        }**/
        userBleKeyPoQuery.setOpType(bleKeyDelDto.getDelType());

        if (userBleKeyPoQuery.getBleKeyStatus().equals(Constant.INIT_STATUS) && userBleKeyPoQuery.getBleAuthId() != null) {
            BleKeyDelVo bleKeyDelVo = bleDelService.delMoveBleKey(userBleKeyPoQuery, vehicleInfoVo, projectId, uid, userName);
            bleDelService.mobileMessagePush(userBleKeyPoQuery, vehicleInfoVo, uid);
            return RestResponse.ok(bleKeyDelVo);
        } else {
            BleKeyDelVo bleKeyDelVo = bleDelService.delBleKey(userBleKeyPoQuery, uid, userName);
            return RestResponse.ok(bleKeyDelVo);
        }
    }

    @PostMapping(value = "/userAuth/deregister")
    @ApiOperation(value = "注销蓝牙钥匙", notes = "注销蓝牙钥匙")
    public ResponseEntity uaDelBlekeyApp(@Validated @RequestBody BleUserAuthDelDto userAuthDelDto)  {
        UserBleKeyPo userBleKeyPoQuery = bleApplyService.queryUserAuthBlekey(userAuthDelDto);
        String userName = bleUserService.getUserName(userBleKeyPoQuery.getOwnerUserId()).getNickname();
        VehicleInfoVo vehicleInfoVo = bleCommonFeignService.bleGetFeignVehicleInfoVo(userAuthDelDto.getDeviceId());

        if (userBleKeyPoQuery.getBleKeyStatus().equals(Constant.INIT_STATUS) && userBleKeyPoQuery.getBleAuthId() != null) {
            BleKeyDelVo bleKeyDelVo = bleDelService.delMoveBleKey(userBleKeyPoQuery, vehicleInfoVo, userBleKeyPoQuery.getProjectId(), userBleKeyPoQuery.getOwnerUserId(), userName);
            return RestResponse.ok(bleKeyDelVo);
        } else {
            BleKeyDelVo bleKeyDelVo = bleDelService.delBleKey(userBleKeyPoQuery, userBleKeyPoQuery.getOwnerUserId(), userName);
            return RestResponse.ok(bleKeyDelVo);
        }
    }


    @PostMapping(value = "/user/deregister")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "uid", value = "用户的id", defaultValue = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "projectId", value = "项目id", defaultValue = "", required = true, dataType = "String"),
    })
    @ApiOperation(value = "注销蓝牙钥匙", notes = "注销蓝牙钥匙")
    public ResponseEntity delUserBlekeysApp(@RequestHeader("uid") String uid, @RequestHeader("projectId") String projectId, @Validated @RequestBody BleUserKeyDelDto bleUserKeyDelDto)  {
        try {
            LocalDateTime dt = LocalDateTime.now();
            Long endTime = DateUtil.parseStringToDate(bleUserKeyDelDto.getBleKeyDestroyTime(), DateUtil.TIME_MASK_DEFAULT).getTime();
            LocalDateTime localDateTime = dt.plusMinutes(-30);
            Date now = Date.from(localDateTime.toInstant(ZoneOffset.of("+8")));
            Long currentTime = now.getTime();
            if (endTime.compareTo(currentTime) < 0) {
                return RestResponse.error(RespCode.AUTH_DATETIME_CHCKEROOR.getValue(), RespCode.AUTH_DATETIME_CHCKEROOR.getDescription());
            }
        }catch (ParseException ex){
            return RestResponse.error(RespCode.AUTH_DATETIME_CHCKEROOR.getValue(), RespCode.AUTH_DATETIME_CHCKEROOR.getDescription());
        }

        bleCommonFeignService.verifyVehIsOnline(bleUserKeyDelDto.getDeviceId());
        //查询在DB里面是否存在
        List<UserBleKeyPo> userBleKeys = bleDelService.queryUserKeysFromDb(bleUserKeyDelDto, projectId, uid);

        if (userBleKeys.size() == Constant.COMPARE_EQUAL_VALUE) {
            return RestResponse.ok(Constant.OPERATION_SUCCESS);
        }

        String userName = bleUserService.getUserName(uid).getNickname();
        VehicleInfoVo vehicleInfoVo = bleCommonFeignService.bleGetFeignVehicleInfoVo(bleUserKeyDelDto.getDeviceId());

        userBleKeys.forEach(elem->{
            if (elem.getBleKeyStatus().equals(Constant.INIT_STATUS) && elem.getBleAuthId() != null) {
                elem.setOpType(Constant.OP_BASE_DEL);
                bleDelService.delMoveBleKey(elem, vehicleInfoVo, projectId, uid, userName);
                //bleDelService.mobileMessagePush(elem, vehicleInfoVo, uid);
            } else {
                bleDelService.delBleKey(elem, uid, userName);
            }
        });
        return RestResponse.ok(Constant.OPERATION_SUCCESS);

    }
    @PostMapping(value = "/all/deregister")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "uid", value = "用户的id", defaultValue = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "projectId", value = "项目id", defaultValue = "", required = true, dataType = "String"),
    })
    @ApiOperation(value = "注销蓝牙钥匙", notes = "注销所有蓝牙")
    public ResponseEntity delDeviceBlekeyApp(@RequestHeader("uid") String uid, @RequestHeader("projectId") String projectId, @Validated @RequestBody BleDeviceDelDto bleDeviceDelDto) {
        bleCommonFeignService.verifyVehIsOnline(bleDeviceDelDto.getDeviceId());
        //查询在DB里面是否存在
        boolean existInDB = bleDelService.deviceIsExistInDb(bleDeviceDelDto.getDeviceId(), projectId);

        if (existInDB == false) {
            BleDeviceDelVo bleDeviceDelVo = bleDelService.AssembleDelBleDeviceVo(bleDeviceDelDto);
            return RestResponse.ok(bleDeviceDelVo);
        }
        String userName = bleUserService.getUserName(uid).getNickname();
        VehicleInfoVo vehicleInfoVo = bleCommonFeignService.bleGetFeignVehicleInfoVo(bleDeviceDelDto.getDeviceId());
        List<UserBleKeyPo> bleKeyInvalidList = bleDelService.queryInvalidBleKey(projectId,bleDeviceDelDto);
        bleKeyInvalidList.forEach(elem -> {
            if (elem.getBleKeyStatus().equals(Constant.INIT_STATUS)) {
                bleDelService.delMoveBleKey(elem, vehicleInfoVo, projectId, uid, userName);
                bleDelService.mobileMessagePush(elem, vehicleInfoVo, uid);
            }
        });
        bleDelService.delDeviceAllBleKey(bleDeviceDelDto, projectId, uid, userName);
        return RestResponse.ok(bleDeviceDelDto);
    }
}
