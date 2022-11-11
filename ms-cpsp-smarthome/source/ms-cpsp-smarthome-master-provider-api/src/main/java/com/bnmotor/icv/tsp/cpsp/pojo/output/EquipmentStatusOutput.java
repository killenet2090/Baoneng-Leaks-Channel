package com.bnmotor.icv.tsp.cpsp.pojo.output;

import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.pojo.CPSPOutput;
import lombok.Data;

import java.util.Map;

/**
 * @ClassName: EquipmentStatusOutput
 * @Description: 设备状态响应实体
 * @author: jiangchangyuan1
 * @date: 2021/2/22
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class EquipmentStatusOutput extends CPSPOutput {
    /**
     * 车架号
     */
    private  String vin;
    /**
     * 设备状态：0-离线，1-开启，2-关闭
     */
    private String status;
    /**
     * 设备数据状态
     */
    private Map<String,String> dataStatus;
}
