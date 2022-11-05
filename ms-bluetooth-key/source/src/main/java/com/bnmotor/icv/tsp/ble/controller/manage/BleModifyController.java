package com.bnmotor.icv.tsp.ble.controller.manage;

import com.bnmotor.icv.adam.core.utils.DateUtil;
import com.bnmotor.icv.adam.web.rest.BaseController;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ble.common.RespCode;
import com.bnmotor.icv.tsp.ble.model.entity.BleAuthPo;
import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import com.bnmotor.icv.tsp.ble.model.request.ble.*;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleKeyModifyAuthVo;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleKeyModifyDateVo;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleKeyModifyVo;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleKeyUserCancelVo;
import com.bnmotor.icv.tsp.ble.service.*;
import com.bnmotor.icv.tsp.ble.service.feign.BleCommonFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Optional;

/**
 * @ClassName: BleModifyController
 * @Description: 蓝牙钥匙修改业务Controller入口
 * @author: shuqi1
 * @date: 2020/6/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/v1/ble")
@Api(value = "蓝牙钥匙属性修改", tags = {"3.蓝牙钥匙管理"}, description = "蓝牙钥匙属性修改相关接口")
public class BleModifyController extends BaseController {
    @Autowired
    private BleKeyMapService bleKeyMapService;

    @Autowired
    private BleApplyService bleApplyService;

    @Autowired
    private BleAuthService bleAuthService;

    @Autowired
    private BleCommonFeignService bleCommonFeignService;

    @RequestMapping(value = "/mgt/blekey/expiretime/update", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "uid", value = "用户的id", defaultValue = "", required = true, dataType = "String")
    })
    @ApiOperation(value = "修改蓝牙钥匙有效期", notes = "修改蓝牙钥匙有效期接口")
    public ResponseEntity blekeyModifyExpireDateApp(@RequestHeader("uid") String uid, @RequestHeader("projectId") String projectId, @Validated @RequestBody BleKeyModifyDateDto bleKeyModifyDateDto) {
        try {
            bleCommonFeignService.verifyVehIsOnline(bleKeyModifyDateDto.getDeviceId());

            bleKeyMapService.checkCanModify(bleKeyModifyDateDto.getDeviceId(), bleKeyModifyDateDto.getBleKeyId(), projectId, uid);

            UserBleKeyPo userBleKeyPo = bleKeyMapService.updateDbExpireDate(bleKeyModifyDateDto, projectId, uid);
            if (userBleKeyPo == null) {
                return RestResponse.error(RespCode.QUERY_RESULT_EMPTY.getValue(), RespCode.QUERY_RESULT_EMPTY.getDescription());
            }
            BleKeyModifyDateVo bleKeyModifyVo = new BleKeyModifyDateVo();
            BeanUtils.copyProperties(bleKeyModifyDateDto, bleKeyModifyVo);
            bleKeyModifyVo.setBleEffectiveTime(DateUtil.parseStringToDate(bleKeyModifyDateDto.getBleEffectiveTime(), DateUtil.TIME_MASK_DEFAULT).getTime());
            bleKeyModifyVo.setBleKeyExpireTime(DateUtil.parseStringToDate(bleKeyModifyDateDto.getBleKeyExpireTime(), DateUtil.TIME_MASK_DEFAULT).getTime());
            return RestResponse.ok(bleKeyModifyVo);
        } catch (ParseException ex) {
            return RestResponse.error(RespCode.AUTH_APPLY_ERROR.getValue(), "传入的时间转换失败");
        }
    }

    @RequestMapping(value = "/mgt/devname/update", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "uid", value = "用户的id", defaultValue = "", required = true, dataType = "String")
    })
    @ApiOperation(value = "修改车牌号", notes = "修改车牌号接口")
    public ResponseEntity blekeyModifyDeviceNameApp(@RequestHeader("uid") String uid, @RequestHeader("projectId") String projectId, @Validated @RequestBody BleModDevNameDto bleModDevNameDto) {
        boolean bResult =  bleKeyMapService.updateDbdeviceName(bleModDevNameDto, projectId, uid);
        return RestResponse.ok(bResult);
    }


    @RequestMapping(value = "/mgt/blename/update", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "uid", value = "用户的id", defaultValue = "", required = true, dataType = "String")
    })
    @ApiOperation(value = "修改蓝牙钥匙名字", notes = "修改蓝牙钥匙名字接口")
    public ResponseEntity blekeyModifyExpireDateApp(@RequestHeader("uid") String uid, @RequestHeader("projectId") String projectId, @Validated @RequestBody BleKeyModifyNameDto bleKeyModifyNameDto) {
        BleKeyModifyAuthVo bleKeyModifyAuthVo = bleKeyMapService.updateDbBleKeyName(bleKeyModifyNameDto, projectId, uid);
        return RestResponse.ok(bleKeyModifyAuthVo);
    }

    @RequestMapping(value = "/mgt/auth/update", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "uid", value = "用户的id", defaultValue = "", required = true, dataType = "String")
    })
    @ApiOperation(value = "修改蓝牙钥匙权限", notes = "修改蓝牙钥匙权限接口")
    public ResponseEntity blekeyModifyAuthApp(@RequestHeader("uid") String uid, @RequestHeader("projectId") String projectId, @Validated @RequestBody BleKeyModifyAuthDto bleKeyModifyAuthDto) {
        bleKeyMapService.checkCanModify("", bleKeyModifyAuthDto.getBleKeyId(), projectId, uid);
        BleKeyModifyAuthVo bleKeyModifyVo = bleKeyMapService.reviseBleKeyAuth(bleKeyModifyAuthDto, projectId, uid);

        if (bleKeyModifyVo != null) {
            return RestResponse.ok(bleKeyModifyVo);
        } else {
            return RestResponse.error(RespCode.MODIFY_RIGHT_ERROR.getValue(), "更新权限失败");
        }
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "uid", value = "用户的id", defaultValue = "", required = true, dataType = "String")
    })
    @ApiOperation(value = "查询修改的蓝牙钥匙", notes = "查询修改的蓝牙钥匙接口")
    public ResponseEntity blekeyQueryModifiedApp(@RequestHeader("uid") String uid, @RequestHeader("projectId") String projectId, @Validated @RequestBody BleKeyInfoDto bleKeyinfoDto) {
        UserBleKeyPo bleKeyInfoPo = UserBleKeyPo.builder().build();
        BeanUtils.copyProperties(bleKeyinfoDto, bleKeyInfoPo);
        bleKeyInfoPo.setOwnerUserId(uid);
        bleKeyInfoPo.setUsedUserMobileDeviceId(bleKeyinfoDto.getMobileDeviceId());
        BleKeyModifyVo bleKeyRevisionVo = bleKeyMapService.queryReviseBleKeyInfo(bleKeyInfoPo);
        return RestResponse.ok(bleKeyRevisionVo);
    }

    @RequestMapping(value = "/mgt/cancel", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "uid", value = "用户的id", defaultValue = "", required = true, dataType = "String")
    })
    @ApiOperation(value = "车主撤销授权", notes = "车主撤销授权接口")
    public ResponseEntity blekeyAuthCancelApp(@RequestHeader("uid") String uid, @RequestHeader("projectId") String projectId, @Validated @RequestBody BleAuthCancelDto bleAuthCancelDto) {
        BleAuthPo bleAuthPo = bleAuthService.cancelCheck(bleAuthCancelDto, projectId, uid);
        if (!Optional.ofNullable(bleAuthPo).isPresent()) {
            return responseError(RespCode.AUTH_COMPE_QUERY_ERROR.getDescription(), RespCode.AUTH_COMPE_QUERY_ERROR.getValue());
        }
        bleApplyService.cancelBleKey(bleAuthCancelDto, projectId, uid);
        bleAuthService.cancelAuth(bleAuthPo, projectId, uid);
        Optional<UserBleKeyPo> cancelBlekeyInfo = bleApplyService.getCancelBlekeyInfo(projectId, bleAuthCancelDto.getDeviceId(), uid, bleAuthCancelDto.getPhoneNumber());
        if (cancelBlekeyInfo.isPresent()) {
            BleKeyUserCancelVo userBleKeyVo = new BleKeyUserCancelVo();
            BeanUtils.copyProperties(cancelBlekeyInfo.get(), userBleKeyVo);
            return RestResponse.ok(userBleKeyVo);
        } else {
            BleKeyUserCancelVo userBleKeyVo = new BleKeyUserCancelVo();
            userBleKeyVo.setDeviceId(bleAuthCancelDto.getDeviceId());
            userBleKeyVo.setStatus(2);
            return RestResponse.ok(userBleKeyVo);
        }

    }

}
