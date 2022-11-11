
package com.bnmotor.icv.tsp.ota.controller.inner;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.model.req.UpgradeFirmwareListReq;
import com.bnmotor.icv.tsp.ota.model.validate.Update;
import com.bnmotor.icv.tsp.ota.service.IFotaPlanFirmwareListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <pre>
 *  该表用于定义一个OTA升级计划中需要升级哪几个软件
包含：
     1. 依赖的软件清单
                                               - Api,控制业务流程.
 *  提供包括业务流程校验,业务流程控制.
 *
 *  <b>地址规则</b>
 *   列表或分页:GET /persons
 *   单人: GET /persons/1
 *   新增: POST /persons
 *   修改: PUT /persons
 *   删除: DELETE /persons/1
 *   关联
 *   列表 GET /persons/1/dogs
 *   单狗 GET /persons/1/dogs/2
 *   增加狗 POST /persons/1/dogs
 *   修改狗 PUT /persons/1/dogs
 *   删除狗 DELETE /persons/1/dogs/2
 * </pre>
 * @ClassName:
 * @Description: 升级任务固件清单控制器
 * @author: jiankang
 * @date: 2020/4/23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Api(value="升级固件清单管理",tags={"升级固件清单管理"})
@RestController
@AllArgsConstructor
public class UpgradeFirmwareListController /*extends BaseController*/ {

    /**
     1. 升级软件清单-服务接口
     */
    private final IFotaPlanFirmwareListService fotaPlanFirmwareListService;

    @ApiOperation(value = "新增升级任务固件清单", response = Boolean.class)
    @PostMapping("/v1/upgradeFirmwareList")
    @ApiImplicitParam(name = "upgradeFirmwareListReq", value = "upgradeFirmwareListReq", required = true, dataType = "UpgradeFirmwareListReq")
    public ResponseEntity save(@RequestBody @Valid UpgradeFirmwareListReq upgradeFirmwareListReq) {
        return RestResponse.ok(fotaPlanFirmwareListService.insertUpgradeFirmwareList(upgradeFirmwareListReq));
    }

    @ApiOperation(value = "更新升级任务固件清单", response = Boolean.class)
    @PutMapping("/v1/upgradeFirmwareList")
    public ResponseEntity updateFotaPlanFirmwareList(@RequestBody @Validated(value = Update.class) UpgradeFirmwareListReq upgradeFirmwareListReq) {
        return RestResponse.ok(fotaPlanFirmwareListService.updateFotaPlanFirmwareList(upgradeFirmwareListReq));
    }
}
