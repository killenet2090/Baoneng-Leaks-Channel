package com.bnmotor.icv.tsp.ota.controller.v2;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.aop.aspect.WrapBasePo;
import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import com.bnmotor.icv.tsp.ota.common.enums.OperateTypeEnum;
import com.bnmotor.icv.tsp.ota.common.event.MyOtaEventKit;
import com.bnmotor.icv.tsp.ota.controller.approval.FotaApprovalProcessService;
import com.bnmotor.icv.tsp.ota.controller.inner.AbstractController;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanPo;
import com.bnmotor.icv.tsp.ota.model.req.approval.DeleteOperateRequest;
import com.bnmotor.icv.tsp.ota.model.req.v2.FotaPlanApproveV2Req;
import com.bnmotor.icv.tsp.ota.model.req.v2.FotaPlanIsEnableV2Req;
import com.bnmotor.icv.tsp.ota.model.req.v2.FotaPlanPublishV2Req;
import com.bnmotor.icv.tsp.ota.model.req.v2.FotaPlanV2Query;
import com.bnmotor.icv.tsp.ota.model.req.v2.FotaPlanV2Req;
import com.bnmotor.icv.tsp.ota.model.req.v2.QueryAssociatedObjectV2Req;
import com.bnmotor.icv.tsp.ota.model.req.v2.QueryUpgradeObjectV2Req;
import com.bnmotor.icv.tsp.ota.model.resp.AddFotaPlanResultVo;
import com.bnmotor.icv.tsp.ota.model.resp.v2.FotaAssociateVehicleV2RespVo;
import com.bnmotor.icv.tsp.ota.model.resp.v2.FotaPlanConditionVo;
import com.bnmotor.icv.tsp.ota.model.resp.v2.FotaPlanDetailV2Vo;
import com.bnmotor.icv.tsp.ota.model.resp.v2.FotaPlanIsEnableV2RespVo;
import com.bnmotor.icv.tsp.ota.model.resp.v2.FotaPlanV2Vo;
import com.bnmotor.icv.tsp.ota.model.resp.v2.QueryAssociatedObjectV2RespVo;
import com.bnmotor.icv.tsp.ota.model.validate.Update;
import com.bnmotor.icv.tsp.ota.service.IFotaStrategyService;
import com.bnmotor.icv.tsp.ota.service.v2.IFotaPlanV2Service;
import com.bnmotor.icv.tsp.ota.service.v2.OtaPlanVehicleAssociationService;
import com.bnmotor.icv.tsp.ota.service.v2.VehicleUploadService;
import com.bnmotor.icv.tsp.ota.util.ExceptionUtil;
import com.bnmotor.icv.tsp.ota.util.MyAssertUtil;
import com.google.common.collect.Lists;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: FotaPlanDo
 * @Description: V2??????????????????
 * @author E.YanLonG
 * @since 2020-11-30
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             ?????????????????????????????????????????????????????????????????????????????????????????????????????????
 */
@Api(value = "??????????????????V2", tags = { "??????????????????V2" })
@RestController
@Slf4j
public class FotaPlanV2Controller extends AbstractController {

	@Autowired
	IFotaPlanV2Service fotaPlanService;
	
	@Autowired
	IFotaStrategyService iFotaStrategyService;
	
	@Autowired
	VehicleUploadService vehicleUploadService;
	
	@Autowired
    OtaPlanVehicleAssociationService otaPlanVehicleAssociationService;
	
	@Autowired
	FotaApprovalProcessService fotaApprovalProcessService;
	
	@Autowired
    MyOtaEventKit myOtaEventKit;

	@ApiOperation(value = "????????????????????????V2", response = FotaPlanV2Vo.class)
	@GetMapping("/v2/task")
	public ResponseEntity queryPage(FotaPlanV2Query fotaPlanQuery) {
		IPage<FotaPlanV2Vo> iPage = fotaPlanService.queryPage(fotaPlanQuery);
		
		otaPlanVehicleAssociationService.strategyPlanVehicleBatchSize(iPage.getRecords());
		List<FotaPlanV2Vo> list = iPage.getRecords();
		return RestResponse.ok(iPage);
	}

	@ApiOperation(value = "??????????????????V2", response = FotaPlanDetailV2Vo.class)
	@GetMapping("/v2/task/{planId}")
	public ResponseEntity queryById(@PathVariable Long planId) {
		return RestResponse.ok(fotaPlanService.getFotaPlanDetailVoById(planId));
	}

