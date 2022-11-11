package com.bnmotor.icv.tsp.cpsp.vehviolation.api;


import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.input.VehviolationHandleInput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.input.VehviolationQueryInput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationHandleOutput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationQueryOutput;

/**
* @ClassName: VehviolationHandleService
* @Description: 违章办理标准接口
* @author: liuhuaqiao1
* @date: 2021/1/8
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
public interface VehviolationHandleService {

    /**
     * 违章办理
     */
    default VehviolationHandleOutput process(VehviolationHandleInput input) {
        return null;
    }

}
