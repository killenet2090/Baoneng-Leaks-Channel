package com.bnmotor.icv.tsp.operation.veh.util;

import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;

/**
 * @author zhoulong1
 * @ClassName: RestResponseUtil
 * @Description: RestResponse结果处理
 * @since: 2020/7/18
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class RestResponseUtil {

    private RestResponseUtil(){}

    public static boolean isSuccessResult(RestResponse restResponse) {
        if (!RespCode.SUCCESS.getValue().equals(restResponse.getRespCode())) {
            return false;
        }
        if (restResponse.getRespData() == null) {
            return false;
        }
        return true;
    }
}
