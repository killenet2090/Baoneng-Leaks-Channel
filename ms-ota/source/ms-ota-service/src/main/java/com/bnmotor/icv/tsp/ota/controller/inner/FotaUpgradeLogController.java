package com.bnmotor.icv.tsp.ota.controller.inner;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.model.page.PageResult;
import com.bnmotor.icv.tsp.ota.model.query.FotaUpgradeLogQuery;
import com.bnmotor.icv.tsp.ota.model.req.DownloadUpgradeLogReq;
import com.bnmotor.icv.tsp.ota.model.resp.FotaUpgradeLogVo;
import com.bnmotor.icv.tsp.ota.service.IFotaUpgradeLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;


/**
 * @ClassName: FotaUpgradeLogController.java FotaUpgradeLogController
 * @Description: 后台管理页面 展示TBOX车辆升级日志持久化相关信息
 * @author E.YanLonG
 * @since 2020-10-22 9:03:09
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Api(value="车辆TBOX升级日志管理",tags={"车辆TBOX升级日志管理"})
@RestController
@Slf4j
public class FotaUpgradeLogController {

	@Autowired
	IFotaUpgradeLogService fotaUpgradeLogService;
	
	@ApiOperation(value = "车辆升级日志消息分页查询升级对象", response = FotaUpgradeLogVo.class)
    @GetMapping("/v1/tbox/upgradelog")
	public ResponseEntity queryVehicleUpgradeLog(FotaUpgradeLogQuery fotaUpgradeLogQuery) {
		log.info("查询车辆TBOX升级日志持久化信息请求参数|{}", fotaUpgradeLogQuery);
		
		PageResult<FotaUpgradeLogVo> fotaUpgradeLogVoPage = fotaUpgradeLogService.queryFotaUpgradeLogPageResult(fotaUpgradeLogQuery);
		return RestResponse.ok(fotaUpgradeLogVoPage);
	}

	/*
	@ApiOperation("车辆升级日志消息分页查询升级对象")
    @GetMapping("/v1/tbox/upgradelog")
	public ResponseEntity queryVehicleUpgradeLog(FotaUpgradeLogQuery fotaUpgradeLogQuery) {
		log.info("查询车辆TBOX升级日志持久化信息请求参数|{}", fotaUpgradeLogQuery);

		Page<FotaUpgradeLogVo> fotaUpgradeLogVoPage = fotaUpgradeLogService.queryFotaUpgradeLogPage(fotaUpgradeLogQuery);
		return RestResponse.ok(fotaUpgradeLogVoPage);
	}
	*/
	
	@ApiOperation(value = "车辆升级日志ZIP下载", response = Void.class)
    @PostMapping("/v1/tbox/upgradelog/download")
	public void downloadUpgradeLog(@RequestBody DownloadUpgradeLogReq downloadUpgradeLogReq) {

		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		log.info("下载TBOX升级文件请求参数|{}", downloadUpgradeLogReq);
		fotaUpgradeLogService.downloadUpgradeLog(downloadUpgradeLogReq.getTransId(), response);
	}

}