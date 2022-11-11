/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.vehicle.basic.service.dao;

import gaea.vehicle.basic.service.models.domain.VehicleEcuBom;
import gaea.vehicle.basic.service.models.query.VehicleEcuBomQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <pre>
 *   ECU信息表数据操作对象
 * </pre>
 *
 * @author   JianKang.Xia
 * @version  1.0.0
 */
@Mapper
public interface VehicleEcuBomMapper {
    /**
     * <pre>
     *  分页查询 ECU信息表数据
     * </pre>
     *
     * @param query 查询条件
     * @return ECU信息表数据集合
     */
    List<VehicleEcuBom> queryPage(VehicleEcuBomQuery query);

    /**
     * <pre>
     * 根据查询条件统计ECU信息表数量
     * </pre>
     *
     * @param query 查询条件
     * @return ECU信息表数量
     */
    int countPage(VehicleEcuBomQuery query);

    /**
     * <pre>
     *  根据ID查询ECU信息表数据
     * </pre>
     *
     * @param vehicleEcuBomId ECU信息表Id
     * @return 单条记录数据
     */
    VehicleEcuBom queryById(Long vehicleEcuBomId);

    /**
     * <pre>
     *  保存信息
     * </pre>
     *
     * @param vehicleEcuBom 数据信息
     */
    void insertVehicleEcuBom(VehicleEcuBom vehicleEcuBom);

    /**
     * <pre>
     * 根据ID更新信息
     * </pre>
     *
     * @param vehicleEcuBom 数据信息
     * @return 修改数量
     */
    int updateVehicleEcuBom(VehicleEcuBom vehicleEcuBom);

    /**
     * <pre>
     *  根据ID删除信息
     * </pre>
     *
     * @param vehicleEcuBomId ECU信息表Id
     * @return 删除数量
     */
    int deleteById(Long vehicleEcuBomId);

    /**
     * 根据vin查询
     * @param vehicleEcuBom
     * @return
     */
    List<VehicleEcuBom> queryByVin(VehicleEcuBom vehicleEcuBom);
}
