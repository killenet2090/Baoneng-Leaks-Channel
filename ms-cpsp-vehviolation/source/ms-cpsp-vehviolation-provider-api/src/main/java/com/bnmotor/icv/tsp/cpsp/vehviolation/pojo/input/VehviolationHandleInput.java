package com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.input;

import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo.CPSPInput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationHandleOutput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
* @ClassName: VehviolationHandleInput
* @Description: 违章办理入参
* @author: liuhuaqiao1
* @date: 2021/1/8
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehviolationHandleInput extends CPSPInput<VehviolationHandleOutput> {

    /**
     * 车架号
     */
    private String vin;

    /**
     * 违章编号
     */
    private List<String> violationIds;

}
