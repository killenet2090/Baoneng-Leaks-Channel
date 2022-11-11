package com.bnmotor.icv.tsp.ota.controller.test;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.common.event.OtaEvent;
import com.bnmotor.icv.tsp.ota.common.event.OtaEventHandler;
import com.bnmotor.icv.tsp.ota.common.event.OtaEventPublisher;
import com.bnmotor.icv.tsp.ota.common.event.OtaEventType;
import com.bnmotor.icv.tsp.ota.controller.inner.AbstractController;
import com.bnmotor.icv.tsp.ota.event.PackageBuildCompleted;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: OtaEventController.java OtaEventController
 * @Description: 手工处理发起消息处理
 * 				示例: {"source":{"firmwarePackageId":311},"timestamp":1605682929143,"transId":"$$0","times":0,"eventType":"UPGRADE_PACKAGE_BUILD_COMPLETE"}
 * @author E.YanLonG 
 * @since 2020-11-16 15:53:58
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Api(value = "OTA消息处理", tags = { "手工触发消息处理" })
@RestController
@Slf4j
public class OtaEventController extends AbstractController {

	@Autowired
	OtaEventHandler otaEventHandler;

	@Autowired
	OtaEventPublisher otaEventPublisher;
	
	@ApiOperation(value = "手工触发消息事件", response = OtaEvent.class)
	@PostMapping("/v1/otaevent/post")
	public ResponseEntity otaEventPost(@RequestBody Long firmwarePkgId) {
		PackageBuildCompleted packageBuildCompleted = PackageBuildCompleted.of();
		packageBuildCompleted.setFirmwarePackageId(firmwarePkgId);
		OtaEvent otaEvent = OtaEventType.UPGRADE_PACKAGE_BUILD_COMPLETE.create(packageBuildCompleted);
		otaEventPublisher.pushOtaEvent(otaEvent);
		return RestResponse.ok(otaEvent);
	}

}