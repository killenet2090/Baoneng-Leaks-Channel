package com.bnmotor.icv.tsp.ota.service.feign.fallback;

import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.service.feign.MsCommonDataFeignService;
import feign.hystrix.FallbackFactory;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: MsCommonDataFeignFallbackFactory
 * @Description: 基础数据服务降级工厂类
 * @author: xuxiaochang1
 * @date 2020/05/18 *
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class MsCommonDataFeignFallbackFactory implements FallbackFactory<MsCommonDataFeignService>
{
    private static final Logger Logger = LoggerFactory.getLogger(MsCommonDataFeignFallbackFactory.class);

    @Override
    public MsCommonDataFeignService create(Throwable throwable) {

        if(Logger.isInfoEnabled()) {
            Logger.info("fallback; reason was: {}", throwable.getMessage());
        }

        return new MsCommonDataFeignService() {
            @Override
            public RestResponse list(String code){
                return new RestResponse(null, RespCode.SERVER_EXECUTE_ERROR.getDescription(), RespCode.SERVER_EXECUTE_ERROR.getValue());
            }

			@Override
			public RestResponse batchlist(String code, List<String> codes) {
				return new RestResponse(null, RespCode.SERVER_EXECUTE_ERROR.getDescription(), RespCode.SERVER_EXECUTE_ERROR.getValue());
			}
        };
    }
}
