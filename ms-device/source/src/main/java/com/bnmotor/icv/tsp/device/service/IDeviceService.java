package com.bnmotor.icv.tsp.device.service;

import com.bnmotor.icv.adam.data.mysql.metadata.Page;
import com.bnmotor.icv.adam.data.mysql.metadata.Pageable;
import com.bnmotor.icv.tsp.device.service.mq.producer.dataaysn.VehicleDevice;
import com.bnmotor.icv.tsp.device.model.entity.DeviceModelInfoPo;
import com.bnmotor.icv.tsp.device.model.entity.DevicePo;
import com.bnmotor.icv.tsp.device.model.response.device.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: IDeviceService
 * @Description: 设备服务
 * @author: zhangwei2
 * @date: 2020/7/18
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IDeviceService {
    /**
     * 根据条件查询车辆设备
     *
     * @param pageable    分页参数
     * @param searchKey   搜索key
     * @param searchValue 搜索值
     * @return 分页设备列表
     */
    Page<DeviceVo> listAllDevices(Pageable pageable, String searchKey, String searchValue);

    /**
     * 查询设备详细信息
     *
     * @param deviceId 设备id
     * @return 设备基本信息, 设备绑定车辆信息, 车辆车主信息, sim卡信息, 设备绑车历史信息
     */
    DeviceDetailVo getDetail(Long deviceId);

    /**
     * 分页查询设备列表
     *
     * @param pageable 分页参数
     * @return 设备型号信息
     */
    Page<DeviceTypeVo> listDeviceTypes(Pageable pageable);

    /**
     * 分页产线设备型号信息
     *
     * @param pageable   分页对象
     * @param deviceType 设备类型
     * @return 设备型号
     */
    Page<DeviceModelVo> listDeviceModels(Pageable pageable, Integer deviceType);

    /**
     * 根据设备类型查询该类型下所有设备型号
     *
     * @param deviceType 设备类型
     * @return 设备型号集合
     */
    List<DeviceModelInfoPo> listDeviceModel(Integer deviceType);

    /**
     * 根据车架号车型车辆设备集合
     *
     * @param vins 车架号
     * @return 车辆设备集合
     */
    List<VehicleDevice> listVehDevices(List<String> vins);

    /**
     * 根据设备类型和设备型号查询设备信息
     *
     * @param deviceType  设备类型
     * @param deviceModel 设备型号
     * @return 设备型号信息
     */
    CacheDeviceModelInfo getDeviceModelInfo(Integer deviceType, String deviceModel);

    /**
     * 分页产线设备型号信息
     *
     * @param pageable 分页对象
     * @param deviceId 绑定记录
     * @return 设备型号
     */
    Page<BindVehicleRecordVo> listBindRecords(Pageable pageable, String deviceId);

    /**
     * 查询所有车型零部件类型
     */
    List<DeviceModelInfoVo> listAllDeviceModelInfoVo();

    /**
     * 批量保存设备信息
     *
     * @param devices 设备信息
     * @return 影响条目
     */
    int saveDevices(List<DevicePo> devices);

    /**
     * 根据iccid获取当前绑定的设备
     */
    DevicePo getDeviceByIccid(String iccId);

    /**
     * 根据设备sn获取设备
     *
     * @param sn 设备sn
     * @return 设备
     */
    List<DevicePo> listDeviceBySn(@Param("sn") String sn);
}
