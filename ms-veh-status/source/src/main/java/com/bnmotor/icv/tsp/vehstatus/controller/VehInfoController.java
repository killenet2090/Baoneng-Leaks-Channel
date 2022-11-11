package com.bnmotor.icv.tsp.vehstatus.controller;


import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.vehstatus.aop.annotation.ParamTransfer;
import com.bnmotor.icv.tsp.vehstatus.aop.annotation.ValidRemoteHandle;
import com.bnmotor.icv.tsp.vehstatus.common.ReqContext;
import com.bnmotor.icv.tsp.vehstatus.service.QueryVehStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

/**
 * @ClassName: VehInfoController
 * @Description: 查询车况信息controller
 * @author: huangyun1
 * @date: 2020/5/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Slf4j
@RestController
@RequestMapping("/v1/vehInfo")
@Api(value = "查询车况信息controller", tags = {"查询车况信息查询接口"})
public class VehInfoController {

    @Autowired
    private QueryVehStatusService queryVehStatusService;

    /**
     * 查询车况数据
     * @return
     */
    @ValidRemoteHandle
    @ParamTransfer(columnsParamIdx = 1, groupsParamIdx = 2)
    @GetMapping("/status/get")
    @ApiOperation(value = "查询车况数据")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", name = "uid", dataType = "String", required = true, value = "用户id")})
    public ResponseEntity getStatus(@RequestParam("vin") String vin,
                                    @RequestParam(value = "columnNames", required = false) Set<String> columnNames,
                                    @RequestParam(value = "groupNames", required = false) Set<String> groupNames) {
        if (ReqContext.getUid() == null) {
            return RestResponse.error(RespCode.USER_REQUIRED_PARAMETER_EMPTY);
        }
        boolean emptyQueryCase = (columnNames == null || columnNames.isEmpty()) && (groupNames == null || groupNames.isEmpty());
        if(emptyQueryCase) {
            return RestResponse.error(RespCode.USER_REQUIRED_PARAMETER_EMPTY);
        }
        Map vehStatusMap = queryVehStatusService.queryVehStatusBean(vin, columnNames);
        //todo 判断更新时间是否小于某个时间点 如是：判断车辆睡眠状态 需要下发指令唤醒 返回特定代码
        return RestResponse.ok(vehStatusMap);

    }

}
