package com.bnmotor.icv.tsp.ble.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ble.model.entity.BleKeyServicePo;
import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyModifyAuthDto;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyModifyDateDto;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyModifyNameDto;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleModDevNameDto;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleKeyModifyAuthVo;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleKeyModifyVo;

import java.text.ParseException;

/**
 * @ClassName: BleReviseService
 * @Description: 蓝牙钥匙属性修改接口类
 * @author: shuqi1
 * @date: 2020/6/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface BleKeyMapService extends IService<BleKeyServicePo> {
    /**
     * 修改蓝牙钥匙授权权限
     * @param bleKeyModifyAuthDto
     * @param uid
     * @return
     */
    BleKeyModifyAuthVo reviseBleKeyAuth(BleKeyModifyAuthDto bleKeyModifyAuthDto, String projectId,String uid);

    /**
     * 查询已经修的蓝牙钥匙
     * @param bleKeyInfoPo
     * @return
     */

    BleKeyModifyVo queryReviseBleKeyInfo(UserBleKeyPo bleKeyInfoPo);

    /**
     * 修改蓝牙钥匙有效期，先存放到临时表
     * @param bleKeyModifyDateDto
     * @param projectId
     * @param uid
     * @return
     * @throws ParseException
     */
    UserBleKeyPo updateDbExpireDate(BleKeyModifyDateDto bleKeyModifyDateDto, String projectId, String  uid) throws ParseException;

    /**
     * 修改蓝牙钥匙名字
     * @param bleKeyModifyNameDto
     * @param projectId
     * @param userId
     * @return
     */
    BleKeyModifyAuthVo updateDbBleKeyName(BleKeyModifyNameDto bleKeyModifyNameDto, String projectId, String  userId);

    /**
     * 修改车牌号
     * @param bleModDevNameDto
     * @param projectId
     * @param userId
     * @return
     */
    boolean updateDbdeviceName(BleModDevNameDto bleModDevNameDto, String projectId, String  userId);

    /**
     * 检查能否修改
     * @param deviceId
     * @param projectId
     * @param uid
     * @return
     * @throws ParseException
     */
    void checkCanModify(String deviceId,String bleKeyId, String projectId, String  uid);

}
