package com.bnmotor.icv.tsp.cpsp.smarthome.thirdparty;

import com.bnmotor.icv.tsp.cpsp.api.SmartHomeEquipmenStatusIService;
import com.bnmotor.icv.tsp.cpsp.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.annotation.ThirdPartySvcAnno;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.strategy.AbstractStrategy;
import com.bnmotor.icv.tsp.cpsp.pojo.input.EquipmentStatusInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.EquipmentStatusOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
* @ClassName: SmartHomeEquipmentListServiceImpl
* @Description: 供应商查询设备状态实现类
* @author: jiangchangyuan1
* @date: 2021/2/22
* @Copyright: 2021 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Slf4j
@Service
@ThirdPartySvcAnno(provider = Constant.PROVIDER_CODE, service = Constant.SMART_HOME_EQUIPMENT_STATUS)
public class SmartHomeEquipmentStatusServiceImpl implements SmartHomeEquipmenStatusIService, AbstractStrategy<EquipmentStatusInput, EquipmentStatusOutput> {


    @Override
    public EquipmentStatusOutput call(EquipmentStatusInput equipmentStatusInputput) {
        EquipmentStatusOutput equipmentStatusOutput = this.process(equipmentStatusInputput);
        return equipmentStatusOutput;
    }

    @Override
    public EquipmentStatusOutput process(EquipmentStatusInput input) {
        EquipmentStatusOutput equipmentStatusOutput = new EquipmentStatusOutput();
        Map<String,String> dataStatus = new HashMap<>();
        equipmentStatusOutput.setStatus("1");
        equipmentStatusOutput.setVin(input.getVin());
        dataStatus.put("wind","4");
        dataStatus.put("temperature","26");
        equipmentStatusOutput.setDataStatus(dataStatus);
        return equipmentStatusOutput;
    }
}
