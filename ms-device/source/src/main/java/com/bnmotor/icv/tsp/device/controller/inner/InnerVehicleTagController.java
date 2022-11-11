package com.bnmotor.icv.tsp.device.controller.inner;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.model.request.tag.EditVehTagDto;
import com.bnmotor.icv.tsp.device.service.IVehTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: InnerVehicleTagController
 * @Description: 车辆标签维护, 提供管理后台进行标签导入，标签录入，标签修改，标签删除等功能接口；已经对车辆进行打标签，修改标签等功能
 * @author: zhangwei2
 * @date: 2020/7/5
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/inner/vehTag")
@Api(value = "管理车辆标签接口", tags = {"管理车辆标签相关接口"})
@Slf4j
public class InnerVehicleTagController {
    @Autowired
    private IVehTagService vehTagService;

    @PostMapping(value = "/create")
    @ApiOperation(value = "新增车辆标签接口")
    public ResponseEntity addVehTag(@RequestBody @Validated EditVehTagDto tagDto) {
        vehTagService.addVehTag(tagDto);
        return RestResponse.ok(null);
    }

    @GetMapping("/listAttributeTag")
    @ApiOperation(value = "查询车辆属性标签")
    public ResponseEntity listAttributeTag() {
        return RestResponse.ok(null);
    }
}
