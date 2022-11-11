package com.bnmotor.icv.tsp.cpsp.pojo.input;

import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo.CPSPInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.RemoteVehControlOutput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.RemoteVehInfoOutput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName: RemoteVehInfoInput
 * @Description: 车辆状态查询请求体
 * @author: liuhuaqiao1
 * @date: 2021/3/8
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemoteVehInfoInput extends CPSPInput<RemoteVehInfoOutput> {

    /**
     * 车辆识别号
     */
    private String vin;

    /**
     * 请求来源（5-第三方合作平台）
     */
    private int reqSource;

    /**
     * 查询字段名称列表
     */
    private List<String> columnNames;

    /**
     * 查询组名称列表
     */
    private List<String> groupNames;

}
