package com.bnmotor.icv.tsp.ble.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ble.mapper.BleAckPushMapper;
import com.bnmotor.icv.tsp.ble.model.entity.BleAckPushPo;
import com.bnmotor.icv.tsp.ble.service.BleAckPushService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shuqi1
 * @ClassName: TspBleAckPush
 * @Description: 记录下发数据类型和流水号，用于消息推送 服务实现类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-08-13
 */

@Service
public class BleAckPushServiceImpl extends ServiceImpl<BleAckPushMapper, BleAckPushPo> implements BleAckPushService {

    @Resource
    BleAckPushMapper bleAckPushMapper;

    @Override
    public int addBleAckPushService(BleAckPushPo bleAckPushPo) {
        int rows = bleAckPushMapper.addBleAckPush(bleAckPushPo);
        return rows;
    }

    @Override
    public int addBleAckPushService(List<BleAckPushPo> bleAckPushPoList) {
        for (BleAckPushPo bleAckPushPo : bleAckPushPoList) {
            int rows = bleAckPushMapper.addBleAckPush(bleAckPushPo);
        }
        return 0;
    }

    @Override
    public int updateBleAckPushService(BleAckPushPo bleAckPushPo) {
        int rows = bleAckPushMapper.deleteBleAckPushById(bleAckPushPo);
        return rows;
    }

    @Override
    public int moveBleAckPushServiceList(List<BleAckPushPo> bleAckPushPoList) {
        int rows = bleAckPushMapper.moveBleAckPushServiceList(bleAckPushPoList);
        return rows;
    }

    @Override
    public int delBleAckPushService(BleAckPushPo bleAckPushPo) {
        int i = bleAckPushMapper.deleteBleAckPushById(bleAckPushPo);
        return i;
    }

    @Override
    public int delBleAckPushServiceList(List<BleAckPushPo> bleAckPushPoList) {
        int i = bleAckPushMapper.deleteBleAckPushList(bleAckPushPoList);
        return i;
    }
}
