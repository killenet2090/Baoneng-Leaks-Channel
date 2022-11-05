package com.bnmotor.icv.tsp.ble.controller.auth;

import com.bnmotor.icv.adam.core.utils.StringUtil;
import com.bnmotor.icv.adam.web.rest.BaseController;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ble.common.RespCode;
import com.bnmotor.icv.tsp.ble.common.constant.Constant;
import com.bnmotor.icv.tsp.ble.mapper.BleKeyUserMapper;
import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleAuthDto;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyDto;
import com.bnmotor.icv.tsp.ble.model.response.ble.*;
import com.bnmotor.icv.tsp.ble.model.response.user.UserPhoneVo;
import com.bnmotor.icv.tsp.ble.model.response.user.UserVo;
import com.bnmotor.icv.tsp.ble.service.BleApplyService;
import com.bnmotor.icv.tsp.ble.service.BleAuthService;
import com.bnmotor.icv.tsp.ble.service.BleUserService;
import com.bnmotor.icv.tsp.ble.service.feign.BleCommonFeignService;
import com.bnmotor.icv.tsp.ble.util.ParamHandleUtil;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
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
import java.util.Optional;
/**
 * @ClassName: BleAuthorizeController
 * @Description: 蓝牙钥匙授权业务Controller入口
 * @author: shuqi1
 * @date: 2020/6/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/v1/ble/auth")
@Api(value = "车主对被被授权人进行蓝牙钥匙授权接口", tags = "2.蓝牙钥匙授权申请")
@ApiSort(value = 2)
public class BleAuthorizeController extends BaseController {
    @Autowired
    private BleAuthService bleAuthService;

    @Autowired
    private BleUserService bleUserService;

    @Resource
    private BleKeyUserMapper bleKeyUserMapper;

    @Autowired
    private BleCommonFeignService bleCommonFeignService;

    @Resource
    private BleApplyService bleApplyService;

    @PostMapping(value = "/vehowner/apply")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "uid", value = "用户的id", defaultValue = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "projectId", value = "项目id", defaultValue = "", required = true, dataType = "String")
    })
    @ApiOperation(value = "蓝牙钥匙授权码生成接口", notes = "蓝牙钥匙授权码生成接口")
    public ResponseEntity blekeyAuthCodeCreateApp(@RequestHeader("uid") String uid, @RequestHeader("projectId") String projectId,
                                                  @Validated @RequestBody BleAuthDto bleAuthDto){
        //bleCommonFeignService.verifyVehIsOnline(bleAuthDto.getDeviceId());

       /**
        TokenCheck tokenCheck = new TokenCheck();
        tokenCheck.setServicePwd(bleAuthDto.getServicePwd());
        bleCommonFeignService.verifyServicePwd(tokenCheck, uid);
        **/
        LocalDateTime dt = LocalDateTime.now();
        Date nowSrc = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        /**
         * 检查用户类型是否合法，不能对车主自己授权
         */
        String ownerName = bleUserService.getUserName(bleAuthDto.getOwnerId()).getNickname();
        UserPhoneVo userPhoneVo = bleCommonFeignService.bleQueryUserPushIdVo(uid, bleAuthDto.getPhoneNumber());
        VehicleInfoVo vehicleInfoVo = bleCommonFeignService.bleGetFeignVehicleInfoVo(bleAuthDto.getDeviceId());
        UserVo tspUserVo = bleAuthService.queryTspUserVo(bleAuthDto.getPhoneNumber(), uid);
        /**
         * 查询书库中记录
         */
        bleAuthService.getDbHasAuthCondition(dt,nowSrc,projectId, tspUserVo, bleAuthDto);
        /**
         * 查询书库中记录
         */
        //bleAuthService.getDbHasConfirmCondition(projectId, String.valueOf(tspUserVo.getUid()), bleAuthDto);
        bleAuthService.queryUserNameLimit(projectId, uid, bleAuthDto, userPhoneVo);
        bleApplyService.checkDeviceBleOverflowLimited(projectId, bleAuthDto);

        /**
         * 生成授权码
         */
        String authCode = bleAuthService.generateAuthCode();

        /**
         *发送通知消息
         */
        //bleAuthService.assemblePushMessageBleAuth(tspUserVo,projectId,bleAuthDto, userPhoneVo,vehicleInfoVo,ownerName);


        //bleCommonFeignService.SendSms(bleAuthDto, vehicleInfoVo, projectId, userName, authCode);
        /**
         * 查询车辆信息
         */
        if (vehicleInfoVo.getVin() == null || vehicleInfoVo.getVin().equals(StringUtil.EMPTY_STRING)) {
            return RestResponse.error(RespCode.AUTH_DEVICE_CHECK_ERROR.getValue(),
                    RespCode.AUTH_DEVICE_CHECK_ERROR.getDescription());
        }

        /**
         * 保存授权权限
         */
        BleAuthBleKeyVo bleAuthBleKeyVo  = bleAuthService.saveBleAuthRight(dt,nowSrc,projectId,bleAuthDto,vehicleInfoVo,userPhoneVo,tspUserVo,authCode,uid);

        return RestResponse.ok(bleAuthBleKeyVo);
    }

    @GetMapping(value = "/authorising/query")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "phoneNumber", value = "被授权人手机号码", defaultValue = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "uid", value = "用户的id", defaultValue = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "projectId", value = "项目ID", defaultValue = "", required = true, dataType = "String")
    })
    @ApiOperation(value = "蓝牙钥匙授权未确认查询接口", notes = "蓝牙钥匙授权未确认查询接口")
    public ResponseEntity blekeyAuthorisingQueryApp(@RequestHeader("uid") String uid, @RequestHeader("projectId") String projectId, @Validated @RequestParam("phoneNumber") String phoneNumber) throws ParseException {
        /**
         * 判断手机号码是否合法
         */
        boolean bok = ParamHandleUtil.isPhoneRegexp(phoneNumber);
        /**
         * 如果不合法，返回提示信息
         */
        if (!bok) {
            return RestResponse.error(RespCode.AUTH_PHONENUMBER_CHECK_ERROR.getValue(), RespCode.AUTH_PHONENUMBER_CHECK_ERROR.getDescription());
        }

        /**
         *查询授权人没有确认的记录
         */
        List<BleAuthingVo> bleAuthingVo = bleAuthService.blekeyAuthorisingQueryService(projectId, uid, phoneNumber);

        return RestResponse.ok(bleAuthingVo);
    }


    @RequestMapping(value = "/authorising/active", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "uid", value = "用户的id", defaultValue = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "projectId", value = "项目id", defaultValue = "", required = true, dataType = "String")
    })
    @ApiOperation(value = "被授权人通过确认创建蓝牙钥匙", notes = "被授权人通过确认创建蓝牙钥匙接口")
    public ResponseEntity blekeyAuthActiveApp(@RequestHeader("uid") String uid, @RequestHeader("projectId") String projectId,
                                              @Validated @RequestBody BleKeyDto bleKeyDto){
        /**
         * 根据传入的参数进行确认流程
         */
        UserBleKeyPo userBleKeyPo = UserBleKeyPo.builder()
                .projectId(projectId)
                .deviceId(bleKeyDto.getDeviceId())
                .bleKeyId(bleKeyDto.getBleKeyId())
                .usedUserId(uid)
                .delFlag(Constant.INIT_DEL_FLAG)
                .build();
        UserBleKeyPo userBleKeyPoQuery = bleKeyUserMapper.queryBleKeyInfoByPrimary(userBleKeyPo);
        BleKeyActiveVo bleKeyActiveVo = BleKeyActiveVo.builder().build();

        if (!Optional.ofNullable(userBleKeyPoQuery).isPresent()) {
            return RestResponse.error(RespCode.BLE_BLEKEY_NOT_FOUND.getValue(),
                    RespCode.BLE_BLEKEY_NOT_FOUND.getDescription());
        }
        BeanUtils.copyProperties(userBleKeyPoQuery, bleKeyActiveVo);
        if (userBleKeyPoQuery.getBleKeyStatus().equals(Constant.ACTIVE_STATUS)){
            return RestResponse.ok(bleKeyActiveVo);
        }
        bleAuthService.activeOfflineAuthBleKey(userBleKeyPoQuery);
        bleKeyActiveVo.setBleKeyStatus(Constant.ACTIVE_STATUS);
        return RestResponse.ok(bleKeyActiveVo);
    }
}
