package com.bnmotor.icv.tsp.device.mapper;

import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import com.bnmotor.icv.tsp.device.service.mq.producer.dataaysn.VehicleDevice;
import com.bnmotor.icv.tsp.device.model.entity.VehicleDevicePo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangwei2
 * @ClassName: VehicleDevicePo
 * @Description: 车辆绑定设备信息 dao层
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-08-18
 */

@Mapper
public interface VehicleDeviceMapper extends AdamMapper<VehicleDevicePo> {
    /**
     * 通过vin码查询车辆绑定的零部件
     *
     * @param vin 车辆vin码
     * @return 车辆零部件
     */
    List<VehicleDevicePo> listByVinAndDeviceType(@Param("vin") String vin, @Param("deviceType") Integer deviceType);

    /**
     * 根据设备id查询当前绑定的车辆
     *
     * @param deviceId 设备id
     * @return 当前设备绑定的车辆
     */
    VehicleDevicePo getByDeviceId(@Param("deviceId") String deviceId);

    /**
     * 根据vin车辆设备绑定关系
     *
     * @param vins 车辆集合
     */
    List<VehicleDevice> listVehDeviceByVins(@Param("vins") List<String> vins);

    /**
     * 根据vin车辆设备绑定关系
     *
     * @param vins 车辆集合
     */
    List<VehicleDevicePo> listByVins(@Param("vins") List<String> vins);

    /**
     * 物理删除
     *
     * @param id 主键id
     * @return 成功条目
     */
    int delete(@Param("id") Long id);

    /**
     * 根据车架号和零部件类型删除车辆绑定的设备
     *
     * @param vin        车架号
     * @param deviceType 设备类型
     * @return 影响条目
     */
    int deleteByVinAndType(@Param("vin") String vin, @Param("deviceType") Integer deviceType);
}
