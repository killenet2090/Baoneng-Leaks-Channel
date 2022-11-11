/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.vehicle.basic.service.dao;


import gaea.vehicle.basic.service.models.domain.VehicleSeries;
import org.apache.ibatis.annotations.Mapper;
import gaea.vehicle.basic.service.models.query.VehicleSeriesQuery;

import java.util.List;

/**
 * <pre>
 *   车辆系列表数据操作对象
 * </pre>
 *
 * @author   jiankang.xia
 * @version  1.0.0
 */
@Mapper
public interface VehicleSeriesMapper {
    /**
     * <pre>
     *  分页查询 车辆系列表数据
     * </pre>
     *
     * @param query 查询条件
     * @return 车辆系列表数据集合
     */
    List<VehicleSeries> queryPage(VehicleSeriesQuery query);

    List<VehicleSeries> queryNameAndId(VehicleSeries vehicleSeries);

    /**
     * <pre>
     * 根据查询条件统计车辆系列表数量
     * </pre>
     *
     * @param query 查询条件
     * @return 车辆系列表数量
     */
    int countPage(VehicleSeriesQuery query);

    /**
     * <pre>
     *  根据ID查询车辆系列表数据
     * </pre>
     *
     * @param vehicleSeriesId 车辆系列表Id
     * @return 单条记录数据
     */
    VehicleSeries queryById(Long vehicleSeriesId);

    /**
     * <pre>
     *  保存信息
     * </pre>
     *
     * @param vehicleSeries 数据信息
     */
    Integer insertVehicleSeries(VehicleSeries vehicleSeries);

    /**
     * <pre>
     * 根据ID更新信息
     * </pre>
     *
     * @param vehicleSeries 数据信息
     * @return 修改数量
     */
    int updateVehicleSeries(VehicleSeries vehicleSeries);

    /**
     * <pre>
     *  根据ID删除信息
     * </pre>
     *
     * @param vehicleSeriesId 车辆系列表Id
     * @return 删除数量
     */
    int deleteById(Long vehicleSeriesId);

 }
