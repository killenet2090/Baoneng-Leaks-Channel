package com.bnmotor.icv.tsp.cpsp.vehviolation.service.impl;

import com.bnmotor.icv.tsp.cpsp.engine.support.CPSPProxy;
import com.bnmotor.icv.tsp.cpsp.vehviolation.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.vehviolation.common.redis.RedisKey;
import com.bnmotor.icv.tsp.cpsp.vehviolation.model.request.VehViolationHandleDto;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.input.VehviolationHandleInput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.input.VehviolationQueryInput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationHandleOutput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationQueryOutput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.service.IVehviolationHandleService;
import com.bnmotor.icv.tsp.cpsp.vehviolation.service.IVehviolationQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @ClassName: VehviolationHandleServiceImpl
* @Description: 违章办理实现类
* @author: liuhuaqiao1
* @date: 2021/1/8
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Service
public class VehviolationHandleServiceImpl implements IVehviolationHandleService {

    @Autowired
    private CPSPProxy cpspProxy;

    @Override
    public VehviolationHandleOutput handleVehviolation(VehViolationHandleDto vehViolationHandleDto) {
        VehviolationHandleInput input = VehviolationHandleInput.builder().build();
        input.setVin(vehViolationHandleDto.getVin());
        input.setViolationIds(vehViolationHandleDto.getViolationIds());
        input.setServiceCode(Constant.SERVICE_CODE);
        input.setCurrentAPI(Constant.VEHVIOLATION_HANDLE_API);
        input.setCacheKey(String.format(RedisKey.VEHVIOLATION_HANDLE_VIN_KEY,vehViolationHandleDto.getVin()));
        input.setCacheClass(VehviolationHandleOutput.class);
        VehviolationHandleOutput output = cpspProxy.call(input);
        return output;
    }
}
