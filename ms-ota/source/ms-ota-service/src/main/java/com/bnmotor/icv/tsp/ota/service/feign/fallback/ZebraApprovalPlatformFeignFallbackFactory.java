package com.bnmotor.icv.tsp.ota.service.feign.fallback;

import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.data.mysql.metadata.Page;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.model.resp.feign.ProcessDefinitionQueryVo;
import com.bnmotor.icv.tsp.ota.model.resp.feign.ProcessDefinitionVo;
import com.bnmotor.icv.tsp.ota.model.resp.feign.StartProcessInstanceVo;
import com.bnmotor.icv.tsp.ota.model.resp.feign.SubmitProcessVo;
import com.bnmotor.icv.tsp.ota.service.feign.ZebraApprovalPlatformFeignService;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: PushFeignFallbackFactory
 * @Description: 用户服务降级工厂类
 * @author: huangyun1
 * @date 2020/05/18 *
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
public class ZebraApprovalPlatformFeignFallbackFactory implements FallbackFactory<ZebraApprovalPlatformFeignService> {
	@Override
	public ZebraApprovalPlatformFeignService create(Throwable throwable) {

		if (log.isInfoEnabled()) {
			log.info("fallback; reason was: {}", throwable.getMessage());
		}

		return new ZebraApprovalPlatformFeignService() {
			@Override
			public RestResponse<Page<ProcessDefinitionVo>> queryProcessDefinition(ProcessDefinitionQueryVo processDefinitionQueryVo) {
				return new RestResponse(null, RespCode.SERVER_EXECUTE_ERROR.getDescription(),
						RespCode.SERVER_EXECUTE_ERROR.getValue());
			}

			@Override
			public RestResponse<String> startProcessByDefinition(StartProcessInstanceVo startProcessInstance) {
				return new RestResponse(null, RespCode.SERVER_EXECUTE_ERROR.getDescription(),
						RespCode.SERVER_EXECUTE_ERROR.getValue());
			}

			@Override
			public RestResponse<Boolean> submitProcessForm(SubmitProcessVo submitProcessVo) {
				return new RestResponse(null, RespCode.SERVER_EXECUTE_ERROR.getDescription(),
						RespCode.SERVER_EXECUTE_ERROR.getValue());
			}
		};
	}
}
