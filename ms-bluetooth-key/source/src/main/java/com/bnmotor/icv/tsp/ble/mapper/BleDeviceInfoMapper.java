package com.bnmotor.icv.tsp.ble.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.ble.model.entity.DeviceBleInfoPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName: TspDeviceBleInfo
* @Description: 设备蓝牙信息 dao层
 * @author shuqi1
 * @since 2020-07-20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Mapper
public interface BleDeviceInfoMapper extends BaseMapper<DeviceBleInfoPo> {
    /**
     *查询单个蓝牙配置信息
     * @param deviceBleInfoPo
     * @return
     */
    DeviceBleInfoPo queryBleDeviceInfo(DeviceBleInfoPo deviceBleInfoPo);

    /**
     *查询多条配置信息
     * @param deviceBleInfoPo
     * @return
     */
    List<DeviceBleInfoPo> queryBleDeviceInfoList(DeviceBleInfoPo deviceBleInfoPo);

    /**
     *添加蓝牙配置信息
     * @param deviceBleInfoPo
     * @return
     */
    int addBleDeviceInfo(DeviceBleInfoPo deviceBleInfoPo);

    /**
     * 更新蓝牙配置信息
     * @param deviceBleInfoPo
     * @return
     */
    int updateBleDeviceInfo(DeviceBleInfoPo deviceBleInfoPo);

    /**
     * 删除蓝牙配置信息
     * @param deviceBleInfoPo
     * @return
     */
    int deleteBleDeviceInfo(DeviceBleInfoPo deviceBleInfoPo);

}
