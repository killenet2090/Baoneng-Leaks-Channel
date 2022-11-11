package com.bnmotor.icv.tsp.cpsp.autodecoration.controller.query;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.output.AutodecorationQueryDetailOutput;
import com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.output.AutodecorationQueryOutput;
import com.bnmotor.icv.tsp.cpsp.autodecoration.service.IAutodecorationQueryService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
* @ClassName: AutodecorationQueryController
* @Description: 汽车美容商家查询控制器
* @author: liuhuaqiao1
* @date: 2021/1/14
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@RestController
@RequestMapping("/v1/merchant")
@Api(tags = {"3.3 商家查询"})
@ApiSort(value = 1)
public class AutodecorationQueryController {

    @Autowired
    private IAutodecorationQueryService autodecorationQueryService;

    @ApiOperation(value = "3.3.1 汽车美容列表查询接口", notes = "汽车美容列表查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adCode", value = "城市区域编码", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "name", value = "商家名称", paramType = "query", dataType = "string", required = false),
            @ApiImplicitParam(name = "sorting", value = "排序方式", paramType = "query", dataType = "string", required = false),
            @ApiImplicitParam(name = "lng", value = "经度", paramType = "query", dataType = "string", required = false),
            @ApiImplicitParam(name = "lat", value = "纬度", paramType = "query", dataType = "string", required = false),
    })
    @GetMapping(value = "/query")
    public ResponseEntity query(@RequestParam("adCode") String adCode,String name,String sorting,String lng,String lat) {
        AutodecorationQueryOutput output = autodecorationQueryService.queryMerchantList(adCode,name,sorting,lng,lat);
        return RestResponse.ok(output);
    }

    @ApiOperation(value = "3.3.2 汽车美容详情查询接口", notes = "汽车美容详情查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchantId", value = "商家名称", paramType = "query", dataType = "string", required = true),
    })
    @GetMapping(value = "/detail")
    public ResponseEntity detail(@RequestParam("merchantId") String merchantId) {
        AutodecorationQueryDetailOutput output = autodecorationQueryService.queryMerchantDetail(merchantId);
        return RestResponse.ok(output);
    }
}
