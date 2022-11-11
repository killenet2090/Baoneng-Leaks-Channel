package com.bnmotor.icv.tsp.cpsp.pojo.input;

import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo.CPSPInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.EquipmentConditionListOutput;
import lombok.Builder;
import lombok.Data;

/**
 * @ClassName: EquipmentPositionInputput
 * @Description: 设备卡片排序请求实体
 * @author: jiangchangyuan1
 * @date: 2021/2/22
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Builder
public class EquipmentConditionListInput extends CPSPInput<EquipmentConditionListOutput> {
    /**
     * 车架号
     */
    private String vin;
}
