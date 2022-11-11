package com.bnmotor.icv.tsp.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bnmotor.icv.tsp.device.model.entity.VehicleDevicePo;
import com.bnmotor.icv.tsp.device.model.entity.VehiclePo;
import com.bnmotor.icv.tsp.device.model.request.vehicle.QueryVehicleDto;
import com.bnmotor.icv.tsp.device.model.request.vehicle.VehicleCerStatusUpdateDto;
import com.bnmotor.icv.tsp.device.model.request.vehicle.VehicleLicPlateDto;
import com.bnmotor.icv.tsp.device.model.response.vehDetail.VehInfoVo;
import com.bnmotor.icv.tsp.device.model.response.vehicle.CondenseVehicleVo;
import com.bnmotor.icv.tsp.device.model.response.vehicle.VehicleListVo;
import com.bnmotor.icv.tsp.device.model.response.vehicle.VehicleVo;
import com.bnmotor.icv.tsp.device.model.response.vehDetail.VehicleDetailVo;

import java.util.Collection;
import java.util.List;

/**
 * @ClassName: IVehLabelService
 * @Description: 车辆管理接口，提供车辆
 * @author: zhangwei2
 * @date: 2020/7/5
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IVehicleService {
    /**
     * 根据条件分页查询所有车辆信息
     *
     * @return 条件过滤后的车辆列表
     */
    IPage<VehicleListVo> list(QueryVehicleDto vehicleDto);

    /**
     * 查询车辆的详细信息,包括车辆基本信息，车辆零部件信息;车辆标签等信息
     *
     * @param vin 车辆唯一标识
     * @return 车辆详情信息
     */
    VehicleDetailVo getVehDetail(String vin);

    /**
     * 查询型号统计详情
     *
     * @return 车辆型号对应数据集合
     */
    List<VehiclePo> listVehicle(Collection<String> vins);

    /**
     * 根据 vin查询查询信息
     *
     * @param vin 车辆唯一标识
     * @return 车辆基本信息
     */
    VehicleVo getVehicleVo(String vin);

    /**
     * 根据orgId获取VIN列表
     */
    List<String> listVinsByOrgId(Long orgId);

    /**
     * 判断车辆是否有效(售出并且未报废登记)
     */
    boolean checkValid(String vin);

    /**
     * 根据配置id,标签id异步查询车辆信息
     *
     * @param uid       用户uid
     * @param configIds 配置id集合
     * @param labelIds  标签id集合
     */
    void aysnQuery(Long uid, List<Long> configIds, List<Long> labelIds);

    /**
     * 更新车牌号码
     */
    int updateDrivingLicPlate(VehicleLicPlateDto vehicleLicPlateDto);

    /**
     * 检查车牌号码是否存在
     */
    boolean checkDrivingLicPlate(VehicleLicPlateDto vehicleLicPlateDto);

    /**
     * 根据组合id分页查询车辆信息
     *
     * @param modelIds 车型ids
     * @param fromId   起始条目
     * @param size     每页条目
     * @return 车辆信息集合
     */
    List<CondenseVehicleVo> listByCombinedId(List<Long> modelIds, Long fromId, Integer size);

    /**
     * 批量插入车辆
     */
    void saveVehicles(List<VehiclePo> vehicles);

    /**
     * 批量插入车辆
     */
    void saveVehDevices(List<VehicleDevicePo> vehDevices);

    /**
     * 根据车架号查询车辆
     *
     * @param vin 车架号
     * @return 车辆实体数据
     */
    VehiclePo getVehicle(String vin);

    VehInfoVo getVehInfo(String vin);

    /**
     * 更新车辆实名认证信息
     * @param vehicleCerStatusUpdateDto
     */
    void updateCertificationStatus(VehicleCerStatusUpdateDto vehicleCerStatusUpdateDto);
}
