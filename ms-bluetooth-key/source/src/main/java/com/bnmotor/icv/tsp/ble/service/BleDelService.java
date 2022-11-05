package com.bnmotor.icv.tsp.ble.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleDeviceDelDto;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleUserKeyDelDto;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleDeviceDelVo;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleKeyDelVo;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyDelDto;
import com.bnmotor.icv.tsp.ble.model.response.ble.VehicleInfoVo;

import java.util.List;

/**
 * @ClassName: BleDeRegService
 * @Description: 蓝牙钥匙注销码接口
 * @author: shuqi1
 * @date: 2020/7/20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface BleDelService extends IService<UserBleKeyPo> {
    /**
     * 根据蓝牙钥匙判断数据库是否存在
     * @param bleKeyDelDto
     * @return
     */
    boolean bleKeyIsExistInDb(BleKeyDelDto bleKeyDelDto,String projectId,String userId);
    /**
     * 判断用户是否存在蓝牙钥匙
     * @param bleUserKeyDelDto
     * @param projectId
     * @param userId
     * @return
     */
    List<UserBleKeyPo> queryUserKeysFromDb(BleUserKeyDelDto bleUserKeyDelDto, String projectId, String userId);
    /**
     * 根据车辆判断数据库是否存在
     * @param deviceId
     * @return
     */
    boolean deviceIsExistInDb(String  deviceId, String projectId);

    /**
     * 查询没有生效的蓝牙钥匙数据
     * @param projectId
     * @param bleDeviceDelDto
     * @return
     */
    List<UserBleKeyPo> queryInvalidBleKey(String projectId, BleDeviceDelDto bleDeviceDelDto);

    /**
     * 合成用于查询那些已经注销掉的蓝牙钥匙以后再次受到注销命令的请求
     * @param bleKeyDelDto
     */
    BleKeyDelVo AssembleDelBleVo(BleKeyDelDto bleKeyDelDto);

    /**
     * 合成用于查询那些已经注销掉扯下的全部蓝牙钥匙以后再次受到注销命令的请求
     * @param bleDeviceDelDto
     */
    BleDeviceDelVo AssembleDelBleDeviceVo(BleDeviceDelDto bleDeviceDelDto);

    /**
     * 注销推送消息
     */
    void mobileMessagePush(UserBleKeyPo userBleKeyPo, VehicleInfoVo vehicleInfoVo,String userId);

    /**
     * 删除蓝牙钥匙
     * @param userBleKeyPoQuery
     * @param uid
     * @param userName
     * @return
     */
    BleKeyDelVo delBleKey(UserBleKeyPo userBleKeyPoQuery,String uid,String userName);

    /**
     * 删除没有激活的蓝牙钥匙
     * @param userBleKeyPo
     * @param projectId
     * @param userId
     * @return
     */
    BleKeyDelVo delMoveBleKey(UserBleKeyPo userBleKeyPo, VehicleInfoVo vehicleInfoVo,String projectId, String userId,String userName);

    /**
     * 删除车辆下全部蓝牙钥匙
     * @param bleDeviceDelDto
     * @param projectId
     * @param userId
     * @return
     */
    int delDeviceAllBleKey(BleDeviceDelDto bleDeviceDelDto, String projectId, String userId,String userName);
}
