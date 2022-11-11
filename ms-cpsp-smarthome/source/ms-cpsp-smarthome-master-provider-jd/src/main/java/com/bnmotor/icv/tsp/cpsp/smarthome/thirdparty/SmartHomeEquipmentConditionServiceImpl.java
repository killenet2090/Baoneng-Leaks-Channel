package com.bnmotor.icv.tsp.cpsp.smarthome.thirdparty;

import com.bnmotor.icv.tsp.cpsp.api.SmartHomeEquipmenConditionIService;
import com.bnmotor.icv.tsp.cpsp.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.annotation.ThirdPartySvcAnno;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.strategy.AbstractStrategy;
import com.bnmotor.icv.tsp.cpsp.pojo.input.EquipmentConditionListInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.EquipmentConditionListOutput;
import com.bnmotor.icv.tsp.cpsp.pojo.vo.IndoorConditionVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @ClassName: SmartHomeEquipmentListServiceImpl
* @Description: 供应商查询设备状态实现类
* @author: jiangchangyuan1
* @date: 2021/2/22
* @Copyright: 2021 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Slf4j
@Service
@ThirdPartySvcAnno(provider = Constant.PROVIDER_CODE, service = Constant.SMART_HOME_EQUIPMENT_CONDITION)
public class SmartHomeEquipmentConditionServiceImpl implements SmartHomeEquipmenConditionIService, AbstractStrategy<EquipmentConditionListInput, EquipmentConditionListOutput> {

    @Override
    public EquipmentConditionListOutput call(EquipmentConditionListInput input) {
        EquipmentConditionListOutput output = this.process(input);
        return output;
    }

    @Override
    public EquipmentConditionListOutput process(EquipmentConditionListInput input) {
        EquipmentConditionListOutput output = new EquipmentConditionListOutput();
        output.setVin(input.getVin());
        List<IndoorConditionVo> indoorConditions = new ArrayList<>();
        IndoorConditionVo indoorConditionVo = new IndoorConditionVo();
        indoorConditionVo.setCondItem("空气质量");
        indoorConditionVo.setCondStatus("良好");
        indoorConditionVo.setLogoUrl("https://xxxxxxx.com/xxx.png");
        indoorConditions.add(indoorConditionVo);
        output.setIndoorConditions(indoorConditions);
        return output;
    }
}
