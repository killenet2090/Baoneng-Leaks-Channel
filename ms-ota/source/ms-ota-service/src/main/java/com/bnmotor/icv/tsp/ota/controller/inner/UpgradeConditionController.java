package com.bnmotor.icv.tsp.ota.controller.inner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.model.resp.FotaUpgradeConditionVo;
import com.bnmotor.icv.tsp.ota.service.IFotaUpgradeConditionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: UpgradeTaskController.java UpgradeTaskController
 * @Description: 前端新建任务时查询终端升级条件（结果按cond_code排列返回）
 * @author eyanlong2
 * @since 2020-9-10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Api(value = "终端升级前置条件接口", tags = { "终端升级前置条件接口" })
@RestController
@Slf4j
public class UpgradeConditionController extends AbstractController {

	@Autowired
	IFotaUpgradeConditionService fotaUpgradeConditionService;

	@ApiOperation(value = "查询升级条件项目", response = FotaUpgradeConditionVo.class)
	@GetMapping("/v1/upgrade/condition")
	public ResponseEntity queryUpgradeCondition() {
		log.info("查询所有终端升级条件项目");
		return RestResponse.ok(fotaUpgradeConditionService.queryAllCondtiion());
	}

}