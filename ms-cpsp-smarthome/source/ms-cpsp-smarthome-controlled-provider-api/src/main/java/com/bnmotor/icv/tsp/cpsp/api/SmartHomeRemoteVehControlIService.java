package com.bnmotor.icv.tsp.cpsp.api;

import com.bnmotor.icv.tsp.cpsp.pojo.input.RemoteVehControlInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.RemoteVehControlOutput;

/**
* @ClassName: SmartHomeRemoteVehControlIService
* @Description: 智能家居控制车辆接口
* @author: liuhuaqiao1
* @date: 2021/3/9
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
public interface SmartHomeRemoteVehControlIService {

    /**
     * 设置空调开关
     */
    default RemoteVehControlOutput vehAirConditionerSetOffOnProcess(RemoteVehControlInput input) {
        return null;
    }

    /**
     * 设置空调温度
     */
    default RemoteVehControlOutput vehAirConditionerSetTempProcess(RemoteVehControlInput input) {
        return null;
    }

    /**
     * 设置车窗状态
     */
    default RemoteVehControlOutput vehWindowSetProcess(RemoteVehControlInput input) {
        return null;
    }

}
