package com.bnmotor.icv.tsp.cpsp.pojo.input;

import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo.CPSPInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.RemoteVehControlOutput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: RemoteVehControlInput
 * @Description: 车辆远程控制请求体
 * @author: liuhuaqiao1
 * @date: 2021/3/8
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemoteVehControlInput extends CPSPInput<RemoteVehControlOutput> {

    /**
     * 车辆识别号
     */
    private String vin;

    /**
     * 开关状态（0-关 1-开）
     */
    private int stateValue;

    /**
     * 请求来源（5-第三方合作平台）
     */
    private int reqSource;

    /**
     * 请求时间戳(ms)
     */
    private Long timestamp;

}
