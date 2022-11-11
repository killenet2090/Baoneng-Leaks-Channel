package com.bnmotor.icv.tsp.device.service.feign.fallback;

import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.model.response.feign.CmpSimVo;
import com.bnmotor.icv.tsp.device.model.response.feign.GetICCIDStateVo;
import com.bnmotor.icv.tsp.device.service.feign.CmpFeignService;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: DeviceOnlineFeignService
 * @Description: sim卡管理降级工厂类
 * @author: huangyun1
 * @date 2020/05/18 *
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class CmpFeignFallbackFactory implements FallbackFactory<CmpFeignService> {
    private static final Logger Logger = LoggerFactory.getLogger(CmpFeignFallbackFactory.class);


    @Override
    public CmpFeignService create(Throwable throwable) {
        if (Logger.isInfoEnabled()) {
            Logger.info("fallback; reason was: {}", throwable.getMessage());
        }

        return new CmpFeignService() {
            @Override
            public RestResponse<GetICCIDStateVo> getICCIDState(String vin) {
                return new RestResponse(null, RespCode.SERVER_EXECUTE_ERROR.getDescription(), RespCode.SERVER_EXECUTE_ERROR.getValue());
            }

            @Override
            public RestResponse<CmpSimVo> getSimByIccid(String iccid) {
                return new RestResponse(null, RespCode.SERVER_EXECUTE_ERROR.getDescription(), RespCode.SERVER_EXECUTE_ERROR.getValue());
            }
        };
    }
}
