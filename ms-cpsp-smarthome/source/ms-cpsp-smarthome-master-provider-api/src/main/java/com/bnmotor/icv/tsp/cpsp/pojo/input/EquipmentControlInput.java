package com.bnmotor.icv.tsp.cpsp.pojo.input;

import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo.CPSPInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.EquipmentControlOutput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @ClassName: EquipmentStatusOutput
 * @Description: 设备控制请求实体
 * @author: jiangchangyuan1
 * @date: 2021/2/22
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentControlInput extends CPSPInput<EquipmentControlOutput> {
    /**
     * 设备ID
     */
    private String equipmentId;
    /**
     * 设备最新状态：
     * 是否关闭：1-开启2-关闭
     */
    private String status;
    /**
     * 车架号
     */
    private String vin;
    /**
     * 设备数据状态
     */
    private Map<String,String> commonStatus;
}
