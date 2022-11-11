/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.vehicle.basic.service.service;

import gaea.vehicle.basic.service.models.domain.VehicleSeries;
import gaea.vehicle.basic.service.models.query.VehicleSeriesQuery;

import java.util.List;

/**
 * <pre>
 * 车辆系列表业务代码,流程控制和业务流程主要还是在Api上写.
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
public interface VehicleSeriesService {
    /**
     * <pre>
     * 分页查询车辆系列表数据
     * </pre>
     *
     * @param query 查询条件,继承分页信息
     * @return 车辆系列表数据集合
     */
    List<VehicleSeries> queryPage(VehicleSeriesQuery query);

    /**
     * <pre>
     * 根据ID查询车辆系列表数据
     * </pre>
     *
     * @param vehicleSeriesId 车辆系列表Id
     * @return 车辆系列表
     */
    VehicleSeries queryById(Long vehicleSeriesId);

    /**
     * <pre>
     * 保存信息
     * </pre>
     *
     * @param vehicleSeries 车辆系列表
     * @return 保存数据ID
     */
    int insertVehicleSeries(VehicleSeries vehicleSeries);

    /**
     * <pre>
     * 根据ID更新信息
     * </pre>
     *
     * @param vehicleSeries 车辆系列表
     * @return 更新数量
     */
    int updateVehicleSeries(VehicleSeries vehicleSeries);

    /**
     * <pre>
     * 根据ID删除信息
     * </pre>
     *
     * @param vehicleSeriesId 车辆系列表Id
     * @return 删除数量
     */
    int deleteById(Long vehicleSeriesId);

}
