package com.bnmotor.icv.tsp.cpsp.smarthome.thirdparty;

import com.bnmotor.icv.tsp.cpsp.api.SmartHomeEquipmentListIService;
import com.bnmotor.icv.tsp.cpsp.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.annotation.ThirdPartySvcAnno;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.strategy.AbstractStrategy;
import com.bnmotor.icv.tsp.cpsp.pojo.input.EquipmentListInput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.EquipmentListOutput;
import com.bnmotor.icv.tsp.cpsp.pojo.vo.EquipmentVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @ClassName: SmartHomeEquipmentListServiceImpl
* @Description: 供应商查询设备列表实现类
* @author: jiangchangyuan1
* @date: 2021/2/5
* @Copyright: 2021 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Slf4j
@Service
@ThirdPartySvcAnno(provider = Constant.PROVIDER_CODE, service = Constant.SMART_HOME_EQUIPMENT_LIST)
public class SmartHomeEquipmentListServiceImpl implements SmartHomeEquipmentListIService, AbstractStrategy<EquipmentListInput, EquipmentListOutput> {


    @Override
    public EquipmentListOutput call(EquipmentListInput equipmentListInput) {
        EquipmentListOutput equipmentListOutput = process(equipmentListInput);
        return equipmentListOutput;
    }

    @Override
    public EquipmentListOutput process(EquipmentListInput input) {
        EquipmentListOutput equipmentListOutput = new EquipmentListOutput();

        //构造响应体
        List<EquipmentVo> equipmentVos = new ArrayList<>();
        for(int i=0;i<10;i++){
            log.info("当前构建第{}条设备信息",i);
            //构造设备信息
            EquipmentVo equipmentVo = new EquipmentVo();
            equipmentVo.setLogo("https://xxxxx.png"+i);
            equipmentVo.setEquipmentId("2486528723785"+i);
            equipmentVo.setIsControl(1);
            equipmentVo.setName("小米扫地机器人"+i);
            equipmentVo.setRoom("主卧"+i);
            equipmentVo.setStatus(1);
            equipmentVo.setRemark("当前温度26度");
            equipmentVo.setVin(input.getVin());
            equipmentVo.setDetailUrl("https://xxxxx.html"+i);
            equipmentVos.add(equipmentVo);
        }
        equipmentListOutput.setEquipments(equipmentVos);
        return equipmentListOutput;
    }
}
