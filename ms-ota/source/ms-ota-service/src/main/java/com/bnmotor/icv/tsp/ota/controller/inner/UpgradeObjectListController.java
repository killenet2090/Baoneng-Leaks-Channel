
package com.bnmotor.icv.tsp.ota.controller.inner;


import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.aop.aspect.WrapBasePo;
import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import com.bnmotor.icv.tsp.ota.model.req.UpgradeObjectListReq;
import com.bnmotor.icv.tsp.ota.model.resp.AddFotaPlanResultVo;
import com.bnmotor.icv.tsp.ota.model.validate.Update;
import com.bnmotor.icv.tsp.ota.service.IFotaPlanObjListService;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <pre>
 *  OTA升级计划对象清单
定义一次升级中包含哪些需要升级的车辆 Api,控制业务流程.
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
 *
 * @ClassName:
 * @Description: 升级任务对象清单控制器
 * @author: jiankang
 * @date: 2020/4/23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能;;汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Api(value="升级对象(车辆)清单管理",tags={"升级对象(车辆)清单管理"})
@RestController
public class UpgradeObjectListController {
    
	@Autowired
    IFotaPlanObjListService fotaPlanObjListService;

    @ApiOperation(value = "新增升级任务对象(车辆)清单", response = AddFotaPlanResultVo.class)
    @PostMapping("/v1/upgradeTaskObjList")
    @WrapBasePo
    public ResponseEntity saveUpgradeTaskObjList(@RequestBody @Valid UpgradeObjectListReq upgradeObjectListReq) {
        AddFotaPlanResultVo addFotaPlanResultVo = fotaPlanObjListService.insertUpgradeTaskObjectList(upgradeObjectListReq);
        if(MyCollectionUtil.isNotEmpty(addFotaPlanResultVo.getExistValidPlanObjVos())){
            return RestResponse.error(OTARespCodeEnum.FOTA_OBJECT_IN_ANOTHORE_PLAN.getCode(), addFotaPlanResultVo.toString());
        }
        return RestResponse.ok(addFotaPlanResultVo);
    }

    @ApiOperation(value = "OTA升级计划对象清单 定义一次升级中包含哪些需要升级的车辆更新", response = AddFotaPlanResultVo.class)
    @PutMapping("/v1/upgradeTaskObjList")
    @WrapBasePo
    public ResponseEntity updateFotaPlanObjList(@RequestBody @Validated(value = Update.class) UpgradeObjectListReq upgradeObjectListReq) {
        AddFotaPlanResultVo addFotaPlanResultVo = fotaPlanObjListService.updateFotaPlanObjList(upgradeObjectListReq);
        if(MyCollectionUtil.isNotEmpty(addFotaPlanResultVo.getExistValidPlanObjVos())){
            return RestResponse.error(OTARespCodeEnum.FOTA_OBJECT_IN_ANOTHORE_PLAN.getCode(), addFotaPlanResultVo.toString());
        }
        return RestResponse.ok(addFotaPlanResultVo);
    }
}
