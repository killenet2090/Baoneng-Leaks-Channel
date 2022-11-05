package com.bnmotor.icv.tsp.ble.controller.manage;

import com.bnmotor.icv.adam.sdk.bluetooth.service.BluetoothDownService;
import com.bnmotor.icv.adam.web.rest.BaseController;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ble.common.constant.Constant;
import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyDto;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyQueryDto;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleUidKeyDto;
import com.bnmotor.icv.tsp.ble.model.request.pki.BleEncryptDto;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleKeyQueryVo;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleUidKey;
import com.bnmotor.icv.tsp.ble.model.response.ble.UserBleKeyVo;
import com.bnmotor.icv.tsp.ble.model.response.ble.UserBlekeyHisVo;
import com.bnmotor.icv.tsp.ble.service.BleApplyService;
import com.bnmotor.icv.tsp.ble.service.BleKeyQueryService;
import com.bnmotor.icv.tsp.ble.service.feign.BleCommonFeignService;
import com.bnmotor.icv.tsp.ble.service.mq.BleKafkaPushMsg;
import com.bnmotor.icv.tsp.ble.util.ByteUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * @ClassName: BleQueryController
 * @Description: 蓝牙钥匙查询控制器
 * @author: liuyiwei
 * @date: 2020/7/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Slf4j
@Api(value = "蓝牙钥匙查询", tags = "3.蓝牙钥匙管理")
@RestController
@RequestMapping("/v1/ble/mgt")
public class BleQueryController extends BaseController {
    @Autowired
    private BleKeyQueryService bleKeyQueryService;

    @Autowired
    private BleApplyService bleApplyService;

