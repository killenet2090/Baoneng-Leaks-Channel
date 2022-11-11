package com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @ClassName: VioSummary
* @Description: 违章概览实体类
* @author: liuhuaqiao1
* @date: 2021/1/7
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Data
@NoArgsConstructor
public class VioSummary {

    /**
     * 违章数
     */
    private String vioCount;

    /**
     * 罚款总金额
     */
    private String totalFine;

    /**
     * 待扣分
     */
    private String totalDegree;

}
