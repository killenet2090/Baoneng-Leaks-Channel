
package com.bnmotor.icv.tsp.ota.controller.v2;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.bnmotor.icv.adam.web.rest.RestResponse;
//import com.bnmotor.icv.tsp.ota.aop.aspect.WrapBasePo;
//import com.bnmotor.icv.tsp.ota.model.req.v2.QueryUpgradeObjectV2Req;
//import com.bnmotor.icv.tsp.ota.model.resp.v2.FotaAssociateVehicleVo;
//import com.bnmotor.icv.tsp.ota.service.v2.IFotaPlanObjListV2Service;
//import com.bnmotor.icv.tsp.ota.service.v2.OtaPlanVehicleAssociationService;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//
///**
// * 
// * @ClassName: UpgradeObjectListV2Controller.java UpgradeObjectListV2Controller
// * @Description: V2版本升级任务关联车辆
// * @author E.YanLonG
// * @since 2020-12-14 18:51:05
// * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
// */
//@Api(value = "待关联车辆查询", tags = { "OTA升级计划V2" })
//@RestController
public class UpgradeObjectListV2Controller {

//	@Autowired
//	IFotaPlanObjListV2Service iFotaPlanObjListV2Service;
//
//	@Autowired
//	OtaPlanVehicleAssociationService otaPlanVehicleAssociationService;
	
//	@ApiOperation("新增OTA升级计划车辆V2")
//	@PostMapping("/v2/upgradeTaskObjList")
//	@WrapBasePo
//	public ResponseEntity insertUpgradeTaskObjList(@RequestBody @Valid UpgradeObjectListV2Req upgradeObjectListReq) {
//		AddFotaPlanResultVo addFotaPlanResultVo = iFotaPlanObjListV2Service.insertUpgradeTaskObjectList(upgradeObjectListReq);
//		if (MyCollectionUtil.isNotEmpty(addFotaPlanResultVo.getExistValidPlanObjVos())) {
//			return RestResponse.error(OTARespCodeEnum.FOTA_OBJECT_IN_ANOTHORE_PLAN.getCode(), addFotaPlanResultVo.toString());
//		}
//		return RestResponse.ok(addFotaPlanResultVo);
//	}

//	@ApiOperation("更新OTA升级计划车辆V2")
//	@PutMapping("/v2/upgradeTaskObjList")
//	@WrapBasePo
//	public ResponseEntity updateFotaPlanObjList(@RequestBody @Validated(value = Update.class) UpgradeObjectListV2Req upgradeObjectListReq) {
//		AddFotaPlanResultVo addFotaPlanResultVo = iFotaPlanObjListV2Service.updateFotaPlanObjList(upgradeObjectListReq);
//		if (MyCollectionUtil.isNotEmpty(addFotaPlanResultVo.getExistValidPlanObjVos())) {
//			return RestResponse.error(OTARespCodeEnum.FOTA_OBJECT_IN_ANOTHORE_PLAN.getCode(), addFotaPlanResultVo.toString());
//		}
//		return RestResponse.ok(addFotaPlanResultVo);
//	}
	
}