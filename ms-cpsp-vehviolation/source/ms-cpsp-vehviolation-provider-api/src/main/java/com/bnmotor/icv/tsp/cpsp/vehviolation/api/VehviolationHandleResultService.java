package com.bnmotor.icv.tsp.cpsp.vehviolation.api;


import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.input.VehviolationHandleResultInput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationHandleResultOutput;

/**
* @ClassName: VehviolationHandleResultService
* @Description: 违章办理结果查询标准接口
* @author: liuhuaqiao1
* @date: 2021/1/8
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
public interface VehviolationHandleResultService {

    /**
     * 违章办理
     */
    default VehviolationHandleResultOutput process(VehviolationHandleResultInput input) {
        return null;
    }

}
