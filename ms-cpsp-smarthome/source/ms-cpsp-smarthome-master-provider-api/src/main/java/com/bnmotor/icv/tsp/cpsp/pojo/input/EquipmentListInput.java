package com.bnmotor.icv.tsp.cpsp.pojo.input;

import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo.CPSPInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.EquipmentListOutput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: EquipmentListInput
 * @Description: 智能家居设备请求体
 * @author: jiangchangyuan1
 * @date: 2021/2/10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentListInput extends CPSPInput<EquipmentListOutput> {
    /**
     * 排序方式：0-家居名称(默认),1-最近使用，2-添加时间,3-自定义
     */
    private String sorting;
    /**
     * 车架号
     */
    private String vin;
    /**
     * 当前页数
     */
    private Integer current;
    /**
     * 每页条数
     */
    private Integer pageSize;
}
