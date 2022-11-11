package com.bnmotor.icv.tsp.cpsp.api;

import com.bnmotor.icv.tsp.cpsp.pojo.input.EquipmentControlInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.EquipmentControlOutput;

/**
 * @ClassName: SmartHomeEquipmenConditionIService
 * @Description: 智能家居控制设备接口
 * @author: jiangchangyuan1
 * @date: 2021/2/24
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface SmartHomeEquipmenControlIService {
    /**
     * 智能家居控制设备
     */
    default EquipmentControlOutput process(EquipmentControlInput input) {
        return null;
    }
}
