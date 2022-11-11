package com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output;

import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo.CPSPOutput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.vo.VioDetail;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.vo.VioSummary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
* @ClassName: VehviolationAlertQueryOutput
* @Description: 违章提醒查询出参
* @author: liuhuaqiao1
* @date: 2021/1/8
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehviolationAlertQueryOutput extends CPSPOutput {

    /**
     * 未办违章数
     */
    private Integer totalViolation;

}
