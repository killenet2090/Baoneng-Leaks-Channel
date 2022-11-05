package com.bnmotor.icv.tsp.ble.controller.manage;

import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.adam.web.rest.BaseController;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ble.common.ParamReader;
import com.bnmotor.icv.tsp.ble.common.RespCode;
import com.bnmotor.icv.tsp.ble.common.constant.Constant;
import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleAuthUpdateDto;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyDto;
import com.bnmotor.icv.tsp.ble.model.request.pki.BleDecrptyDto;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleAuthBleKeyVo;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleReplaceResultVo;
import com.bnmotor.icv.tsp.ble.service.BleKeyReplaceService;
import com.bnmotor.icv.tsp.ble.service.feign.BleCommonFeignService;
import com.bnmotor.icv.tsp.ble.util.ByteUtil;
import com.bnmotor.icv.tsp.ble.util.RedisHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;


/**
 * @ClassName: BleKeyReplaceController
 * @Description: 蓝牙钥匙更新替换
 * @author: liuyiwei
 * @date: 2020/7/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Api(value = "车主更新蓝牙钥匙", tags = {"3.蓝牙钥匙管理"})
@RestController
@RequestMapping("/v1/ble/mgt/key")
public class BleReplaceController extends BaseController {
    @Autowired
    private BleKeyReplaceService bleKeyReplaceService;

    @Resource
    private BleCommonFeignService bleCommonFeignService;

    @Resource
    private RedisHelper redisHelper;

    @Resource
    private ParamReader paramReader;

    @ApiOperation(value = "6.3.7 车主更新替换蓝牙钥匙接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID（网关）", required = true, defaultValue = "10019", paramType = "header", dataType = "string"),
            @ApiImplicitParam(name = "projectId", value = "项目ID（网关）", required = true, defaultValue = "GX16", paramType = "header", dataType = "string"),
    })
    @PostMapping("/replace")
    public ResponseEntity replaceBleKey(@RequestHeader("uid") String userId, @RequestHeader("projectId") String projectId, @Validated @RequestBody BleKeyDto bleKeyDto) {
        bleCommonFeignService.verifyVehIsOnline(bleKeyDto.getDeviceId());
        //0.前置校验
        UserBleKeyPo userBleKeyCondition = UserBleKeyPo.builder()
                .projectId(projectId)
                .deviceId(bleKeyDto.getDeviceId())
                .bleKeyId(bleKeyDto.getBleKeyId())
                .ownerUserId(userId)
                .bleKeyStatus(Constant.ACTIVE_STATUS)
                .delFlag(Constant.INIT_DEL_FLAG)
                .build();
        UserBleKeyPo userBleKeyPo = bleKeyReplaceService.queryBleKeyInfo(userBleKeyCondition);

        long sub = bleKeyReplaceService.querySubBleKeyRefresh(userBleKeyPo);

        if (sub >= Constant.COMPARE_ZERO_VALUE) {
            return RestResponse.error(RespCode.BLE_OVER_SPAN_LIMIT.getValue(), RespCode.BLE_OVER_SPAN_LIMIT.getDescription());
        }

        //1.数据库记录更新蓝牙钥匙

        BleReplaceResultVo bleReplaceResultVo = bleKeyReplaceService.updateBleKey(userBleKeyPo, projectId, userId);

        return RestResponse.ok(bleReplaceResultVo);
    }
    @ApiOperation(value = "车主授权更新蓝牙钥匙接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID（网关）", required = true, defaultValue = "10019", paramType = "header", dataType = "string"),
            @ApiImplicitParam(name = "projectId", value = "项目ID（网关）", required = true, defaultValue = "GX16", paramType = "header", dataType = "string"),
    })
    @PostMapping("/auth/refresh")
    public ResponseEntity authUpdateBleKey(@RequestHeader("uid") String userId, @RequestHeader("projectId") String projectId, @Validated @RequestBody BleAuthUpdateDto bleAuthUpdateDto) {
        bleCommonFeignService.verifyVehIsOnline(bleAuthUpdateDto.getDeviceId());
        //0.前置校验
        String appKey = bleAuthUpdateDto.getDeviceId().concat("_")
                .concat(bleAuthUpdateDto.getMobileDeviceId()).concat("_")
                .concat(String.valueOf(userId)).concat("_1");
        String appJson = redisHelper.getStr(appKey);
        BleDecrptyDto bleDecrptyDto = BleDecrptyDto
                .builder()
                .algorithm(paramReader.algorithm)
                .cipherText(ByteUtil.getBytes(appJson))
                .keyIndex(Integer.parseInt(paramReader.keyId))
                .build();
        String decrptyAppJsons = bleCommonFeignService.pkiDecrpty(bleDecrptyDto);
        String tboxKey = bleAuthUpdateDto.getDeviceId().concat("_")
                .concat(bleAuthUpdateDto.getMobileDeviceId())
                .concat("_").concat(String.valueOf(userId)).concat("_0");
        String tboxJson = redisHelper.getStr(tboxKey);
        bleDecrptyDto.setCipherText(ByteUtil.getBytes(tboxJson));
        String decrptyTboxJsons = bleCommonFeignService.pkiDecrpty(bleDecrptyDto);
        String kafkaKey = bleAuthUpdateDto.getDeviceId().concat("_")
                .concat(bleAuthUpdateDto.getMobileDeviceId())
                .concat("_").concat(String.valueOf(userId)).concat("_2");
        String kafkaJson = redisHelper.getStr(kafkaKey);
        bleDecrptyDto.setCipherText(ByteUtil.getBytes(kafkaJson));
        String decrptykafkaJsons = bleCommonFeignService.pkiDecrpty(bleDecrptyDto);
        bleKeyReplaceService.SendAuthUpdate(decrptyTboxJsons,decrptykafkaJsons);
        try {
            BleAuthBleKeyVo bleAuthBleKeyVo = JsonUtil.toObject(decrptyAppJsons, BleAuthBleKeyVo.class);
            return RestResponse.ok(bleAuthBleKeyVo);
        }catch (Exception ex){
            return RestResponse.error(RespCode.BLE_ACTIVATE_PARSE_ERROR.getValue(), RespCode.BLE_ACTIVATE_PARSE_ERROR.getDescription());
        }
    }

}
