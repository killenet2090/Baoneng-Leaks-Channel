/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.vehicle.basic.service.service;

import gaea.vehicle.basic.service.models.domain.VehicleModel;
import gaea.vehicle.basic.service.models.query.VehicleModelQuery;

import java.util.List;

/**
 * <pre>
 * 业务代码,流程控制和业务流程主要还是在Api上写.
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
public interface VehicleModelService {
    /**
     * <pre>
     * 分页查询数据
     * </pre>
     *
     * @param query 查询条件,继承分页信息
     * @return 数据集合
     */
    List<VehicleModel> queryPage(VehicleModelQuery query);

    /**
     * <pre>
     * 根据ID查询数据
     * </pre>
     *
     * @param vehicleModelId Id
     * @return 
     */
    VehicleModel queryById(Long vehicleModelId);

    /**
     * <pre>
     * 保存信息
     * </pre>
     *
     * @param vehicleModel 
     * @return 保存数据ID
     */
    int insertVehicleModel(VehicleModel vehicleModel);

    /**
     * <pre>
     * 根据ID更新信息
     * </pre>
     *
     * @param vehicleModel 
     * @return 更新数量
     */
    int updateVehicleModel(VehicleModel vehicleModel);

    /**
     * <pre>
     * 根据ID删除信息
     * </pre>
     *
     * @param vehicleModelId Id
     * @return 删除数量
     */
    int deleteById(Long vehicleModelId);

}
