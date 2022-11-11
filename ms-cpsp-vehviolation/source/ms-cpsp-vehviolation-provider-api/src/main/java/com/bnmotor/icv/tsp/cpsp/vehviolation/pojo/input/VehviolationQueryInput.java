package com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.input;

import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo.CPSPInput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationQueryOutput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @ClassName: VehviolationQueryInput
* @Description: 违章查询入参
* @author: liuhuaqiao1
* @date: 2021/1/7
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehviolationQueryInput extends CPSPInput<VehviolationQueryOutput> {

    /**
     * 车架号
     */
    private String vin;

}
