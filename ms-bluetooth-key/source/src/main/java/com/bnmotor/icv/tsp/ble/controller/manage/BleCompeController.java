package com.bnmotor.icv.tsp.ble.controller.manage;

import com.bnmotor.icv.adam.web.rest.BaseController;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ble.common.ParamReader;
import com.bnmotor.icv.tsp.ble.common.RespCode;
import com.bnmotor.icv.tsp.ble.model.request.pki.BleImportDto;
import com.bnmotor.icv.tsp.ble.service.BleAuthCompeService;
import com.bnmotor.icv.tsp.ble.service.feign.BleCommonFeignService;
import com.bnmotor.icv.tsp.ble.util.RandomUnit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @ClassName: BleCompeController
 * @Description: 权限查询接口
 * @author: shuqi1
 * @date: 2020/6/25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/v1/ble/mgt")
@Api(value = "查询所有权限列表", tags = {"3.蓝牙钥匙管理"}, description = "权限查询")
public class BleCompeController extends BaseController {
    @Autowired
    BleAuthCompeService bleAuthCompeService;
    
    @Autowired
    BleCommonFeignService bleCommonFeignService;

    @Autowired
    ParamReader paramReader;


    @GetMapping(value = "/compe/query")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "uid", value = "用户的id", defaultValue = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "projectId", value = "项目id", defaultValue = "", required = true, dataType = "String"),
    })
    @ApiOperation(value = "查询所有权限列表", notes = "查询所有权限列表")
    public ResponseEntity blekeyCompeQueryApp(@RequestHeader("uid") String uid, @RequestHeader("projectId") String projectId) {
        Map<Long, String> map = bleAuthCompeService.queryAllCompe();
        if (map != null) {
            return responseSuccess(map);
        } else {
            return RestResponse.error(RespCode.AUTH_COMPE_QUERY_ERROR.getValue(),RespCode.AUTH_COMPE_QUERY_ERROR.getDescription());
        }

    }

    @PostMapping(value = "/pki/import")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "uid", value = "用户的id", defaultValue = "", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "projectId", value = "项目id", defaultValue = "", required = true, dataType = "String"),
    })
    @ApiOperation(value = "查询所有权限列表", notes = "查询所有权限列表")
    public ResponseEntity blekeyPkiImportApp(@RequestHeader("uid") String uid, @RequestHeader("projectId") String projectId) {
        BleImportDto bleImportDto = BleImportDto
                .builder()
                .groupId("Group-2")
                .keyId(31)
                .key(RandomUnit.getSecureRandom())
                .build();
        boolean res = bleCommonFeignService.pkiImportKey(bleImportDto);
        return RestResponse.ok(res);

    }
}
