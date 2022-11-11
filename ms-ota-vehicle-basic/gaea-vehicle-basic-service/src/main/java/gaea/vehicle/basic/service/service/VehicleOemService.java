/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.vehicle.basic.service.service;

import gaea.vehicle.basic.service.models.domain.VehicleOem;
import gaea.vehicle.basic.service.models.query.VehicleOemQuery;

import java.util.List;

/**
 * <pre>
 * 车辆主机厂表业务代码,流程控制和业务流程主要还是在Api上写.
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
public interface VehicleOemService {
    /**
     * <pre>
     * 分页查询车辆主机厂表数据
     * </pre>
     *
     * @param query 查询条件,继承分页信息
     * @return 车辆主机厂表数据集合
     */
    List<VehicleOem> queryPage(VehicleOemQuery query);

    /**
     * <pre>
     * 根据ID查询车辆主机厂表数据
     * </pre>
     *
     * @param vehicleOemId 车辆主机厂表Id
     * @return 车辆主机厂表
     */
    VehicleOem queryById(Long vehicleOemId);

    /**
     * <pre>
     * 保存信息
     * </pre>
     *
     * @param vehicleOem 车辆主机厂表
     * @return 保存数据ID
     */
    int insertVehicleOem(VehicleOem vehicleOem);

    /**
     * <pre>
     * 根据ID更新信息
     * </pre>
     *
     * @param vehicleOem 车辆主机厂表
     * @return 更新数量
     */
    int updateVehicleOem(VehicleOem vehicleOem);

    /**
     * <pre>
     * 根据ID删除信息
     * </pre>
     *
     * @param vehicleOemId 车辆主机厂表Id
     * @return 删除数量
     */
    int deleteById(Long vehicleOemId);

}
