/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.vehicle.basic.service.service;

import gaea.vehicle.basic.service.models.domain.VehicleBrand;
import gaea.vehicle.basic.service.models.query.VehicleBrandQuery;

import java.util.List;

/**
 * <pre>
 * 车辆品牌表业务代码,流程控制和业务流程主要还是在Api上写.
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
public interface VehicleBrandService {
    /**
     * <pre>
     * 分页查询车辆品牌表数据
     * </pre>
     *
     * @param query 查询条件,继承分页信息
     * @return 车辆品牌表数据集合
     */
    List<VehicleBrand> queryPage(VehicleBrandQuery query);

    List<VehicleBrand> queryNameAndId(VehicleBrandQuery query);

    /**
     * <pre>
     * 根据ID查询车辆品牌表数据
     * </pre>
     *
     * @param vehicleBrandId 车辆品牌表Id
     * @return 车辆品牌表
     */
    VehicleBrand queryById(Long vehicleBrandId);

    /**
     * <pre>
     * 保存信息
     * </pre>
     *
     * @param vehicleBrand 车辆品牌表
     * @return 保存数据ID
     */
    int insertVehicleBrand(VehicleBrand vehicleBrand);

    /**
     * <pre>
     * 根据ID更新信息
     * </pre>
     *
     * @param vehicleBrand 车辆品牌表
     * @return 更新数量
     */
    int updateVehicleBrand(VehicleBrand vehicleBrand);

    /**
     * <pre>
     * 根据ID删除信息
     * </pre>
     *
     * @param vehicleBrandId 车辆品牌表Id
     * @return 删除数量
     */
    int deleteById(Long vehicleBrandId);

}
