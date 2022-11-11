/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package gaea.vehicle.basic.service.service;

import gaea.vehicle.basic.service.models.domain.VehicleEcuBom;
import gaea.vehicle.basic.service.models.query.VehicleEcuBomQuery;
import gaea.vehicle.basic.service.models.request.VehicleEcuBomRequest;

import java.util.List;

/**
 * <pre>
 * ECU信息表业务代码,流程控制和业务流程主要还是在Api上写.
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
public interface VehicleEcuBomService {
    /**
     * <pre>
     * 分页查询ECU信息表数据
     * </pre>
     *
     * @param query 查询条件,继承分页信息
     * @return ECU信息表数据集合
     */
    List<VehicleEcuBom> queryPage(VehicleEcuBomQuery query);

    /**
     * <pre>
     * 根据ID查询ECU信息表数据
     * </pre>
     *
     * @param vehicleEcuBomId ECU信息表Id
     * @return ECU信息表
     */
    VehicleEcuBom queryById(Long vehicleEcuBomId);

    /**
     * <pre>
     * 保存信息
     * </pre>
     *
     * @param vehicleEcuBom ECU信息表
     * @return 保存数据ID
     */
    Long insertVehicleEcuBom(VehicleEcuBom vehicleEcuBom);

    /**
     * <pre>
     * 根据ID更新信息
     * </pre>
     *
     * @param vehicleEcuBom ECU信息表
     * @return 更新数量
     */
    int updateVehicleEcuBom(VehicleEcuBom vehicleEcuBom);

    /**
     * <pre>
     * 根据ID删除信息
     * </pre>
     *
     * @param vehicleEcuBomId ECU信息表Id
     * @return 删除数量
     */
    int deleteById(Long vehicleEcuBomId);

    List<VehicleEcuBom> queryByVin(VehicleEcuBomRequest vehicleEcuBomReq);
}
