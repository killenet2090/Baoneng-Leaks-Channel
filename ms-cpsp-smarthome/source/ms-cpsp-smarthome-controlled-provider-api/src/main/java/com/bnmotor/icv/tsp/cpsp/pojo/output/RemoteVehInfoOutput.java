package com.bnmotor.icv.tsp.cpsp.pojo.output;

import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo.CPSPOutput;
import lombok.Data;

/**
* @ClassName: RemoteVehInfoOutput
* @Description: 车辆状态查询输出类
* @author: liuhuaqiao1
* @date: 2021/3/8
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Data
public class RemoteVehInfoOutput extends CPSPOutput {

    /**
     * 空调开关状态
     */
    private String airConditionStatusOffOn;

    /**
     * 续航里程
     */
    private String distanceToEmpty;

    /**
     * 电量状态
     */
    private String electricityRemain;

    /**
     * 车窗状态
     */
    private String windowStatus;

    /**
     * 充电状态
     */
    private String chargingState;

    /**
     * 时间戳
     */
    private Long timestamp;

}