	@ApiOperation(value = "??????????????????V2", response = FotaPlanV2Req.class)
	@PostMapping("/v2/task")
	@WrapBasePo
	@Transactional(rollbackFor = AdamException.class)
	public ResponseEntity insertFotaPlan(@RequestPart(value = "file", required = false) MultipartFile multipartFile, @Valid FotaPlanV2Req fotaPlanV2Req) {
		
		fotaPlanV2Req.setRestfulType(1); // ????????????
		// ???????????? 1??????????????? 2???????????????
		Integer operateType = fotaPlanV2Req.getOperateType();
		
		// ????????????????????????fileName??????
		if (OperateTypeEnum.isFile(operateType) && Objects.nonNull(multipartFile)) {
			if (StringUtils.isBlank(fotaPlanV2Req.getFileName())) {
				fotaPlanV2Req.setFileName(multipartFile.getOriginalFilename());
			}
		}
		
		FotaPlanPo fotaPlanPo = fotaPlanService.insertFotaPlan(fotaPlanV2Req);
//		FotaPlanV2RespVo fotaPlanV2RespVo = new FotaPlanV2RespVo();
		
		if (OperateTypeEnum.isCond(operateType)) {
			log.info("????????????????????????");
			AddFotaPlanResultVo addFotaPlanResultVo = otaPlanVehicleAssociationService.associate(fotaPlanV2Req, fotaPlanPo, fotaPlanV2Req.getFotaStrategyId());
			if (addFotaPlanResultVo.getResult() == 0) { // ???????????????
				throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FOTA_PLAN_INVALID_OBJECT);
			}
			return RestResponse.ok(addFotaPlanResultVo);
		}
		
