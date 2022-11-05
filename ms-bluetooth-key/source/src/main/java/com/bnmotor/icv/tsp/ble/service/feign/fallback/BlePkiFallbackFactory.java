package com.bnmotor.icv.tsp.ble.service.feign.fallback;

import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ble.model.request.pki.*;
import com.bnmotor.icv.tsp.ble.model.response.user.UserCheckVo;
import com.bnmotor.icv.tsp.ble.model.response.user.UserPhoneVo;
import com.bnmotor.icv.tsp.ble.model.response.user.UserVo;
import com.bnmotor.icv.tsp.ble.service.feign.BlePkiFeignService;
import com.bnmotor.icv.tsp.ble.service.feign.UserFeignService;
import feign.hystrix.FallbackFactory;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @ClassName: UserFeignFallbackFactory
 * @Description: 用户feign调用异常接口
 * @author: shuqi1
 * @date: 2020/6/8
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class BlePkiFallbackFactory implements FallbackFactory<BlePkiFeignService> {
    private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(BlePkiFallbackFactory.class);
    @Override
    public BlePkiFeignService create(Throwable throwable) {
        return new BlePkiFeignService(){
            @Override
            public RestResponse<AsymmetricVo> asymmetricEncrypt(BleEncryptDto bleEncryptDto) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }

            @Override
            public RestResponse<Map> encrpty(BleEncrptyDto bleEncrptyDto) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }

            @Override
            public RestResponse<Map> decrpty(BleDecrptyDto bleDecrptyDto) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }

            @Override
            public RestResponse<SignVo> sign(BleSignDto bleSignDto) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }

            @Override
            public RestResponse<VerifySignVo> verifySign(BleVerSignDto bleVerSignDto) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }

            @Override
            public RestResponse<Map> importKey(BleImportDto bleImportDto) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }

        };

    }
}