    @Resource
    private BleCommonFeignService bleCommonFeignService;


    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID（网关）", required = true, defaultValue = "10019", dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "projectId", value = "项目ID（网关）", required = true, defaultValue = "1", dataType = "string", paramType = "header"),
    })
    @ApiOperation(value = "6.3.1 车主查询蓝牙钥匙接口", notes = "车主查询蓝牙钥匙接口")
    @GetMapping(value = "/vehowner/query")
    public ResponseEntity queryVehOwnerBleKey(@RequestHeader("uid") String uid, @RequestHeader("projectId") String projectId, @Validated BleKeyQueryDto queryDto) {
        bleKeyQueryService.checkExpireHisData(projectId, queryDto.getDeviceId());
        List<BleKeyQueryVo> queryVos = bleKeyQueryService.queryByVehOwner(uid, projectId, queryDto);
        return RestResponse.ok(queryVos);
    }


    @ApiOperation(value = "车主查询tbox端指定蓝牙钥匙接口", notes = "车主查询蓝牙钥匙接口")
    @PostMapping(value = "/vehowner/check/device/query")
    public ResponseEntity queryCheckVehOwnerBleKey(@RequestHeader("uid") String uid, @RequestHeader("projectId") String projectId, @RequestBody @Validated BleKeyQueryDto queryDto) {
        bleKeyQueryService.checkExpireHisData(projectId, queryDto.getDeviceId());
        BleKeyQueryDto result = bleKeyQueryService.queryCheckByVehOwner(uid, projectId, queryDto);
        return RestResponse.ok(result);
    }


    @ApiOperation(value = "6.3.2 车主授权查询查询蓝牙钥匙授权记录", notes = "车主查询已经完成的蓝牙钥匙授权记录")
    @RequestMapping(value = "/authorised/query", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", name = "BleKeyinfoDto", value = "蓝牙钥匙入参实体", defaultValue = "", required = true, dataType = "BleKeyinfoDto"),
            @ApiImplicitParam(paramType = "header", name = "uid", value = "用户的id", defaultValue = "", required = true, dataType = "String")
    })
    public ResponseEntity blekeyAuthQueryApp(@RequestHeader("uid") String uid, @RequestHeader("projectId") String projectId, @Validated @RequestParam("deviceId") String deviceId) {
        bleKeyQueryService.checkExpireHisData(projectId, deviceId);
        List<UserBleKeyVo> dbBlekeyInfo = bleApplyService.ownerQueryBorryDbBlekeyInfo(projectId, deviceId, uid);
        return RestResponse.ok(dbBlekeyInfo);
    }

    @ApiOperation(value = "6.3.3 被授权人查询蓝牙钥匙已借钥匙记录", notes = "被授权人查询蓝牙钥匙已借钥匙记录")
    @RequestMapping(value = "/borrybleKey/query", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "uid", value = "用户的id", defaultValue = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "projectId", value = "项目的id", defaultValue = "1", required = true, dataType = "String")
    })
    public ResponseEntity blekeyBeAuthQueryApp(@RequestHeader("uid") String uid, @RequestHeader("projectId") String projectId,
                                               @Validated @RequestParam("usedMobileDeviceId") String usedMobileDeviceId) {
        List<UserBleKeyVo> userBleKeyVoList = bleApplyService.getBorryDbBlekeyInfo(projectId, uid, usedMobileDeviceId);
        if (Optional.ofNullable(userBleKeyVoList).isPresent() && userBleKeyVoList.size() > 0) {
            bleKeyQueryService.checkExpireHisData(projectId, userBleKeyVoList.get(0).getDeviceId());
            userBleKeyVoList = bleApplyService.getBorryDbBlekeyInfo(projectId, uid, usedMobileDeviceId);
        }
        return RestResponse.ok(userBleKeyVoList);
    }

    @ApiOperation(value = "从数据库查询车下拥有的蓝牙钥匙数量列表", notes = "从数据据库查询车下拥有的蓝牙钥匙数量列表")
    @RequestMapping(value = "/device/query", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "uid", value = "用户的id", defaultValue = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "projectId", value = "项目的id", defaultValue = "1", required = true, dataType = "String")
    })
    public ResponseEntity deviceBlekeyQueryApp(@RequestHeader("uid") String uid, @RequestHeader("projectId") String projectId, @Validated @RequestParam("deviceId") String deviceId) {
        List<UserBlekeyHisVo> userBleKeyPoList = bleApplyService.checkDeviceBleCount(projectId, deviceId);
        return RestResponse.ok(userBleKeyPoList);
    }


    @ApiOperation(value = "从车端某把指定蓝牙钥匙接口，检查钥匙是否存在", notes = "从车端查询某把指定的钥匙接口，检查钥匙是否存在")
    @PostMapping(value = "/check/blekey/query")
    public ResponseEntity queryCheckSpeciVehOwnerBleKey(@RequestHeader("uid") String uid, @RequestHeader("projectId") String projectId,@RequestBody @Validated BleKeyDto queryDto) {
        BleKeyDto result = bleKeyQueryService.queryCheckSpeciByVehOwner(uid, projectId, queryDto);
        return RestResponse.ok(result);
    }

    @RequestMapping(value = "/check/device/query", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "uid", value = "用户的id", defaultValue = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "projectId", value = "项目的id", defaultValue = "1", required = true, dataType = "String")
    })
    @ApiOperation(value = "从车端（tbox）查询蓝牙钥匙列表", notes = "从车端（tbox）查询蓝牙钥匙列表")
    public ResponseEntity blekeyTboxApplyApp(@RequestHeader("uid") String uid, @RequestHeader("projectId") String projectId, @Validated @RequestParam("deviceId") String deviceId) {
        String result = bleKeyQueryService.queryCheckAllByVehOwner(projectId, uid, deviceId);
        return RestResponse.ok(result);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "uid", value = "用户的id", defaultValue = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "projectId", value = "项目的id", defaultValue = "1", required = true, dataType = "String")
    })
    @ApiOperation(value = "车辆绑定解绑需求：根据用户ids查询蓝牙钥匙记录", notes = "车辆绑定解绑需求：根据用户ids查询蓝牙钥匙记录")
    @PostMapping(value = "/uids/blekeys")
    public ResponseEntity blekeysQuery(@RequestHeader("uid") String uid, @RequestHeader("projectId") String projectId,  @RequestBody @Validated BleUidKeyDto bleUidKeyDto) {
        bleUidKeyDto.setProjectId(projectId);
        List<BleUidKey> bleKeyQueryVos = bleKeyQueryService.queryBleUids(bleUidKeyDto);
        return RestResponse.ok(bleKeyQueryVos);
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "uid", value = "用户的id", defaultValue = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "projectId", value = "项目的id", defaultValue = "1", required = true, dataType = "String")
    })
    @ApiOperation(value = "测试接口", notes = "测试接口")
    public ResponseEntity<?> blekeyTest(@RequestHeader("uid") String uid, @RequestHeader("projectId") String projectId) {
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));

        UserBleKeyPo userBleKeyVo = UserBleKeyPo.builder().bleKeyEffectiveTime(now).build();
        String bleKey="camhigtdyycsryubhgyb123";
        BleEncryptDto bleEncryptDto =  BleEncryptDto.builder()
                .algorithm(Constant.ALGORITHM)
                .content(ByteUtil.getBytes(bleKey))
                .deviceId("8680346032528290")
                .build();
        //app端蓝牙钥匙用app ID
        byte[] byteAppBleKey = bleCommonFeignService.pkiAsymmetricEncrypt(bleEncryptDto);
        System.out.println(byteAppBleKey);
        String encryAppBleKey = Hex.toHexString(byteAppBleKey);

        return RestResponse.ok(encryAppBleKey);
    }
}
