package com.bnmotor.icv.tsp.ota.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bnmotor.icv.adam.data.mysql.metadata.Page;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.model.resp.feign.ProcessDefinitionQueryVo;
import com.bnmotor.icv.tsp.ota.model.resp.feign.ProcessDefinitionVo;
import com.bnmotor.icv.tsp.ota.model.resp.feign.StartProcessInstanceVo;
import com.bnmotor.icv.tsp.ota.model.resp.feign.SubmitProcessVo;
import com.bnmotor.icv.tsp.ota.service.feign.fallback.ZebraApprovalPlatformFeignFallbackFactory;

/**
 * @ClassName: ZebraApprovalPlatformFeignService.java
 * @Description: 审批平台
 * @author E.YanLonG
 * @since 2021-3-23 16:39:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@FeignClient(value = "zebra-approval-platform", fallback = ZebraApprovalPlatformFeignFallbackFactory.class)
public interface ZebraApprovalPlatformFeignService {

	@PostMapping(value = "/api/v1/definition/pageList")
	RestResponse<Page<ProcessDefinitionVo>> queryProcessDefinition(@RequestBody ProcessDefinitionQueryVo processDefinitionQuery);
	
	@PostMapping(value = "/api/v1/instance/startProcessByDefinition")
	RestResponse<String> startProcessByDefinition(@RequestBody StartProcessInstanceVo startProcessInstance);
	
	@PostMapping(value = "/api/v1/instance/submitProcessForm")
	RestResponse<Boolean> submitProcessForm(@RequestBody SubmitProcessVo submitProcessVo);

}