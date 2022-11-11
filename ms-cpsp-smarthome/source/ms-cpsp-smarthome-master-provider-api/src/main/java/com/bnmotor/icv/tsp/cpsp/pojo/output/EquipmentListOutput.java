package com.bnmotor.icv.tsp.cpsp.pojo.output;

import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo.CPSPOutput;
import com.bnmotor.icv.tsp.cpsp.pojo.vo.EquipmentVo;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: EquipmentListOutput
 * @Description: 智能家居设备响应体
 * @author: jiangchangyuan1
 * @date: 2021/2/10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class EquipmentListOutput extends CPSPOutput {
    List<EquipmentVo> equipments;
}
