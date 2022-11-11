package com.bnmotor.icv.tsp.device.controller.inner;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bnmotor.icv.adam.data.mysql.metadata.PageRequest;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.model.response.vehModel.VehConfigDetailVo;
import com.bnmotor.icv.tsp.device.model.response.vehModel.VehModelConfigVo;
import com.bnmotor.icv.tsp.device.model.response.vehModel.VehModelStatisticsVo;
import com.bnmotor.icv.tsp.device.service.IVehModelService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: MgtVehicleModelController
 * @Description: 车型管理
 * @author: zhangwei2
 * @date: 2020/7/12
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/inner")
@Api(value = "车型管理接口,维护,提供车型统计，车型配置查询等功能", tags = {"车辆型号管理"})
@Slf4j
public class InnerVehicleModelController {
    @Autowired
    private IVehModelService modelService;

    @GetMapping(value = {"/vehModel", "/model/list"})
    @ApiOperation(value = "车辆型号查询,返回指定车系下所有型号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "seriesId", value = "车系id", dataType = "Long", paramType = "query")
    })
    public ResponseEntity queryModels(@RequestParam(value = "seriesId", required = false) Long seriesId) {
        return RestResponse.ok(modelService.listModels(seriesId));
    }

    @GetMapping("/vehModel/statistics")
    @ApiOperation(value = "获取车辆型号统计数据")
    public ResponseEntity queryVehicles(@ApiParam(value = "车辆类型,1-燃油，2-纯电动,3-混动") @RequestParam(required = false) Integer vehType, PageRequest pageRequest) {
        IPage<VehModelStatisticsVo> vehicles = modelService.listStatistics(vehType, pageRequest);
        return RestResponse.ok(vehicles);
    }

    @GetMapping("/vehModel/models")
    @ApiOperation(value = "获取某型号下车辆配置列表")
    public ResponseEntity listModelDetail(@RequestParam(value = "modelCode") String modelCode,
                                          @RequestParam(value = "vehType", required = false) Integer vehType) {
        List<VehModelConfigVo> detailVos = modelService.listModelConfig(modelCode, vehType);
        return RestResponse.ok(detailVos);
    }

    @GetMapping("/vehModel/configDetail/{configId}")
    @ApiOperation(value = "获取某型号下车辆详情")
    public ResponseEntity getVehConfigDetail(@PathVariable(value = "configId") Long configId) {
        VehConfigDetailVo detailVo = modelService.queryVehConfigDetail(configId);
        return RestResponse.ok(detailVo);
    }

    @GetMapping(value = "/vehicle/level")
    @ApiOperation(value = "车辆层级,用户获取全部车辆品牌,车系,车型信息,数据将用平铺的形式给出")
    public ResponseEntity queryLevel() {
        return RestResponse.ok(modelService.queryLevel());
    }
}

