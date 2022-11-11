package com.bnmotor.icv.tsp.cpsp.vehviolation.service.impl;

import com.bnmotor.icv.tsp.cpsp.engine.support.CPSPProxy;
import com.bnmotor.icv.tsp.cpsp.vehviolation.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.vehviolation.common.redis.RedisKey;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.input.VehviolationAlertQueryInput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationAlertQueryOutput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.service.IVehviolationAlertQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @ClassName: VehviolationAlertQueryServiceImpl
* @Description: 违章提醒查询实现类
* @author: liuhuaqiao1
* @date: 2021/1/8
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Service
public class VehviolationAlertQueryServiceImpl implements IVehviolationAlertQueryService {

    @Autowired
    private CPSPProxy cpspProxy;

    @Override
    public VehviolationAlertQueryOutput queryAlertVehviolation(String vin) {
        VehviolationAlertQueryInput input = VehviolationAlertQueryInput.builder().build();
        input.setVin(vin);
        input.setServiceCode(Constant.SERVICE_CODE);
        input.setCurrentAPI(Constant.VEHVIOLATION_ALERT_QUERY_API);
        input.setCacheKey(String.format(RedisKey.VEHVIOLATION_ALERT_QUERY_VIN_KEY,vin));
        input.setCacheClass(VehviolationAlertQueryOutput.class);
        VehviolationAlertQueryOutput output = cpspProxy.call(input);
        return output;
    }
}
