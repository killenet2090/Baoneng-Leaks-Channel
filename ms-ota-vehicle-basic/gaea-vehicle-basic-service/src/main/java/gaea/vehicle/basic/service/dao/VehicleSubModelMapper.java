/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.vehicle.basic.service.dao;

import gaea.vehicle.basic.service.models.domain.VehicleModel;
import gaea.vehicle.basic.service.models.domain.VehicleSubModel;
import gaea.vehicle.basic.service.models.query.VehicleSubModelQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <pre>
 *   车辆配置表数据操作对象
 * </pre>
 *
 * @author   JianKang.Xia
 * @version  1.0.0
 */
@Mapper
public interface VehicleSubModelMapper {
    /**
     * <pre>
     *  分页查询 车辆配置表数据
     * </pre>
     *
     * @param query 查询条件
     * @return 车辆配置表数据集合
     */
    List<VehicleSubModel> queryPage(VehicleSubModelQuery query);

    List<VehicleSubModel> queryNameAndId(VehicleModel vehicleModel);

    /**
     * <pre>
     * 根据查询条件统计车辆配置表数量
     * </pre>
     *
     * @param query 查询条件
     * @return 车辆配置表数量
     */
    int countPage(VehicleSubModelQuery query);

    /**
     * <pre>
     *  根据ID查询车辆配置表数据
     * </pre>
     *
     * @param vehicleSubModelId 车辆配置表Id
     * @return 单条记录数据
     */
    VehicleSubModel queryById(Long vehicleSubModelId);

    /**
     * <pre>
     *  保存信息
     * </pre>
     *
     * @param vehicleSubModel 数据信息
     */
    Integer insertVehicleSubModel(VehicleSubModel vehicleSubModel);

    /**
     * <pre>
     * 根据ID更新信息
     * </pre>
     *
     * @param vehicleSubModel 数据信息
     * @return 修改数量
     */
    int updateVehicleSubModel(VehicleSubModel vehicleSubModel);

    /**
     * <pre>
     *  根据ID删除信息
     * </pre>
     *
     * @param vehicleSubModelId 车辆配置表Id
     * @return 删除数量
     */
    int deleteById(Long vehicleSubModelId);

 }
