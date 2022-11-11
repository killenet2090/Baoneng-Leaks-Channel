package com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @ClassName: VioDetail
* @Description: 违章明细实体类
* @author: liuhuaqiao1
* @date: 2021/1/7
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Data
@NoArgsConstructor
public class VioDetail {

    /**
     * 违章时间
     */
    private String time;

    /**
     * 违章地点
     */
    private String location;

    /**
     * 违章原因
     */
    private String reason;

    /**
     * 违章罚款金额
     */
    private String fine;

    /**
     * 违章扣分
     */
    private String degree;

    /**
     * 是否可代办， 1-可代办， 0-不可代办
     */
    private String canProcess;

    /**
     * 违章记录状态
     */
    private String status;

    /**
     * 第三方平台违章记录ID
     */
    private String violationId;

}
