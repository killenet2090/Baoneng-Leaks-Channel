package com.bnmotor.icv.tsp.ota.controller.inner;


import java.util.Collections;
import java.util.List;

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

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.aop.aspect.WrapBasePo;
import com.bnmotor.icv.tsp.ota.common.MyRestResponse;
import com.bnmotor.icv.tsp.ota.common.enums.WebEnums;
import com.bnmotor.icv.tsp.ota.controller.approval.FotaApprovalProcessService;
import com.bnmotor.icv.tsp.ota.model.query.FotaStrategyQuery;
import com.bnmotor.icv.tsp.ota.model.req.approval.DeleteOperateRequest;
import com.bnmotor.icv.tsp.ota.model.req.web.FotaStrategyAuditDto;
import com.bnmotor.icv.tsp.ota.model.req.web.FotaStrategyDto;
import com.bnmotor.icv.tsp.ota.model.req.web.FotaStrategyEnableDto;
import com.bnmotor.icv.tsp.ota.model.req.web.FotaStrategyReportDto;
import com.bnmotor.icv.tsp.ota.model.resp.web.FotaStrategyPreConditionVo;
import com.bnmotor.icv.tsp.ota.model.resp.web.FotaStrategySelectableFirmwareVo;
import com.bnmotor.icv.tsp.ota.model.validate.Save;
import com.bnmotor.icv.tsp.ota.model.validate.Update;
import com.bnmotor.icv.tsp.ota.service.IFotaStrategyService;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: FotaStrategyController
 * @Description: OTA升级策略-新表 controller层
 * @author xuxiaochang1
 * @since 2020-12-01
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Api(value="升级策略管理",tags={"升级策略管理"})
@RestController
@Slf4j
public class FotaStrategyController {
    
	@Autowired
    IFotaStrategyService fotaStrategyService;
    
	@Autowired
	FotaApprovalProcessService fotaApprovalProcessService;

    @ApiOperation(value = "新增策略", response = Long.class)
    @PostMapping("/v1/strategy")
    @WrapBasePo
    public ResponseEntity saveFotaStrategy(@RequestBody @Validated(Save.class) FotaStrategyDto fotaStrategyDto) {
        //区分新增和编辑策略
        fotaStrategyDto.setId(null);
        return RestResponse.ok(fotaStrategyService.saveOrUpdateFotaStrategy(fotaStrategyDto));
    }

    @ApiOperation(value = "修改策略", response = Long.class)
    @PutMapping("/v1/strategy")
    @WrapBasePo
    public ResponseEntity updateFotaStrategy(@RequestBody @Validated(Update.class) FotaStrategyDto fotaStrategyDto) {
        return RestResponse.ok(fotaStrategyService.saveOrUpdateFotaStrategy(fotaStrategyDto));
    }

    @ApiOperation(value = "删除策略", response = Void.class)
    @DeleteMapping("/v1/strategy/{id}")
    public ResponseEntity delFotaStrategy(@PathVariable String id) {
    	Long strategyId = Long.parseLong(id);
    	fotaApprovalProcessService.checkDeleteOperate(DeleteOperateRequest.of().setPrimaryKey(strategyId).setApprovalType(1));
        fotaStrategyService.delFotaStrategy(strategyId);
        return RestResponse.ok(null);
    }

    @ApiOperation(value = "策略详情", response = FotaStrategySelectableFirmwareVo.class)
    @GetMapping("/v1/strategy/{id}")
    public ResponseEntity findFotaStrategy(@PathVariable String id) {
        FotaStrategyDto fotaStrategyDto = fotaStrategyService.findOneFotaStrategy(Long.parseLong(id));
        return RestResponse.ok(fotaStrategyDto);
    }

    /*@ApiOperation(value = "实时显示升级策略时长")
    @GetMapping("/v1/strategy/calcEstimateUpgradeTime")
    public ResponseEntity calcEstimateUpgradeTime(*//*@RequestParam *//*List<FotaStrategyFirmwareListDto> fotaStrategyFirmwareListDtos) {
        int estimateUpgradeTime = fotaStrategyService.calcEstimateUpgradeTime(fotaStrategyFirmwareListDtos);
        return RestResponse.ok(estimateUpgradeTime);
    }*/

    @ApiOperation(value = "查询策略列表", response = FotaStrategyDto.class)
    @GetMapping("/v1/strategy")
    public ResponseEntity list(FotaStrategyQuery fotaStrategyQuery) {
        return RestResponse.ok(fotaStrategyService.list(fotaStrategyQuery));
    }

    @ApiOperation(value = "查询创建策略可选的固件列表", response = FotaStrategySelectableFirmwareVo.class)
    @GetMapping("/v1/strategySelectableFirmwares")
    public ResponseEntity listSelectableFirmwares(@RequestParam Long treeNodeId) {
        return MyRestResponse.ok(fotaStrategyService.listSelectableFirmwares(treeNodeId));
    }

    @ApiOperation(value = "策略审核", response = Boolean.class)
    @PutMapping("/v1/strategyAudit")
    @WrapBasePo
    public ResponseEntity strategyAudit(@RequestBody FotaStrategyAuditDto fotaStrategyAuditDto) {
        fotaStrategyService.strategyAudit(fotaStrategyAuditDto);
        return RestResponse.ok(Boolean.TRUE);
    }

    @ApiOperation(value = "策略打开/关闭", response = Boolean.class)
    @PutMapping("/v1/strategyEnable")
    @WrapBasePo
    public ResponseEntity strategyEnable(@RequestBody FotaStrategyEnableDto fotaStrategyEnableDto) {
        fotaStrategyService.strategyEnable(fotaStrategyEnableDto);
        return RestResponse.ok(Boolean.TRUE);
    }

    @ApiOperation(value = "查询默认前置条件", response = FotaStrategyPreConditionVo.class)
    @GetMapping("/v1/strategy/preCondition")
    public ResponseEntity listPreConditions(Long treeNodeId) {
        List<WebEnums.PreConditionTypeEnum> preConditionTypeEnumList = WebEnums.PreConditionTypeEnum.list();
        if(MyCollectionUtil.isNotEmpty(preConditionTypeEnumList)){
            return RestResponse.ok(MyCollectionUtil.newCollection(preConditionTypeEnumList, item -> {
                FotaStrategyPreConditionVo fotaStrategyPreConditionVo = new FotaStrategyPreConditionVo();
                fotaStrategyPreConditionVo.setConName(item.getDesc());
                fotaStrategyPreConditionVo.setConCode(item.getValue());
                fotaStrategyPreConditionVo.setPreConditionValues(MyCollectionUtil.newCollection(item.getPreConditionValues(), item1 -> FotaStrategyPreConditionVo.PreConditionValue.builder().isDefault(item1.getIsDefault()).value(item1.getValue()).valueDesc(item1.getValueDesc()).build()));
                return fotaStrategyPreConditionVo;
            }));
        }
        return RestResponse.ok(Collections.emptyList());
    }

    @ApiOperation(value = "策略审批时关联测试报告", response = Boolean.class)
    @WrapBasePo
    @PostMapping("/v1/strategy/report")
    public ResponseEntity accociateStrategyReport(@RequestBody FotaStrategyReportDto fotaStrategyReportDto) {
    	log.info("策略上传审批报告请求参数|{}", fotaStrategyReportDto);
    	return RestResponse.ok(fotaStrategyService.accociateStrategyReport(fotaStrategyReportDto));
    }
    
}
