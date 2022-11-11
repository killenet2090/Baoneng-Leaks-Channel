package com.bnmotor.icv.tsp.cpsp.vehviolation.api;


import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.input.VehviolationHotSpotQueryInput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.input.VehviolationQueryInput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationHotSpotQueryOutput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationQueryOutput;

/**
* @ClassName: VehviolationHotSpotQueryService
* @Description: 违章热点信息查询标准接口
* @author: liuhuaqiao1
* @date: 2021/1/7
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
public interface VehviolationHotSpotQueryService {

    /**
     * 违章热点信息查询
     */
    default VehviolationHotSpotQueryOutput process(VehviolationHotSpotQueryInput input) {
        return null;
    }

}
