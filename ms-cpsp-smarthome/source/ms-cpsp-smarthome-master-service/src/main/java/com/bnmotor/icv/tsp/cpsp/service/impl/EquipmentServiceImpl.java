package com.bnmotor.icv.tsp.cpsp.service.impl;

import com.bnmotor.icv.tsp.cpsp.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.common.redis.RedisKey;
import com.bnmotor.icv.tsp.cpsp.engine.support.CPSPProxy;
import com.bnmotor.icv.tsp.cpsp.pojo.input.*;
import com.bnmotor.icv.tsp.cpsp.pojo.output.*;
import com.bnmotor.icv.tsp.cpsp.service.IEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: EquipmentServiceImpl
 * @Description: 智能家居设备服务实现类
 * @author: jiangchangyuan1
 * @date: 2021/2/10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
public class EquipmentServiceImpl implements IEquipmentService {

    @Autowired
    private CPSPProxy cpspProxy;

    /**
     * 查询设备列表
     * @param sorting 排序方式：0-家居名称(默认),1-最近使用，2-添加时间,3-自定义
     * @param vin 车架号
     * @param current 当前页数
     * @param pageSize 每页条数
     * @return
     */
    @Override
    public EquipmentListOutput getEquipmentList(Integer sorting,String vin,Integer current,Integer pageSize) {
        EquipmentListInput input = EquipmentListInput.builder().build();
        if(null != sorting){
            input.setSorting(String.valueOf(sorting));
        }else{
            input.setSorting("0");
        }
        if(!StringUtils.isEmpty(vin)){
            input.setVin(vin);
        }
        if(null != current){
            input.setCurrent(current);
        }
        if(null != pageSize){
            input.setPageSize(pageSize);
        }
        input.setServiceCode(Constant.SERVICE_CODE);
        input.setCurrentAPI(Constant.SMART_HOME_EQUIPMENT_LIST);
        input.setCacheKey(String.format(RedisKey.SMART_HOME_EQUIPMENT_LIST_KEY, sorting));
        input.setCacheClass(EquipmentListOutput.class);
        EquipmentListOutput output = cpspProxy.call(input);
        return output;
    }

    /**
     * 查询设备状态接口
     * @param equipmentId 设备id
     * @param vin 车架号
     * @return
     */
    @Override
    public EquipmentStatusOutput getEquipmentStatus(String equipmentId,String vin) {
        EquipmentStatusInput input = EquipmentStatusInput.builder().build();
        if(!StringUtils.isEmpty(equipmentId)){
            input.setEquipmentId(equipmentId);
        }
        if(!StringUtils.isEmpty(vin)){
            input.setVin(vin);
        }
        input.setServiceCode(Constant.SERVICE_CODE);
        input.setCurrentAPI(Constant.SMART_HOME_EQUIPMENT_STATUS);
        input.setCacheKey(String.format(RedisKey.SMART_HOME_EQUIPMENT_STATUS_KEY, equipmentId));
        input.setCacheClass(EquipmentStatusOutput.class);
        EquipmentStatusOutput output = cpspProxy.call(input);
        return output;
    }

    /**
     * 设备控制接口
     * @param equipmentId 设备id
     * @param status 设备最新状态
     * @param vin 车架号
     * @param commonStatus 预留字段
     * @return
     */
    @Override
    public EquipmentControlOutput euipmentControl(String equipmentId, Integer status,String vin, Map<String, String> commonStatus) {
        EquipmentControlInput input = EquipmentControlInput.builder().build();
        if(!StringUtils.isEmpty(equipmentId)){
            input.setEquipmentId(equipmentId);
            input.setStatus(String.valueOf(status));
            input.setVin(vin);
            input.setCommonStatus(commonStatus);
        }
        input.setServiceCode(Constant.SERVICE_CODE);
        input.setCurrentAPI(Constant.SMART_HOME_EQUIPMENT_CONTROL);
        input.setCacheKey(String.format(RedisKey.SMART_HOME_EQUIPMENT_CONTROL_KEY, equipmentId));
        input.setCacheClass(EquipmentControlOutput.class);
        EquipmentControlOutput output = cpspProxy.call(input);
        return output;
    }

    /**
     * 设备卡片排序
     * @param vin 车架号
     * @param equipmentIds 最终排序设备ID顺序
     * @return
     */
    @Override
    public EquipmentPositionOutput euipmentPositionSet(String vin,List<String> equipmentIds) {
        EquipmentPositionInput input = EquipmentPositionInput.builder().build();
        if(!StringUtils.isEmpty(equipmentIds) && equipmentIds.size() > 0){
            input.setEquipmentIds(equipmentIds);
        }
        if(!StringUtils.isEmpty(vin)){
            input.setVin(vin);
        }
        input.setServiceCode(Constant.SERVICE_CODE);
        input.setCurrentAPI(Constant.SMART_HOME_EQUIPMENT_POSITION);
//        input.setCacheKey(String.format(RedisKey.SMART_HOME_EQUIPMENT_POSITION, equipmentId));
//        input.setCacheClass(EquipmentPositionOutput.class);
        EquipmentPositionOutput output = cpspProxy.call(input);
        return output;
    }

    /**
     * 家庭数据采集信息
     * @param vin 车架号
     * @return
     */
    @Override
    public EquipmentConditionListOutput getConditionData(String vin) {
        EquipmentConditionListInput input = EquipmentConditionListInput.builder().build();
        if(!StringUtils.isEmpty(vin)){
            input.setVin(vin);
        }
        input.setServiceCode(Constant.SERVICE_CODE);
        input.setCurrentAPI(Constant.SMART_HOME_EQUIPMENT_CONDITION);
        input.setCacheKey(String.format(RedisKey.SMART_HOME_EQUIPMENT_CONDITION_KEY, vin));
        input.setCacheClass(EquipmentConditionListOutput.class);
        EquipmentConditionListOutput output = cpspProxy.call(input);
        return output;
    }
}
