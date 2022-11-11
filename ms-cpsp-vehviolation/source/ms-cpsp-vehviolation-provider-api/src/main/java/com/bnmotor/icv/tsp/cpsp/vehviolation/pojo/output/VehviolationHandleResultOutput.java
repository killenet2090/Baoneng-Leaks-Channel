package com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output;

import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo.CPSPOutput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @ClassName: VehviolationHandleResultOutput
* @Description: 违章办理结果查询出参
* @author: liuhuaqiao1
* @date: 2021/1/8
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehviolationHandleResultOutput extends CPSPOutput {

    /**
     * 违章办理状态
     */
    private String payStatus;

}
