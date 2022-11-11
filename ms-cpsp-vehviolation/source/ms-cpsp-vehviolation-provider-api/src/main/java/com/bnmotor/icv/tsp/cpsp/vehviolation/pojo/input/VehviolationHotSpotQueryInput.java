package com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.input;

import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo.CPSPInput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationHotSpotQueryOutput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @ClassName: VehviolationHotSpotQueryInput
* @Description: 违章热点查询入参
* @author: liuhuaqiao1
* @date: 2021/1/8
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehviolationHotSpotQueryInput extends CPSPInput<VehviolationHotSpotQueryOutput> {

    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 违章类型
     */
    private String type;

    /**
     * 地图类型
     */
    private String mapType;

    /**
     * 半径
     */
    private String radius;

}
