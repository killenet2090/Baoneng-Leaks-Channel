package com.bnmotor.icv.tsp.ble.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ble.model.entity.BleAuthPo;
import com.bnmotor.icv.tsp.ble.model.entity.BleAuthServicePo;
import com.bnmotor.icv.tsp.ble.mapper.BleAuthServiceMapper;
import com.bnmotor.icv.tsp.ble.model.entity.BleKeyServicePo;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleKeyModifyVo;
import com.bnmotor.icv.tsp.ble.service.BleAuthMapService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: BleAuthService
 * @Description: 蓝牙钥匙授权接口
 * @author: shuqi1
 * @date: 2020/6/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
public class BleAuthMapServiceImpl extends ServiceImpl<BleAuthServiceMapper, BleAuthServicePo> implements BleAuthMapService {
    @Resource
    BleAuthServiceMapper bleAuthServiceMapper;

    @Override
    public BleKeyModifyVo reviseBleKeyAuthInfo(List<BleKeyServicePo> bleKeyServicePoList) {
        return null;
    }

    @Override
    public int addBleKeyAuthMapInfo(BleAuthServicePo bleAuthServicePo) {
        int rows = bleAuthServiceMapper.addBleAuthService(bleAuthServicePo);
        return rows;
    }

    @Override
    public int addBleKeyAuthMapInfo(List<BleAuthServicePo> bleAuthServicePoList) {
        for (BleAuthServicePo bleAuthServicePo : bleAuthServicePoList) {
            bleAuthServiceMapper.addBleAuthService(bleAuthServicePo);
        }

        return 0;
    }

    @Override
    public int updateBleKeyAuthMapInfo(BleAuthServicePo bleAuthServicePo) {
        bleAuthServiceMapper.updateBleAuthService(bleAuthServicePo);
        return 0;
    }

    @Override
    public int delBleKeyAuthMapInfo(List<BleAuthPo> bleAuthServicePoList) {
        return 0;
    }

}
