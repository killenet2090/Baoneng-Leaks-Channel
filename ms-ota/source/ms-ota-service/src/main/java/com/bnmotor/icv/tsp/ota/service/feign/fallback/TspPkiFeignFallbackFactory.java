package com.bnmotor.icv.tsp.ota.service.feign.fallback;

import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.model.req.feign.FileDto;
import com.bnmotor.icv.tsp.ota.model.req.feign.SignOtaFileDto;
import com.bnmotor.icv.tsp.ota.model.resp.feign.PkiInfo;
import com.bnmotor.icv.tsp.ota.service.feign.TspPkiFeighService;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: TspPkiFeignFallbackFactory
 * @Description: PKI服务降级工厂类
 * @author:
 * @date
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
public class TspPkiFeignFallbackFactory implements FallbackFactory<TspPkiFeighService> {

	@Override
	public TspPkiFeighService create(Throwable throwable) {

		log.info("fallback; reason was: {}", throwable.getMessage());

		return new TspPkiFeighService() {
			@Override
			public RestResponse<PkiInfo> securityEncryptFile(FileDto fileDto) {
				return new RestResponse(null, RespCode.SERVER_EXECUTE_ERROR.getDescription(), RespCode.SERVER_EXECUTE_ERROR.getValue());
			}

			@Override
			public RestResponse<String> signMessage(SignOtaFileDto signDto) {
				return new RestResponse(null, RespCode.SERVER_EXECUTE_ERROR.getDescription(), RespCode.SERVER_EXECUTE_ERROR.getValue());
			}
		};
	}
}