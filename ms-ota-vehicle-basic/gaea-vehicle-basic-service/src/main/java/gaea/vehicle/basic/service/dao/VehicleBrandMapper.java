/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.vehicle.basic.service.dao;

import gaea.vehicle.basic.service.models.domain.VehicleBrand;
import gaea.vehicle.basic.service.models.query.VehicleBrandQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <pre>
 *   车辆品牌表数据操作对象
 * </pre>
 *
 * @author   jiankang.xia
 * @version  1.0.0
 */
@Mapper
public interface VehicleBrandMapper {
    /**
     * <pre>
     *  分页查询 车辆品牌表数据
     * </pre>
     *
     * @param query 查询条件
     * @return 车辆品牌表数据集合
     */
    List<VehicleBrand> queryPage(VehicleBrandQuery query);

    /**
     *
     * @param
     * @return
     */
    List<VehicleBrand> queryNameAndId(VehicleBrand vehicleBrand);

    /**
     * <pre>
     * 根据查询条件统计车辆品牌表数量
     * </pre>
     *
     * @param query 查询条件
     * @return 车辆品牌表数量
     */
    int countPage(VehicleBrandQuery query);

    /**
     * <pre>
     *  根据ID查询车辆品牌表数据
     * </pre>
     *
     * @param vehicleBrandId 车辆品牌表Id
     * @return 单条记录数据
     */
    VehicleBrand queryById(Long vehicleBrandId);

    /**
     * <pre>
     *  保存信息
     * </pre>
     *
     * @param vehicleBrand 数据信息
     */
    Integer insertVehicleBrand(VehicleBrand vehicleBrand);

    /**
     * <pre>
     * 根据ID更新信息
     * </pre>
     *
     * @param vehicleBrand 数据信息
     * @return 修改数量
     */
    int updateVehicleBrand(VehicleBrand vehicleBrand);

    /**
     * <pre>
     *  根据ID删除信息
     * </pre>
     *
     * @param vehicleBrandId 车辆品牌表Id
     * @return 删除数量
     */
    int deleteById(Long vehicleBrandId);

 }
