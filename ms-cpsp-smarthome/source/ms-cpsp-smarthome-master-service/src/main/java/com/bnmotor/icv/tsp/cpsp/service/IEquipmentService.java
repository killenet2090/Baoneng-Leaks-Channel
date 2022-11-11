package com.bnmotor.icv.tsp.cpsp.service;

import com.bnmotor.icv.tsp.cpsp.pojo.output.*;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: IEquipmentService
 * @Description: 智能家居设备接口
 * @author: jiangchangyuan1
 * @date: 2021/2/10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IEquipmentService {
    /**
     * 查询设备接口
     * @param sorting 排序方式：0-家居名称(默认),1-最近使用，2-添加时间,3-自定义
     * @param vin 车架号
     * @param current 当前页数
     * @param pageSize 每页条数
     * @return
     */
    EquipmentListOutput getEquipmentList(Integer sorting,String vin,Integer current,Integer pageSize);

    /**
     * 查询设备状态接口
     * @param equipmentId 设备id
     * @param vin 车架号
     *
     * @return
     */
    EquipmentStatusOutput getEquipmentStatus(String equipmentId,String vin);

    /**
     * 设备控制接口
     * @param equipmentId 设备id
     * @param status 设备最新状态
     * @param commonStatus 预留字段
     * @param vin 车架号
     * @return
     */
    EquipmentControlOutput euipmentControl(String equipmentId, Integer status,String vin, Map<String, String> commonStatus);

    /**
     * 设备卡片排序
     * @param equipmentIds 最终排序设备ID顺序
     * @param vin 车架号
     * @return
     */
    EquipmentPositionOutput euipmentPositionSet(String vin,List<String> equipmentIds);

    /**
     * 家庭数据采集
     * @param vin 车架号
     * @return
     */
    EquipmentConditionListOutput getConditionData(String vin);

}
