package com.bnmotor.icv.tsp.ble.service;

import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyGeneratorDto;
import com.bnmotor.icv.tsp.ble.model.response.user.UserPhoneVo;

import java.util.Date;

public interface BleTboxService {
    /**
     * 车主申请发送消息到mqtt
     * @param userBleKeyPo
     * @param now
     * @param response
     * @param bleKeyGeneratorDto
     */
    void bleApply(UserBleKeyPo userBleKeyPo, Date now, UserPhoneVo response, BleKeyGeneratorDto bleKeyGeneratorDto);

    /**
     * 删除车下某把蓝牙钥匙
     *
     * @param userBleKeyPo
     * @param now
     * @param pushId
     */
    void delBleKey(UserBleKeyPo userBleKeyPo, Date now, String pushId, String uid, String userName);

    /**
     * 删除车下所有钥匙
     *
     * @param userBleKeyPo
     * @param now
     * @param pushId
     */
   void delDeviceBle(UserBleKeyPo userBleKeyPo, Date now, String pushId);
    /**
     * 修改蓝牙钥匙权限
     *
     * @param userBleKeyPo
     * @param now
     * @param pushId
     * @param bleRights
     * @param bleRight
     */
    void reviseBleKeyAuth(UserBleKeyPo userBleKeyPo, Date now, String pushId, String bleRights, Long bleRight);

    /**
     * 修改蓝牙钥匙有效期
     *
     * @param userBleKeyPo
     * @param now
     * @param pushId
     */
    void updateBleExpireDate(UserBleKeyPo userBleKeyPo, Date now, String pushId, String uid, String userName);
}