		// ???????????????
		if (OperateTypeEnum.isFile(operateType)) {
			// ????????????????????????????????????
			fotaPlanV2Req.setLabels(Lists.newArrayList());
			fotaPlanV2Req.setRegions(Lists.newArrayList());
			
			log.info("????????????????????????");
			Long treeNodeId = fotaPlanV2Req.getTreeNodeId();
			List<FotaObjectPo> fotaObjectPoList = vehicleUploadService.process(multipartFile, Lists.newArrayList());
			String fileName = resolveOriginFileName(multipartFile);
			fotaPlanV2Req.setFileName(fileName);
			List<String> vins = fotaObjectPoList.stream().map(FotaObjectPo::getObjectId).collect(Collectors.toList());
			fotaPlanV2Req.setVins(vins);
			AddFotaPlanResultVo addFotaPlanResultVo = otaPlanVehicleAssociationService.associate(fotaPlanV2Req, fotaPlanPo, fotaPlanV2Req.getFotaStrategyId());
			if (addFotaPlanResultVo.getResult() == 0) { // ???????????????
				throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FOTA_PLAN_INVALID_OBJECT);
			}
			return RestResponse.ok(addFotaPlanResultVo);
		}
		
		Long fotaPlanId = fotaPlanPo.getId();
		log.info("????????????id|{}", fotaPlanId);
		fotaPlanV2Req.setId(fotaPlanId);
		
		return RestResponse.ok(fotaPlanV2Req);
	}
	
	public String resolveOriginFileName(MultipartFile multipartFile) {
		if (Objects.isNull(multipartFile)) {
			return null;
		}
		return multipartFile.getOriginalFilename();
	}

	/**
	 * ?????????????????????????????????????????????????????????????????????
	 * @param fotaPlanV2Req
	 * @param multipartFile
	 * @return
	 */
	@ApiOperation(value = "??????????????????V2", response = AddFotaPlanResultVo.class)
	@PutMapping("/v2/task")
	@WrapBasePo
	public ResponseEntity updateFotaPlan(@RequestPart(value = "file", required = false) MultipartFile multipartFile, @Validated(value = Update.class) FotaPlanV2Req fotaPlanV2Req) {
		fotaPlanV2Req.setRestfulType(2); // ????????????
		Long otaPlanId = fotaPlanV2Req.getId();
		FotaPlanPo fotaPlanPo = fotaPlanService.selectFotaPlan(otaPlanId);
		MyAssertUtil.notNull(fotaPlanPo, OTARespCodeEnum.DATA_NOT_FOUND);
		
		fotaPlanService.updateFotaPlan(fotaPlanV2Req);
		fotaPlanPo = fotaPlanService.selectFotaPlan(otaPlanId);
		
		Integer operateType = fotaPlanV2Req.getOperateType();
		// ????????????????????????fileName??????
		if (OperateTypeEnum.isFile(operateType) && Objects.nonNull(multipartFile)) {
			if (StringUtils.isBlank(fotaPlanV2Req.getFileName())) {
				fotaPlanV2Req.setFileName(multipartFile.getOriginalFilename());
			}
		}
		
		if (OperateTypeEnum.isCond(operateType)) { // ???????????????
			log.info("???????????????...");
			AddFotaPlanResultVo addFotaPlanResultVo = otaPlanVehicleAssociationService.associate(fotaPlanV2Req, fotaPlanPo, fotaPlanV2Req.getFotaStrategyId());
			if (addFotaPlanResultVo.getResult() == 0) { // ???????????????
				throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FOTA_PLAN_INVALID_OBJECT);
			}
			myOtaEventKit.triggerFotaUpgradePlanSwitchEvent(otaPlanId, null);
			return RestResponse.ok(addFotaPlanResultVo);
		}
		
		// ???????????????
		if (OperateTypeEnum.isFile(operateType)) {
			log.info("????????????????????????");
			Long treeNodeId = fotaPlanV2Req.getTreeNodeId();
			List<FotaObjectPo> fotaObjectPoList = vehicleUploadService.process(multipartFile, Lists.newArrayList());
			List<String> vins = fotaObjectPoList.stream().map(FotaObjectPo::getObjectId).collect(Collectors.toList());
			fotaPlanV2Req.setVins(vins);
			AddFotaPlanResultVo addFotaPlanResultVo = otaPlanVehicleAssociationService.associate(fotaPlanV2Req, fotaPlanPo, fotaPlanV2Req.getFotaStrategyId());
			if (addFotaPlanResultVo.getResult() == 0) { // ???????????????
				throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FOTA_PLAN_INVALID_OBJECT);
			}
			myOtaEventKit.triggerFotaUpgradePlanSwitchEvent(otaPlanId, null);
			return RestResponse.ok(addFotaPlanResultVo);
		}
		return RestResponse.ok(null);
	}

	@ApiOperation(value = "????????????????????????", response = Integer.class)
	@DeleteMapping("/v2/task/{planId}")
	public ResponseEntity deleteById(@PathVariable Long planId) {
		fotaApprovalProcessService.checkDeleteOperate(DeleteOperateRequest.of().setPrimaryKey(planId).setApprovalType(2));
		return RestResponse.ok(fotaPlanService.deleteById(planId));
	}

	@ApiOperation(value = "??????????????????", response = FotaPlanConditionVo.class)
	@GetMapping("/v2/task/cond/{treeNodeId}")
	public ResponseEntity queryFilterCondition(@PathVariable Long treeNodeId) {
		FotaPlanConditionVo fotaPlanConditionVo = otaPlanVehicleAssociationService.listConditionByTreeNodeId(treeNodeId);
		return RestResponse.ok(fotaPlanConditionVo);
	}

	@ApiOperation(value = "????????????-??????????????????", response = FotaPlanIsEnableV2RespVo.class)
	@PutMapping("/v2/task/edit/isEnable")
	@WrapBasePo
	public ResponseEntity isEnableFotaPlan(@RequestBody @Validated(value = Update.class) FotaPlanIsEnableV2Req req) {
		return RestResponse.ok(fotaPlanService.isEnableFotaPlan(req));
	}

	@ApiOperation(value = "????????????-????????????", response = Boolean.class)
	@PutMapping("/v2/task/edit/approve")
	@WrapBasePo
	public ResponseEntity approveFotaPlan(@RequestBody @Validated(value = Update.class) FotaPlanApproveV2Req req) {
		return RestResponse.ok(fotaPlanService.approveFotaPlan(req));
	}

	@ApiOperation(value = "????????????-????????????", response = Boolean.class)
	@PutMapping("/v2/task/edit/publish")
	@WrapBasePo
	public ResponseEntity publishFotaPlan(@RequestBody @Validated(value = Update.class) FotaPlanPublishV2Req req) {
		return RestResponse.ok(fotaPlanService.publishFotaPlan(req));
	}
	
	@ApiOperation(value = "??????OTA???????????????????????????V2", response = FotaAssociateVehicleV2RespVo.class)
	@PostMapping("/v2/task/queryUpgradeObjList")
	@WrapBasePo
	public ResponseEntity queryFotaPlanObjList(@RequestBody @Validated QueryUpgradeObjectV2Req queryUpgradeObjectV2Req) {
		FotaAssociateVehicleV2RespVo fotaAssociateVehicleV2RespVo = otaPlanVehicleAssociationService.pageCondQuery(queryUpgradeObjectV2Req);
		return RestResponse.ok(fotaAssociateVehicleV2RespVo);
	}
	
	@ApiOperation(value = "??????OTA?????????????????????????????????", response = QueryAssociatedObjectV2RespVo.class)
	@GetMapping("/v2/task/queryAssociatedObjList")
	@WrapBasePo
	public ResponseEntity queryFotaPlanObjList(@Validated QueryAssociatedObjectV2Req queryAssociatedObjectV2Req) {
		QueryAssociatedObjectV2RespVo queryAssociatedObjectV2RespVo = otaPlanVehicleAssociationService.queryAssociateObjectList(queryAssociatedObjectV2Req);
		return RestResponse.ok(queryAssociatedObjectV2RespVo);
	}
}