package com.bnmotor.icv.tsp.cpsp.api;

import com.bnmotor.icv.tsp.cpsp.pojo.input.EquipmentListInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.EquipmentListOutput;

/**
 * @ClassName: SmartHomeEquipmentListIService
 * @Description: 智能家居查询设备列表接口
 * @author: jiangchangyuan1
 * @date: 2021/2/24
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface SmartHomeEquipmentListIService {
    /**
     * 智能家居查询设备列表
     */
    default EquipmentListOutput process(EquipmentListInput input) {
        return null;
    }
}
