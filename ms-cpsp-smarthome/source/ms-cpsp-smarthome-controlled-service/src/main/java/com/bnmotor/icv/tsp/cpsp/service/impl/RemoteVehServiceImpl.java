package com.bnmotor.icv.tsp.cpsp.service.impl;

import com.bnmotor.icv.tsp.cpsp.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.common.redis.RedisKey;
import com.bnmotor.icv.tsp.cpsp.domain.request.RemoteVehControlVo;
import com.bnmotor.icv.tsp.cpsp.domain.request.RemoteVehInfoVo;
import com.bnmotor.icv.tsp.cpsp.engine.support.CPSPProxy;
import com.bnmotor.icv.tsp.cpsp.pojo.input.RemoteVehControlInput;
import com.bnmotor.icv.tsp.cpsp.pojo.input.RemoteVehInfoInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.RemoteVehControlOutput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.RemoteVehInfoOutput;
import com.bnmotor.icv.tsp.cpsp.service.IRemoteVehService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @ClassName: RemoteVehServiceImpl
* @Description: 家控车实现类
* @author: liuhuaqiao1
* @date: 2021/1/8
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Service
public class RemoteVehServiceImpl implements IRemoteVehService {

    @Autowired
    private CPSPProxy cpspProxy;

    @Override
    public RemoteVehInfoOutput getVehInfo(RemoteVehInfoVo vo) {
        RemoteVehInfoInput input = RemoteVehInfoInput.builder().build();
        input.setVin(vo.getVin());
        input.setReqSource(vo.getReqSource());
        input.setColumnNames(vo.getColumnNames());
        input.setGroupNames(vo.getGroupNames());
        input.setServiceCode(Constant.SERVICE_CODE);
        input.setCurrentAPI(Constant.REMOTE_VEH_INFO_API);
        input.setCacheKey(String.format(RedisKey.REMOTE_VEH_INFO_KEY,vo.getVin()));
        input.setCacheClass(RemoteVehInfoOutput.class);
        RemoteVehInfoOutput output = cpspProxy.call(input);
        return output;
    }

    @Override
    public RemoteVehControlOutput airConditionerSetOffOn(RemoteVehControlVo vo) {
        RemoteVehControlInput input = packageInput(vo);
        input.setCurrentAPI(Constant.REMOTE_VEH_CONTROL_AIR_OFFON_API);
        input.setCacheKey(String.format(RedisKey.REMOTE_VEH_CONTROL_AIR_OFFON_KEY,vo.getVin()));
        input.setCacheClass(RemoteVehControlOutput.class);
        RemoteVehControlOutput output = cpspProxy.call(input);
        return output;
    }

    @Override
    public RemoteVehControlOutput airConditionerSetTemperature(RemoteVehControlVo vo) {
        RemoteVehControlInput input = packageInput(vo);
        input.setCurrentAPI(Constant.REMOTE_VEH_CONTROL_AIR_TEMP_API);
        input.setCacheKey(String.format(RedisKey.REMOTE_VEH_CONTROL_AIR_TEMP_KEY,vo.getVin()));
        input.setCacheClass(RemoteVehControlOutput.class);
        RemoteVehControlOutput output = cpspProxy.call(input);
        return output;
    }

    @Override
    public RemoteVehControlOutput vehWindowSet(RemoteVehControlVo vo) {
        RemoteVehControlInput input = packageInput(vo);
        input.setCurrentAPI(Constant.REMOTE_VEH_CONTROL_WINDOWS_SET_API);
        input.setCacheKey(String.format(RedisKey.REMOTE_VEH_CONTROL_WINDOWS_SET_KEY,vo.getVin()));
        input.setCacheClass(RemoteVehControlOutput.class);
        RemoteVehControlOutput output = cpspProxy.call(input);
        return output;
    }

    private RemoteVehControlInput packageInput(RemoteVehControlVo vo){
        RemoteVehControlInput input = RemoteVehControlInput.builder().build();
        input.setVin(vo.getVin());
        input.setStateValue(vo.getStateValue());
        input.setReqSource(vo.getReqSource());
        input.setTimestamp(vo.getTimestamp());
        input.setServiceCode(Constant.SERVICE_CODE);
        return input;
    }
}
