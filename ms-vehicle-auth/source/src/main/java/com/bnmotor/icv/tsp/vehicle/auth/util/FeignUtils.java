package com.bnmotor.icv.tsp.vehicle.auth.util;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName: FeignUtils
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author: wuhao1
 * @date: 2020/12/22
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class FeignUtils {

    /**
     * 检查feign调用的接口是否正确返回
     * @param restResponse
     * @throws AdamException
     */
    public static boolean checkRestResponse(RestResponse<?> restResponse) throws AdamException {
        if (!StringUtils.equalsIgnoreCase(restResponse.getRespCode(), RespCode.SUCCESS.getValue())) {
            throw new AdamException(restResponse.getRespCode(), restResponse.getRespMsg());
        }
        return true;
    }
}
