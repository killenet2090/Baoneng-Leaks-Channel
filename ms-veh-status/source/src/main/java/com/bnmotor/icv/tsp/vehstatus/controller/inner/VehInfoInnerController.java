package com.bnmotor.icv.tsp.vehstatus.controller.inner;


import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.vehstatus.aop.annotation.ParamTransfer;
import com.bnmotor.icv.tsp.vehstatus.service.QueryVehStatusService;
import io.swagger.annotations.Api;
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
@RequestMapping("/inner/vehInfo")
@Api(value = "运营后台-查询车况信息controller", tags = {"运营后台接口-查询车况信息"})
public class VehInfoInnerController {

    @Autowired
    private QueryVehStatusService queryVehStatusService;

    /**
     * 查询车况数据
     * @return
     */
    @GetMapping("/status/get")
    @ParamTransfer(columnsParamIdx = 1, groupsParamIdx = 2)
    @ApiOperation(value = "查询车况数据")
    public ResponseEntity getStatus(@RequestParam("vin") String vin,
                                    @RequestParam(value = "columnNames", required = false) Set<String> columnNames,
                                    @RequestParam(value = "groupNames", required = false) Set<String> groupNames) {
        //如果不传任何查询字段则返回空
        boolean emptyParameter = (columnNames == null || columnNames.isEmpty()) && (groupNames == null || groupNames.isEmpty());
        if(emptyParameter) {
            return RestResponse.ok(null);
        }
        Map vehStatusMap = queryVehStatusService.queryVehStatusBean(vin, columnNames);
        //todo 判断更新时间是否小于某个时间点 如是：判断车辆睡眠状态 需要下发指令唤醒 返回特定代码
        return RestResponse.ok(vehStatusMap);
    }

}
