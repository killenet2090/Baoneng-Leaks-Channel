package com.bnmotor.icv.tsp.cpsp.vehviolation.service.impl;

import com.bnmotor.icv.tsp.cpsp.engine.support.CPSPProxy;
import com.bnmotor.icv.tsp.cpsp.vehviolation.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.vehviolation.common.redis.RedisKey;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.input.VehviolationHandleResultInput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationHandleResultOutput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.service.IVehviolationHandleResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @ClassName: VehviolationHandleResultServiceImpl
* @Description: 违章办理结果查询实现类
* @author: liuhuaqiao1
* @date: 2021/1/8
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Service
public class VehviolationHandleResultServiceImpl implements IVehviolationHandleResultService {

    @Autowired
    private CPSPProxy cpspProxy;

    @Override
    public VehviolationHandleResultOutput queryHandleResultVehviolation(String orderNo) {
        VehviolationHandleResultInput input = VehviolationHandleResultInput.builder().build();
        input.setOrderNo(orderNo);
        input.setServiceCode(Constant.SERVICE_CODE);
        input.setCurrentAPI(Constant.VEHVIOLATION_HANDLE_RESULT_API);
        input.setCacheKey(String.format(RedisKey.VEHVIOLATION_HANDLE_RESULT_ORDERNO_KEY,orderNo));
        input.setCacheClass(VehviolationHandleResultOutput.class);
        VehviolationHandleResultOutput output = cpspProxy.call(input);
        return output;
    }
}
