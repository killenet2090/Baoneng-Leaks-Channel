package com.bnmotor.icv.tsp.cpsp.smarthome.thirdparty;

import com.bnmotor.icv.tsp.cpsp.api.SmartHomeEquipmenPositionIService;
import com.bnmotor.icv.tsp.cpsp.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.annotation.ThirdPartySvcAnno;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.strategy.AbstractStrategy;
import com.bnmotor.icv.tsp.cpsp.pojo.input.EquipmentPositionInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.EquipmentPositionOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
* @ClassName: SmartHomeEquipmentListServiceImpl
* @Description: 供应商设备卡片排序实现类
* @author: jiangchangyuan1
* @date: 2021/2/22
* @Copyright: 2021 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Slf4j
@Service
@ThirdPartySvcAnno(provider = Constant.PROVIDER_CODE, service = Constant.SMART_HOME_EQUIPMENT_POSITION)
public class SmartHomeEquipmentPositionServiceImpl implements SmartHomeEquipmenPositionIService, AbstractStrategy<EquipmentPositionInput, EquipmentPositionOutput> {


    @Override
    public EquipmentPositionOutput call(EquipmentPositionInput equipmentPositionInputput) {
        EquipmentPositionOutput equipmentPositionOutput = this.process(equipmentPositionInputput);
        return equipmentPositionOutput;
    }

    @Override
    public EquipmentPositionOutput process(EquipmentPositionInput input) {
        EquipmentPositionOutput equipmentPositionOutput = new EquipmentPositionOutput();
        equipmentPositionOutput.setVin(input.getVin());
        return equipmentPositionOutput;
    }
}
