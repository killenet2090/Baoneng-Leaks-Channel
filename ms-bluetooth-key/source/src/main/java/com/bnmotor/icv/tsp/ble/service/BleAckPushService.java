package com.bnmotor.icv.tsp.ble.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ble.model.entity.BleAckPushPo;

import java.util.List;

/**
 * @ClassName: TspBleAckPush
 * @Description: 记录下发数据类型和流水号，用于消息推送 服务类
 * @author shuqi1
 * @since 2020-08-13
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface BleAckPushService extends IService<BleAckPushPo> {
    /**
     * 增加单个蓝牙钥匙
     * @param bleAckPushPo
     * @return
     */
    int addBleAckPushService(BleAckPushPo bleAckPushPo);

    /**
     * 批量增加蓝牙钥匙
     * @param bleAckPushPoList
     * @return
     */
    int addBleAckPushService(List<BleAckPushPo> bleAckPushPoList);

    /**
     * 更新蓝牙钥匙
     * @param bleAckPushPo
     * @return
     */
    int updateBleAckPushService(BleAckPushPo bleAckPushPo);

    /**
     * 把记录移到历史数据表中
     * @param bleAckPushPoList
     * @return
     */
    int moveBleAckPushServiceList(List<BleAckPushPo> bleAckPushPoList);

    /**
     * 删除蓝牙钥匙
     * @param bleAckPushPo
     * @return
     */
    int delBleAckPushService(BleAckPushPo bleAckPushPo);

    /**
     * 批量删除蓝牙钥匙
     * @param bleAckPushPoList
     * @return
     */
    int delBleAckPushServiceList(List<BleAckPushPo> bleAckPushPoList);
}
