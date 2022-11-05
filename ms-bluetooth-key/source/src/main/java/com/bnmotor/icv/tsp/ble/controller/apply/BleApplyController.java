package com.bnmotor.icv.tsp.ble.controller.apply;

import com.bnmotor.icv.adam.web.rest.BaseController;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ble.common.constant.Constant;
import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyApplyDto;
import com.bnmotor.icv.tsp.ble.model.request.feign.TokenCheck;
import com.bnmotor.icv.tsp.ble.model.request.feign.TokenDto;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleKeyApplyVo;
import com.bnmotor.icv.tsp.ble.service.BleApplyService;
import com.bnmotor.icv.tsp.ble.service.BleKeyQueryService;
import com.bnmotor.icv.tsp.ble.service.feign.BleCommonFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.Base64;

/**
 * @ClassName: BleApplyController
 * @Description: 车主蓝牙钥匙申请控制器
 * @author: liuyiwei
 * @date: 2020/7/26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
@Api(value = "车主蓝牙钥匙申请", tags = "1.车主蓝牙钥匙申请")
@RestController
@RequestMapping("/v1/ble/vehowner")
public class BleApplyController extends BaseController {
    final Base64.Encoder encoder = Base64.getEncoder();

    @Resource
    private BleApplyService bleApplyService;

    @Resource
    private BleKeyQueryService bleKeyQueryService;

    @Autowired
    private BleCommonFeignService bleCommonFeignService;

    @ApiOperation(value = "6.1.1 车主申请蓝牙钥匙", notes = "车主申请蓝牙钥匙接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID（网关）", required = true, defaultValue = "10019", dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "projectId", value = "项目ID（网关）", required = true, defaultValue = "GX16", dataType = "string", paramType = "header"),
    })
    @PostMapping("/apply")
    public ResponseEntity apply(@RequestHeader("uid") String userId, @RequestHeader("projectId") String projectId, @RequestBody @Validated BleKeyApplyDto keyApplyDto) {
        if (keyApplyDto.getApplyType().intValue()== Constant.COMPARE_EQUAL_VALUE) {
            TokenDto tokenDto = new TokenDto();
            tokenDto.setServicePwd(keyApplyDto.getServicePwd());
            bleCommonFeignService.verifyToken(tokenDto, userId);
        }else {
            TokenCheck tokenCheck = new TokenCheck();
            tokenCheck.setServicePwd(keyApplyDto.getServicePwd());
            bleCommonFeignService.verifyServicePwd(tokenCheck, userId);
        }
        bleCommonFeignService.verifyVehIsOnline(keyApplyDto.getDeviceId());
        //0.校验蓝牙钥匙请求是否合法
        bleKeyQueryService.checkOwnerExpireHisData(projectId,keyApplyDto.getDeviceId(),userId);
        bleApplyService.checkVehOwnerBleKey(projectId, userId, keyApplyDto.getDeviceId(), keyApplyDto.getMobileDeviceId());

        //bleApplyService.queryHasApply(projectId,userId,keyApplyDto);
        bleApplyService.checkVehBleKeyOverflowLimited(projectId, userId, keyApplyDto);

        //1.数据库记录蓝牙钥匙申请， 蓝牙钥匙信息， 关联授权权限（返回bleKeyId）
        UserBleKeyPo userBleKeyPo = bleApplyService.assembleCreateBleKey(projectId,userId,keyApplyDto);
        BleKeyApplyVo bleKeyApplyVo = bleApplyService.saveBleKeyAndPerms(userBleKeyPo);

        return RestResponse.ok(bleKeyApplyVo);
    }

}
