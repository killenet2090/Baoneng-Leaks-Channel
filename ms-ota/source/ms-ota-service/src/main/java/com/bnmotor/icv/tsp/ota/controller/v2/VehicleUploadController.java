package com.bnmotor.icv.tsp.ota.controller.v2;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.aop.aspect.WrapBasePo;
import com.bnmotor.icv.tsp.ota.common.enums.VehicleAssociateStateEnum;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanPo;
import com.bnmotor.icv.tsp.ota.model.req.v2.FotaPlanV2Req;
import com.bnmotor.icv.tsp.ota.model.req.v2.VehicleUploadFileV2Req;
import com.bnmotor.icv.tsp.ota.model.resp.AddFotaPlanResultVo;
import com.bnmotor.icv.tsp.ota.model.resp.ExistValidPlanObjVo;
import com.bnmotor.icv.tsp.ota.model.resp.FileUploadVo;
import com.bnmotor.icv.tsp.ota.model.resp.v2.FotaAssociateVehicleVo;
import com.bnmotor.icv.tsp.ota.model.resp.v2.FotaPlanVehicleStateV2RespVo;
import com.bnmotor.icv.tsp.ota.service.v2.FotaVehicleMetaObj;
import com.bnmotor.icv.tsp.ota.service.v2.IFotaPlanV2Service;
import com.bnmotor.icv.tsp.ota.service.v2.OtaPlanVehicleAssociationService;
import com.bnmotor.icv.tsp.ota.service.v2.VehicleUploadService;
import com.google.common.collect.Lists;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(value = "升级任务车辆上传", tags = { "升级任务车辆上传" })
@RestController
@Slf4j
public class VehicleUploadController {

	@Autowired
	VehicleUploadService vehicleUploadService;
	
	@Autowired
	IFotaPlanV2Service fotaPlanService;
	
	@Autowired
	OtaPlanVehicleAssociationService otaPlanVehicleAssociationService;
	
	/**
	 * 上传类型为excel，列为vin号
	 * 
	 * @param data
	 * @param fileUploadReq
	 * @return
	 */
	@WrapBasePo
	@ApiOperation(value = "升级任务车辆列表文件上传", notes = "升级任务车辆列表文件上传", response = FotaPlanVehicleStateV2RespVo.class)
	@PostMapping(value = "/v2/vehicle/upload")
	@ResponseBody
	// public ResponseEntity process(@RequestPart("file") MultipartFile multipartFile , Long treeNodeId, String fileKey) {
	public ResponseEntity process(@RequestPart(value = "file", required = false) MultipartFile multipartFile , VehicleUploadFileV2Req vehicleUploadFileV2Req) {
		// 从缓存读取已经上传的文档
		String fileKey = vehicleUploadFileV2Req.getFileKey();
		Long treeNodeId = vehicleUploadFileV2Req.getTreeNodeId();
		Long otaPlanId = vehicleUploadFileV2Req.getOtaPlanId(); // 是否属于当前任务
		FotaPlanVehicleStateV2RespVo fotaPlanVehicleStateV2RespVo = null;
		if (StringUtils.isNotBlank(fileKey)) {
			
			fotaPlanVehicleStateV2RespVo = otaPlanVehicleAssociationService.cacheVehicleUploadFile(fileKey, FotaPlanVehicleStateV2RespVo.class);
			
			
		} else { // 重新生成 文件缓存
			List<String> invalidVehicle = Lists.newArrayList();
			List<FotaObjectPo> fotaObjectPoList = vehicleUploadService.process(multipartFile, invalidVehicle);
			
			List<FotaVehicleMetaObj> upgradeTaskObjectReqList = Lists.newArrayList();
			
			upgradeTaskObjectReqList = fotaObjectPoList.stream().map((it) -> {
				FotaVehicleMetaObj fotaVehicleMetaObj = FotaVehicleMetaObj.of();
				fotaVehicleMetaObj.setVin(it.getObjectId());
				fotaVehicleMetaObj.setObjectId(it.getId());
				return fotaVehicleMetaObj;
			}).collect(Collectors.toList());
			
			
			List<ExistValidPlanObjVo> existValidPlanObjVoList = otaPlanVehicleAssociationService.validate4AddFotaPlan(upgradeTaskObjectReqList);
			// 过滤掉当前任务中包含的车辆
			if (Objects.nonNull(otaPlanId)) {
				existValidPlanObjVoList = existValidPlanObjVoList.stream().filter(it -> !it.getOtaPlanId().equals(otaPlanId)).collect(Collectors.toList());
			}
			
			Map<String, ExistValidPlanObjVo> existValidPlanObjVoMap = existValidPlanObjVoList.stream().collect(Collectors.toMap(ExistValidPlanObjVo::getVin, Function.identity(), (x, y) -> x));
			Page<FotaAssociateVehicleVo> page = vehicleUploadService.assemblyResult(fotaObjectPoList, treeNodeId, invalidVehicle);
			page.getRecords().stream().forEach(it -> {
				String vin = it.getVin();
				
				// 1可以关联 2已在其它有效任务中 3无效车辆（vin不存在）4无效车辆（非指定设备下的车辆）
				if (it.getCanAssociate().intValue() == VehicleAssociateStateEnum.OTHER_REASON.getType()) {
					if (existValidPlanObjVoMap.containsKey(vin)) { // 存在有效任务
						it.setCanAssociate(VehicleAssociateStateEnum.IN_OTHER_PLANS.getType()); //
					} else {
						it.setCanAssociate(VehicleAssociateStateEnum.CAN_ASSOCIATE.getType());
					}
					
				}
				
			});
			String fileKey0 = otaPlanVehicleAssociationService.formatFileName(null);
//			FotaPlanVehicleStateV2RespVo fotaPlanVehicleStateV2RespVo = FotaPlanVehicleStateV2RespVo.wrap(page);
			fotaPlanVehicleStateV2RespVo = FotaPlanVehicleStateV2RespVo.wrap(page);
			fotaPlanVehicleStateV2RespVo.setFileKey(fileKey0);
			fotaPlanVehicleStateV2RespVo.setTreeNodeId(treeNodeId);
			// 缓存对象
			otaPlanVehicleAssociationService.cacheVehicleUploadFile(fileKey0, fotaPlanVehicleStateV2RespVo);
		}
		
		// 手工分页
		if (Objects.nonNull(fotaPlanVehicleStateV2RespVo)) {
			fotaPlanVehicleStateV2RespVo.manualPage(vehicleUploadFileV2Req);
		}
		
		return RestResponse.ok(fotaPlanVehicleStateV2RespVo);
	}
	
	
	
	@WrapBasePo
	@ApiOperation(value = "新增升级任务并关联车辆", notes = "文件上传", response = FileUploadVo.class)
	@PostMapping("/v2/vehicle/submit")
	public ResponseEntity test(@RequestBody @Valid FotaPlanV2Req fotaPlanV2Req) {
		FotaPlanPo fotaPlanPo = fotaPlanService.insertFotaPlan(fotaPlanV2Req);
//		FotaPlanV2RespVo fotaPlanV2RespVo = new FotaPlanV2RespVo();
		
		Long fotaPlanId = fotaPlanPo.getId();
		log.info("升级任务id|{}", fotaPlanId);
		
		AddFotaPlanResultVo addFotaPlanResultVo = otaPlanVehicleAssociationService.associate(fotaPlanV2Req, fotaPlanPo, fotaPlanV2Req.getFotaStrategyId());
		return RestResponse.ok(addFotaPlanResultVo);
	}


}