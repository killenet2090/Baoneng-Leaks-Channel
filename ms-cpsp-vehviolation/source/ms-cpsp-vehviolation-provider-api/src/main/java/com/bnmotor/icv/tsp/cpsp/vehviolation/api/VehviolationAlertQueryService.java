package com.bnmotor.icv.tsp.cpsp.vehviolation.api;


import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.input.VehviolationAlertQueryInput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.input.VehviolationQueryInput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationAlertQueryOutput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationQueryOutput;

/**
* @ClassName: VehviolationAlertQueryService
* @Description: 违章提醒查询标准接口
* @author: liuhuaqiao1
* @date: 2021/1/8
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
public interface VehviolationAlertQueryService {

    /**
     * 违章提醒查询
     */
    default VehviolationAlertQueryOutput process(VehviolationAlertQueryInput input) {
        return null;
    }

}
