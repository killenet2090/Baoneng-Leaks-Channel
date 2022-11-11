package com.bnmotor.icv.tsp.ota.controller.inner;


import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.aop.aspect.WrapBasePo;
import com.bnmotor.icv.tsp.ota.model.query.FotaPlanQuery;
import com.bnmotor.icv.tsp.ota.model.req.FotaPlanReq;
import com.bnmotor.icv.tsp.ota.model.resp.FotaPlanDetailVo;
import com.bnmotor.icv.tsp.ota.model.resp.FotaPlanVo;
import com.bnmotor.icv.tsp.ota.model.validate.Update;
import com.bnmotor.icv.tsp.ota.service.IFotaPlanService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @ClassName: FotaPlanController
 * @Description: OTA升级计划表 controller层
 * @author xxc
 * @since 2020-07-07
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Api(value="升级任务管理",tags={"升级任务管理"})
@RestController
public class FotaPlanController extends AbstractController {
    
	@Autowired
    private IFotaPlanService fotaPlanService;
    
	@ApiOperation(value = "查询升级任务列表", response = FotaPlanVo.class)
    @GetMapping("/v1/task")
    public ResponseEntity queryPage(FotaPlanQuery query) {
        IPage<FotaPlanVo> iPage = fotaPlanService.queryPage(query);
        if (Objects.nonNull(iPage)) {
            for (FotaPlanVo record : iPage.getRecords()) {
                record.setStartTime(record.getPlanStartTime());
                record.setFinishTime(record.getPlanEndTime());
            }
        }
        return RestResponse.ok(iPage);
    }

    @ApiOperation(value = "查看任务详情", response = FotaPlanDetailVo.class)
    @GetMapping("/v1/task/{planId}")
    public ResponseEntity queryById(@PathVariable Long planId) {
        return RestResponse.ok(fotaPlanService.getFotaPlanDetailVoById(planId));
    }

    @ApiOperation(value = "新增升级任务", response = Long.class)
    @PostMapping("/v1/task")
    @WrapBasePo
    public ResponseEntity insertFotaPlan(@RequestBody @Valid FotaPlanReq fotaPlanReq) {
        return RestResponse.ok(Long.toString(fotaPlanService.insertFotaPlan(fotaPlanReq)));
    }

    @ApiOperation(value = "修改升级任务", response = Boolean.class)
    @PutMapping("/v1/task")
    @WrapBasePo
    public ResponseEntity updateFotaPlan(@RequestBody @Validated(value = Update.class) FotaPlanReq fotaPlanReq) {
        return RestResponse.ok(fotaPlanService.updateFotaPlan(fotaPlanReq));
    }

    @ApiOperation(value = "逻辑删除升级任务", response = Integer.class)
    @DeleteMapping("/v1/task/{planId}")
    @WrapBasePo
    public ResponseEntity deleteById(@PathVariable Long planId) {
        return RestResponse.ok(fotaPlanService.deleteById(planId));
    }

    @ApiOperation(value = "让任务失效", response = Boolean.class)
    @GetMapping("/v1/task/setInvalid")
    @WrapBasePo
    public ResponseEntity setInvalid(@RequestParam Long otaPlanId) {
        return RestResponse.ok(fotaPlanService.setInvalid(otaPlanId));
    }
}
