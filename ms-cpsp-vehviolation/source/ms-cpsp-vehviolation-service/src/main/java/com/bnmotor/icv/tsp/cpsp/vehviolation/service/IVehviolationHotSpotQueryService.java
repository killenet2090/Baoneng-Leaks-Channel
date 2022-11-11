package com.bnmotor.icv.tsp.cpsp.vehviolation.service;

import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationHotSpotQueryOutput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationQueryOutput;
import org.springframework.web.bind.annotation.RequestParam;

/**
* @ClassName: IVehviolationHotSpotQueryService
* @Description: 违章热点信息查询
* @author: liuhuaqiao1
* @date: 2021/1/8
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
public interface IVehviolationHotSpotQueryService {

    /**
     * 违章热点信息查询
     */
    VehviolationHotSpotQueryOutput queryVehviolationHotSpot(String lng, String lat, String type, String mapType, String radius);
}
