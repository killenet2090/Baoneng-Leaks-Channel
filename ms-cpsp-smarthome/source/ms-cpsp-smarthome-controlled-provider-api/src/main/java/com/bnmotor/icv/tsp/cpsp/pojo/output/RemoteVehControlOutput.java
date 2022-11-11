package com.bnmotor.icv.tsp.cpsp.pojo.output;

import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo.CPSPOutput;
import lombok.Data;

/**
* @ClassName: RemoteVehControlOutput
* @Description: 车辆远程控制输出类
* @author: liuhuaqiao1
* @date: 2021/3/8
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Data
public class RemoteVehControlOutput extends CPSPOutput {

    /**
     * 业务ID
     */
    private String businessId;

}
