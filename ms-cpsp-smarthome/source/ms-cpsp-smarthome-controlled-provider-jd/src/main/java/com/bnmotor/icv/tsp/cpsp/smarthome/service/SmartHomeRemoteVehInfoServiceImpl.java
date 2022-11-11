package com.bnmotor.icv.tsp.cpsp.smarthome.service;

import com.bnmotor.icv.tsp.cpsp.api.SmartHomeRemoteVehInfoIService;
import com.bnmotor.icv.tsp.cpsp.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.annotation.ThirdPartySvcAnno;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.strategy.AbstractStrategy;
import com.bnmotor.icv.tsp.cpsp.pojo.input.RemoteVehInfoInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.RemoteVehInfoOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
* @ClassName: SmartHomeRemoteVehInfoServiceImpl
* @Description: 车辆状态获取实现类
* @author: liuhuaqiao1
* @date: 2021/3/9
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Slf4j
@Service
@ThirdPartySvcAnno(provider = Constant.PROVIDER_CODE, service = Constant.REMOTE_VEH_INFO_API)
public class SmartHomeRemoteVehInfoServiceImpl implements SmartHomeRemoteVehInfoIService, AbstractStrategy<RemoteVehInfoInput, RemoteVehInfoOutput> {


    @Override
    public RemoteVehInfoOutput call(RemoteVehInfoInput input) {
        RemoteVehInfoOutput output = this.process(input);
        return output;
    }

    @Override
    public RemoteVehInfoOutput process(RemoteVehInfoInput input) {
        RemoteVehInfoOutput output = new RemoteVehInfoOutput();
        output.setAirConditionStatusOffOn("1");
        output.setChargingState("1");
        output.setDistanceToEmpty("480");
        output.setElectricityRemain("251");
        output.setWindowStatus("1");
        output.setTimestamp(System.currentTimeMillis());
        return output;
    }
}
