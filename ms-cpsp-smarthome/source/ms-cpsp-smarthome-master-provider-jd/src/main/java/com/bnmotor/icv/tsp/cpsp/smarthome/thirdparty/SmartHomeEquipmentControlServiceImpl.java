package com.bnmotor.icv.tsp.cpsp.smarthome.thirdparty;

import com.bnmotor.icv.tsp.cpsp.api.SmartHomeEquipmenControlIService;
import com.bnmotor.icv.tsp.cpsp.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.annotation.ThirdPartySvcAnno;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.strategy.AbstractStrategy;
import com.bnmotor.icv.tsp.cpsp.pojo.input.EquipmentControlInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.EquipmentControlOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
* @ClassName: SmartHomeEquipmentListServiceImpl
* @Description: 供应商设备控制实现类
* @author: jiangchangyuan1
* @date: 2021/2/22
* @Copyright: 2021 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Slf4j
@Service
@ThirdPartySvcAnno(provider = Constant.PROVIDER_CODE, service = Constant.SMART_HOME_EQUIPMENT_CONTROL)
public class SmartHomeEquipmentControlServiceImpl implements SmartHomeEquipmenControlIService, AbstractStrategy<EquipmentControlInput, EquipmentControlOutput> {


    @Override
    public EquipmentControlOutput call(EquipmentControlInput equipmentControlInputput) {
        EquipmentControlOutput equipmentControlOutput = this.process(equipmentControlInputput);
        return equipmentControlOutput;
    }

    @Override
    public EquipmentControlOutput process(EquipmentControlInput input) {
        EquipmentControlOutput equipmentControlOutput = new EquipmentControlOutput();
        equipmentControlOutput.setVin(input.getVin());
        return equipmentControlOutput;
    }
}
