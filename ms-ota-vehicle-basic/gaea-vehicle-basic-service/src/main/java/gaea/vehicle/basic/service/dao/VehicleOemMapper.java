/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.vehicle.basic.service.dao;


import gaea.vehicle.basic.service.models.domain.VehicleOem;
import gaea.vehicle.basic.service.models.query.VehicleOemQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <pre>
 *   车辆主机厂表数据操作对象
 * </pre>
 *
 * @author   Wel.Lao
 * @version  1.0.0
 */
@Mapper
public interface VehicleOemMapper {
    /**
     * <pre>
     *  分页查询 车辆主机厂表数据
     * </pre>
     *
     * @param query 查询条件
     * @return 车辆主机厂表数据集合
     */
    List<VehicleOem> queryPage(VehicleOemQuery query);

    /**
     * <pre>
     * 根据查询条件统计车辆主机厂表数量
     * </pre>
     *
     * @param query 查询条件
     * @return 车辆主机厂表数量
     */
    int countPage(VehicleOemQuery query);

    /**
     * <pre>
     *  根据ID查询车辆主机厂表数据
     * </pre>
     *
     * @param vehicleOemId 车辆主机厂表Id
     * @return 单条记录数据
     */
    VehicleOem queryById(Long vehicleOemId);

    /**
     * <pre>
     *  保存信息
     * </pre>
     *
     * @param vehicleOem 数据信息
     */
    void insertVehicleOem(VehicleOem vehicleOem);

    /**
     * <pre>
     * 根据ID更新信息
     * </pre>
     *
     * @param vehicleOem 数据信息
     * @return 修改数量
     */
    int updateVehicleOem(VehicleOem vehicleOem);

    /**
     * <pre>
     *  根据ID删除信息
     * </pre>
     *
     * @param vehicleOemId 车辆主机厂表Id
     * @return 删除数量
     */
    int deleteById(Long vehicleOemId);

 }
