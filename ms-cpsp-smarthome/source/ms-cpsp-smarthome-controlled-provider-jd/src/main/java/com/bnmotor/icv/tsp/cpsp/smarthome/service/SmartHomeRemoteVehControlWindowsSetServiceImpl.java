package com.bnmotor.icv.tsp.cpsp.smarthome.service;

import com.bnmotor.icv.tsp.cpsp.api.SmartHomeRemoteVehControlIService;
import com.bnmotor.icv.tsp.cpsp.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.annotation.ThirdPartySvcAnno;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.strategy.AbstractStrategy;
import com.bnmotor.icv.tsp.cpsp.pojo.input.RemoteVehControlInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.RemoteVehControlOutput;
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
@ThirdPartySvcAnno(provider = Constant.PROVIDER_CODE, service = Constant.REMOTE_VEH_CONTROL_WINDOWS_SET_API)
public class SmartHomeRemoteVehControlWindowsSetServiceImpl implements SmartHomeRemoteVehControlIService, AbstractStrategy<RemoteVehControlInput, RemoteVehControlOutput> {


    @Override
    public RemoteVehControlOutput call(RemoteVehControlInput input) {
        RemoteVehControlOutput output = this.vehWindowSetProcess(input);
        return output;
    }

    @Override
    public RemoteVehControlOutput vehWindowSetProcess(RemoteVehControlInput input) {
        RemoteVehControlOutput output = new RemoteVehControlOutput();
        output.setBusinessId("T88700000000001");
        return output;
    }
}
