package com.bnmotor.icv.tsp.cpsp.vehviolation.service.impl;

import com.bnmotor.icv.tsp.cpsp.engine.support.CPSPProxy;
import com.bnmotor.icv.tsp.cpsp.vehviolation.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.vehviolation.common.redis.RedisKey;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.input.VehviolationQueryInput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationQueryOutput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.service.IVehviolationQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @ClassName: IVehviolationQueryServiceImpl
* @Description: 违章查询实现类
* @author: liuhuaqiao1
* @date: 2021/1/7
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Service
public class VehviolationQueryServiceImpl implements IVehviolationQueryService {

    @Autowired
    private CPSPProxy cpspProxy;

    @Override
    public VehviolationQueryOutput queryVehviolation(String vin) {
        VehviolationQueryInput input = VehviolationQueryInput.builder().build();
        input.setVin(vin);
        input.setServiceCode(Constant.SERVICE_CODE);
        input.setCurrentAPI(Constant.VEHVIOLATION_QUERY_API);
        input.setCacheKey(String.format(RedisKey.VEHVIOLATION_QUERY_VIN_KEY,vin));
        input.setCacheClass(VehviolationQueryOutput.class);
        VehviolationQueryOutput output = cpspProxy.call(input);
        return output;
    }
}
