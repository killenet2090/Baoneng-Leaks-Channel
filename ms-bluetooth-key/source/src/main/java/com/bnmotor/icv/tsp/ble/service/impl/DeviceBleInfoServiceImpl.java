package com.bnmotor.icv.tsp.ble.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ble.model.entity.DeviceBleInfoPo;
import com.bnmotor.icv.tsp.ble.mapper.BleDeviceInfoMapper;
import com.bnmotor.icv.tsp.ble.service.DeviceBleInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author shuqi1
 * @ClassName: TspDeviceBleInfo
 * @Description: 设备蓝牙信息 服务实现类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-20
 */

@Service
public class DeviceBleInfoServiceImpl extends ServiceImpl<BleDeviceInfoMapper, DeviceBleInfoPo> implements DeviceBleInfoService {
    @Resource
    private BleDeviceInfoMapper bleDeviceInfoMapper;

    @Override
    public DeviceBleInfoPo queryBleDeviceInfo(DeviceBleInfoPo deviceBleInfoPo) {
        DeviceBleInfoPo deviceBleInfoPoQuery = bleDeviceInfoMapper.queryBleDeviceInfo(deviceBleInfoPo);
        return deviceBleInfoPoQuery;
    }


    @Override
    public int addBleDeviceInfo(DeviceBleInfoPo deviceBleInfoPo) {
        int row = bleDeviceInfoMapper.addBleDeviceInfo(deviceBleInfoPo);
        return row;
    }

    @Override
    public int updateBleDeviceInfo(DeviceBleInfoPo deviceBleInfoPo) {
        int row = bleDeviceInfoMapper.updateBleDeviceInfo(deviceBleInfoPo);
        return row;
    }

    @Override
    public int deleteBleDeviceInfo(DeviceBleInfoPo deviceBleInfoPo) {
        int row = bleDeviceInfoMapper.deleteBleDeviceInfo(deviceBleInfoPo);
        return row;
    }
}
