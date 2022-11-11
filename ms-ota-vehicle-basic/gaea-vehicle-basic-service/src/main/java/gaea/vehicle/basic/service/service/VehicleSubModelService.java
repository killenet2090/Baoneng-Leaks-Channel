/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.vehicle.basic.service.service;

import gaea.vehicle.basic.service.models.domain.VehicleSubModel;
import gaea.vehicle.basic.service.models.query.VehicleSubModelQuery;

import java.util.List;

/**
 * <pre>
 * 车辆配置表业务代码,流程控制和业务流程主要还是在Api上写.
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
public interface VehicleSubModelService {
    /**
     * <pre>
     * 分页查询车辆配置表数据
     * </pre>
     *
     * @param query 查询条件,继承分页信息
     * @return 车辆配置表数据集合
     */
    List<VehicleSubModel> queryPage(VehicleSubModelQuery query);

    /**
     * <pre>
     * 根据ID查询车辆配置表数据
     * </pre>
     *
     * @param vehicleSubModelId 车辆配置表Id
     * @return 车辆配置表
     */
    VehicleSubModel queryById(Long vehicleSubModelId);

    /**
     * <pre>
     * 保存信息
     * </pre>
     *
     * @param vehicleSubModel 车辆配置表
     * @return 保存数据ID
     */
    Long insertVehicleSubModel(VehicleSubModel vehicleSubModel);

    /**
     * <pre>
     * 根据ID更新信息
     * </pre>
     *
     * @param vehicleSubModel 车辆配置表
     * @return 更新数量
     */
    int updateVehicleSubModel(VehicleSubModel vehicleSubModel);

    /**
     * <pre>
     * 根据ID删除信息
     * </pre>
     *
     * @param vehicleSubModelId 车辆配置表Id
     * @return 删除数量
     */
    int deleteById(Long vehicleSubModelId);

}
