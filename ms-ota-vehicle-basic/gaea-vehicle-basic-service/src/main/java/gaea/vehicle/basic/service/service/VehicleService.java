/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.vehicle.basic.service.service;

import gaea.vehicle.basic.service.models.domain.Vehicle;
import gaea.vehicle.basic.service.models.query.VehicleQuery;
import gaea.vehicle.basic.service.models.request.VehicleReq;
import gaea.vehicle.basic.service.models.query.VehicleConditionQuery;
import gaea.vehicle.basic.service.models.vo.VehicleConditionPageVO;
import gaea.vehicle.basic.service.models.vo.VehicleConditionVO;

import java.util.List;

/**
 * <pre>
 * 车辆基础信息表业务代码,流程控制和业务流程主要还是在Api上写.
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
public interface VehicleService {
    /**
     * <pre>
     * 分页查询车辆基础信息表数据
     * </pre>
     *
     * @param query 查询条件,继承分页信息
     * @return 车辆基础信息表数据集合
     */
    List<Vehicle> queryPage(VehicleQuery query);

    /**
     * <pre>
     * 根据ID查询车辆基础信息表数据
     * </pre>
     *
     * @param vehicleId 车辆基础信息表Id
     * @return 车辆基础信息表
     */
    Vehicle queryById(Long vehicleId);

    /**
     * <pre>
     * 保存信息
     * </pre>
     *
     * @param vehicle 车辆基础信息表
     * @return 保存数据ID
     */
    int insertVehicle(Vehicle vehicle);

    /**
     * <pre>
     * 根据ID更新信息
     * </pre>
     *
     * @param vehicle 车辆基础信息表
     * @return 更新数量
     */
    int updateVehicle(Vehicle vehicle);

    /**
     * <pre>
     * 根据ID删除信息
     * </pre>
     *
     * @param vehicleId 车辆基础信息表Id
     * @return 删除数量
     */
    int deleteById(Long vehicleId);

    List<Vehicle> queryList(VehicleReq vehicleReq);

    VehicleConditionVO queryConditions();

    List<VehicleConditionPageVO> queryByConditions(VehicleConditionQuery vehicleConditionQuery);
}
