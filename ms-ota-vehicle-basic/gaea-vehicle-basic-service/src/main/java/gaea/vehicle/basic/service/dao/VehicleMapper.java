/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.vehicle.basic.service.dao;

import gaea.vehicle.basic.service.models.domain.Vehicle;
import gaea.vehicle.basic.service.models.query.VehicleQuery;
import gaea.vehicle.basic.service.models.query.VehicleConditionQuery;
import gaea.vehicle.basic.service.models.vo.VehicleConditionPageVO;

import java.util.List;

/**
 * <pre>
 *   车辆基础信息表数据操作对象
 * </pre>
 *
 * @author   jiankang.xia
 * @version  1.0.0
 */
public interface VehicleMapper {
    /**
     * <pre>
     *  分页查询 车辆基础信息表数据
     * </pre>
     *
     * @param query 查询条件
     * @return 车辆基础信息表数据集合
     */
    List<Vehicle> queryPage(VehicleQuery query);

    List<Vehicle> queryNameAndId(Vehicle vehicle);

    /**
     * <pre>
     * 根据查询条件统计车辆基础信息表数量
     * </pre>
     *
     * @param query 查询条件
     * @return 车辆基础信息表数量
     */
    int countPage(VehicleQuery query);

    /**
     * <pre>
     *  根据ID查询车辆基础信息表数据
     * </pre>
     *
     * @param vehicleId 车辆基础信息表Id
     * @return 单条记录数据
     */
    Vehicle queryById(Long vehicleId);

    /**
     * <pre>
     *  保存信息
     * </pre>
     *
     * @param vehicle 数据信息
     */
    void insertVehicle(Vehicle vehicle);

    /**
     * <pre>
     * 根据ID更新信息
     * </pre>
     *
     * @param vehicle 数据信息
     * @return 修改数量
     */
    int updateVehicle(Vehicle vehicle);

    /**
     * <pre>
     *  根据ID删除信息
     * </pre>
     *
     * @param vehicleId 车辆基础信息表Id
     * @return 删除数量
     */
    int deleteById(Long vehicleId);

    /**
     * 车辆条件查询
     * @param vehicleConditionQuery
     * @return
     */
    List<VehicleConditionPageVO> queryByConditions(VehicleConditionQuery vehicleConditionQuery);

    int countConditionsPage(VehicleConditionQuery vehicleConditionQuery);
}
