package com.bnmotor.icv.tsp.cpsp.api;

import com.bnmotor.icv.tsp.cpsp.pojo.input.RemoteVehInfoInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.RemoteVehInfoOutput;

/**
* @ClassName: SmartHomeRemoteVehInfoIService
* @Description: 智能家居车辆状态获取接口
* @author: liuhuaqiao1
* @date: 2021/3/9
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
public interface SmartHomeRemoteVehInfoIService {

    /**
     * 车辆状态获取
     */
    default RemoteVehInfoOutput process(RemoteVehInfoInput input) {
        return null;
    }

}
