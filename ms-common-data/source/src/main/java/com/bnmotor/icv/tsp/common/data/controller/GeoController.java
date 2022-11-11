package com.bnmotor.icv.tsp.common.data.controller;


import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.common.data.common.Constants;
import com.bnmotor.icv.tsp.common.data.model.response.GeoCityVo;
import com.bnmotor.icv.tsp.common.data.model.response.GeoVo;
import com.bnmotor.icv.tsp.common.data.model.response.view.AppView;
import com.bnmotor.icv.tsp.common.data.service.IGeoService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: GeoDo
 * @Description: 地理位置信息表 controller层
 * @author zhangjianghua1
 * @since 2020-07-10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/v1/geo")
public class GeoController {

    private List<GeoVo> CHINA_REGION_TREE = new ArrayList<>();
    private final Object lockObj = new Object();

    private final IGeoService geoService;

    public GeoController(IGeoService geoService) {
        this.geoService = geoService;
    }

    @ApiOperation(value = "获取中国行政区划数据(树形结构)，如果没传编码，默认中国所有行政区域")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "中国行政区划编码", required = false, paramType = "query", dataType = "string")
    })
    @JsonView(AppView.class)
    @GetMapping("/cn/tree/list")
    public ResponseEntity chinaTreeList(String code){
        if(StringUtils.isEmpty(code) || Constants.CHINA_GEO_CODE.equals(code)) {
            if (CollectionUtils.isEmpty(CHINA_REGION_TREE)) {
                synchronized (lockObj) {
                    if (CollectionUtils.isEmpty(CHINA_REGION_TREE)) {
                        CHINA_REGION_TREE = geoService.getTreeList(Constants.CHINA_GEO_ID);
                    }
                }
            }
            return RestResponse.ok(CHINA_REGION_TREE);
        }
        Pattern pattern = Pattern.compile(Constants.REGEX_CHINA_GEO_CODE);
        Matcher m = pattern.matcher(code);
        Assert.isTrue(m.matches(), "中国行政区划编码格式错误");
        return RestResponse.ok(geoService.getTreeList(code));
    }

    @PutMapping("/tree/list")
    public ResponseEntity updateChinaGeoListCache(@RequestParam("code") String code){
        return RestResponse.ok(geoService.getTreeList(code));
    }

    @JsonView(AppView.class)
    @GetMapping("/tree/list")
    public ResponseEntity treeList(@RequestParam("code") String code){
        return RestResponse.ok(geoService.getTreeList(code));
    }

    @ApiOperation(value = "获取地理位置详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "中国行政区划编码", required = false, paramType = "query", dataType = "string")
    })
    @JsonView(AppView.class)
    @GetMapping
    public ResponseEntity geoDetails(@RequestParam("code")String code){
        GeoVo geo = geoService.getByCode(code);
        return RestResponse.ok(geo);
    }
    @GetMapping("/last/modified")
    public ResponseEntity getGeoLastModified(){
        return RestResponse.ok(geoService.getLastModified());
    }

    @ApiOperation(value = "模糊查询获取地理位置详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "搜索关键字， 支持城市名、区/县名，区域编码", required = true, paramType = "query", dataType = "string")
    })
    @JsonView(AppView.class)
    @GetMapping("/city/lookup")
    public ResponseEntity geoListDetails(@RequestParam("keyword")String keyword){
        List<GeoCityVo> geo = geoService.getByKeyword(keyword);
        return RestResponse.ok(geo);
    }
}
