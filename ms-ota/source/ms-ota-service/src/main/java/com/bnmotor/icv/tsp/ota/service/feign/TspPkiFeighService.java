package com.bnmotor.icv.tsp.ota.service.feign;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.model.req.feign.FileDto;
import com.bnmotor.icv.tsp.ota.model.req.feign.SignOtaFileDto;
import com.bnmotor.icv.tsp.ota.model.resp.feign.PkiInfo;
import com.bnmotor.icv.tsp.ota.service.feign.fallback.TspPkiFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * @ClassName: TspPkiFeighService.java TspPkiFeighService
 * @Description: PKI加密服务
 * @author E.YanLonG
 * @since 2020-11-11 9:57:47
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@FeignClient(value = "ms-pki", fallback = TspPkiFeignFallbackFactory.class)
public interface TspPkiFeighService {

	/**
	 * 加密文件
	 * @param fileDto
	 * @return
	 */
	@PostMapping("/inner/security/encryptFile")
    RestResponse<PkiInfo> securityEncryptFile(@RequestBody FileDto  fileDto);


	/**
	 * 	签名
	 * @param signDto
	 * @return
	 */
	@PostMapping("/inner/security/signOtaFile")
	RestResponse<String> signMessage(@RequestBody SignOtaFileDto  signDto);
}