package com.bnmotor.icv.tsp.common.data.controller.inner;


import com.bnmotor.icv.adam.data.mysql.metadata.Page;
import com.bnmotor.icv.adam.data.mysql.metadata.PageRequest;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.common.data.common.validation.group.UpdateGroup;
import com.bnmotor.icv.tsp.common.data.model.request.GeoQueryVo;
import com.bnmotor.icv.tsp.common.data.model.request.GeoReqVo;
import com.bnmotor.icv.tsp.common.data.model.response.GeoVo;
import com.bnmotor.icv.tsp.common.data.model.response.view.AppView;
import com.bnmotor.icv.tsp.common.data.model.response.view.MgtView;
import com.bnmotor.icv.tsp.common.data.service.IGeoService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: GeoDo
 * @Description: 地理位置信息表 controller层 内部接口
 * @author zhangjianghua1
 * @since 2020-07-10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@RestController
@RequestMapping("/inner/geo")
public class GeoInnerController {
    private final IGeoService geoService;

    public GeoInnerController(IGeoService geoService) {
        this.geoService = geoService;
    }

    @JsonView(MgtView.class)
    @GetMapping
    public ResponseEntity directChildren(PageRequest pageRequest, GeoQueryVo query){
        Page<GeoVo> geoList = geoService.findAll(pageRequest, query);
        return RestResponse.ok(geoList);
    }

    @ApiOperation(value = "获取地理位置详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "中国行政区划编码", required = false, paramType = "query", dataType = "string")
    })
    @JsonView(MgtView.class)
    @GetMapping("single")
    public ResponseEntity geoDetails(@RequestParam("code")String code){
        GeoVo geo = geoService.getByCode(code);
        return RestResponse.ok(geo);
    }

    @ApiOperation(value = "批量获取地理位置列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "中国行政区划编码", required = false, paramType = "query", dataType = "string")
    })
    @JsonView(MgtView.class)
    @GetMapping("list")
    public ResponseEntity geoList(@RequestParam("codes") List<String> codes){
        Assert.isTrue(codes.size()<=50, "请求参数态多");
        List<GeoVo> geoList = geoService.getByCode(codes);
        return RestResponse.ok(geoList);
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable("id") Long id,
                                 @RequestBody @Validated(UpdateGroup.class) GeoReqVo geoReqVo){
        boolean result = geoService.update(id, geoReqVo);
        if(result){
            //TODO 发送Kafka消息,清理中国行政区域缓存
        }
        return RestResponse.ok("更新成功");
    }
}
